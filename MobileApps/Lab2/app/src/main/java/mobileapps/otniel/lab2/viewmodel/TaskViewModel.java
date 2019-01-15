package mobileapps.otniel.lab2.viewmodel;

import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.reactivex.functions.Consumer;
import mobileapps.otniel.lab2.api.TaskResource;

import mobileapps.otniel.lab2.connection.MyBroadcastReceiver;
import mobileapps.otniel.lab2.connection.NotifyObject;
import mobileapps.otniel.lab2.persistance.task.TaskDatabase;
import mobileapps.otniel.lab2.persistance.task.TaskRepository;
import mobileapps.otniel.lab2.persistance.token.TokenDatabase;
import mobileapps.otniel.lab2.persistance.token.TokenRepository;
import mobileapps.otniel.lab2.viewobjects.Task;
import mobileapps.otniel.lab2.viewobjects.TaskPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class TaskViewModel extends ViewModel implements SensorEventListener,Consumer<NotifyObject> {
    private static final String TAG=TaskViewModel.class.getCanonicalName();
    private MutableLiveData<List<Task>> tasks;
    private List<Task> currentTasks;
    private org.java_websocket.client.WebSocketClient mWebSocketClient;
    private TaskRepository taskRepository;
    private TokenRepository tokenRepository;
    private boolean connected=false;
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastUpdate;


    public TaskViewModel(final Context context){
        taskRepository=TaskRepository.getInstance(TaskDatabase.getAppDatabase(context).taskDao());
        tokenRepository=TokenRepository.getInstance(TokenDatabase.getAppDatabase(context).tokenDao());
        sensorManager=(SensorManager)context.getSystemService(Service.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL);
        connectWebSocket();

        final Handler handler=new Handler();
       handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean tempConn=connected;
                connected = isInternetAvailable();
                if(tempConn==false && connected==true) {
                    //Log.d("TAGULET", String.valueOf(connected));
                    loadTasks();
                }
                handler.postDelayed(this, 5000);

            }
        }, 5000);
    }

    public boolean isInternetAvailable() {
        try {
            final String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMobile = cm.getNetworkInfo(cm.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(cm.TYPE_WIFI);
        if (netInfoMobile != null && netInfoMobile.isConnected()) {
            Log.v("TAG", "Mobile Internet connected");
            return true;
        }
        if (netInfoWifi != null && netInfoWifi.isConnected()) {
            Log.v("TAG", "Wifi Internet connected");
            return true;
        }
        return false;
    }

    private void connectWebSocket(){
        URI uri;
        try {
            uri = new URI(TaskResource.ws_base_url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
//
        mWebSocketClient = new WebSocketClient(uri) {
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL+connected);
            }

            public void onMessage(String s) {
                NotifyObject notifyObject=new Gson().fromJson(s,NotifyObject.class);
                Task fromServerTask=notifyObject.getTask();
                if(notifyObject.getOperation().equals("delete")){
                    Log.d("WEBSOCKET_DELETE","start");
                    deleteFromStorage(fromServerTask);
                }else{
                    Log.d("WEBSOCKET_ADD","start");
                    addInStorage(fromServerTask);
                }
            }


            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();

    }

    private void addInStorage(Task task){
        taskRepository.insertTask(task.getId(),task.getDescription(),task.getImportance());
        currentTasks=loadLocalTasks();
        tasks.postValue(currentTasks);
    }

    private void deleteFromStorage(Task task){
        taskRepository.delete(task);
        currentTasks=loadLocalTasks();
        tasks.postValue(currentTasks);
    }

    public LiveData<List<Task>> getTasks(){

        if(tasks==null){
            tasks=new MutableLiveData<List<Task>>();
            loadTasks();
        }
        return tasks;
    }

    private List<Task> loadLocalTasks(){
        List<Task> localTasks= null;
        try {
            localTasks = taskRepository.getAll();
            return localTasks;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateDbAndResources(Map<Integer,Task> allTasks,Retrofit retrofit,TaskResource api){
        for(Task task:allTasks.values()){
            Task fromDbTask=taskRepository.getTask(task.getId());
            if(fromDbTask==null){
                taskRepository.insertTask(task.getId(),task.getDescription(),task.getImportance());
            }else {
                if (!(task.getDescription().equals(fromDbTask.getDescription()))) {
                    task.setDescription(fromDbTask.getDescription());
                }
                if (!(task.getImportance() == fromDbTask.getImportance())) {
                    task.setImportance(fromDbTask.getImportance());
                }
            }
        }

        doCache(allTasks);

        for(Task t:allTasks.values()){

            Call<Void> updateCall=api.updateTask(t.getId(),t,"application/json","Bearer "+tokenRepository.getToken().getAccess_token());
            updateCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d(TAG,"SUCCES");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG,"update failed",t);
                }
            });
        }

    }

    private boolean checkIfPresent(Map<Integer,Task> allTasks,Task t){
        if(allTasks.get(t.getId())!=null) return true;
        return false;
    }

    private void doCache(Map<Integer,Task> allTasks){
        try {
            List<Task> dbTasks=taskRepository.getAll();
            for(Task t:dbTasks){
                if(!checkIfPresent(allTasks,t)){
                    taskRepository.delete(t);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks(){

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TaskResource.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();

        final TaskResource api=retrofit.create(TaskResource.class);
        Call<TaskPage> call=api.getTasks("Bearer "+tokenRepository.getToken().getAccess_token());
        call.enqueue(new Callback<TaskPage>() {
            @Override
            public void onResponse(Call<TaskPage> call, Response<TaskPage> response) {
                Log.d("DATE:","A MERS");
                Map<Integer,Task> allTasks=response.body().getTasks();
                updateDbAndResources(allTasks,retrofit,api);

                List<Task> listWithTasks=new ArrayList<>();
                for(Task t:allTasks.values()){
                    listWithTasks.add(t);
                }

                tasks.setValue(listWithTasks);
                currentTasks=listWithTasks;

            }

            @Override
            public void onFailure(Call<TaskPage> call, Throwable t) {
                Log.d(TAG,"loadTasks failed",t);
                currentTasks=loadLocalTasks();
                tasks.setValue(currentTasks);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(sensorEvent);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
//            if (actualTime - lastUpdate < 10000) {
//                return;
//            }
            lastUpdate = actualTime;
            Collections.shuffle(currentTasks);
            tasks.postValue(currentTasks);
            Log.d("ACELERATOR","SHUFFLe");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void accept(NotifyObject notifyObject) throws Exception {
        System.out.println("Am fost notificat de pe backend");
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context ctxt;

        public Factory(Context ctxt) {
            this.ctxt=ctxt.getApplicationContext();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return((T)new TaskViewModel(ctxt));
        }
    }





}
