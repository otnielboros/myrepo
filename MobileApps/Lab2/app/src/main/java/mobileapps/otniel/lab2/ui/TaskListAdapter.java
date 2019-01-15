package mobileapps.otniel.lab2.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobileapps.otniel.lab2.R;
import mobileapps.otniel.lab2.api.TaskResource;
import mobileapps.otniel.lab2.persistance.task.TaskDatabase;
import mobileapps.otniel.lab2.persistance.task.TaskRepository;
import mobileapps.otniel.lab2.persistance.token.TokenDatabase;
import mobileapps.otniel.lab2.persistance.token.TokenRepository;
import mobileapps.otniel.lab2.viewmodel.TaskViewModel;
import mobileapps.otniel.lab2.viewobjects.Page;
import mobileapps.otniel.lab2.viewobjects.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private static final String TAG=TaskViewModel.class.getCanonicalName();
    private Context mContext;
    private List<Task> mTasks;
    private TaskRepository taskRepository;
    private TokenRepository tokenRepository;

    public TaskListAdapter(Context context, List<Task> tasks){
        this.mContext=context;
        this.mTasks=tasks;
        taskRepository=TaskRepository.getInstance(TaskDatabase.getAppDatabase(context).taskDao());
        tokenRepository=TokenRepository.getInstance(TokenDatabase.getAppDatabase(context).tokenDao());
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.task_layout,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task=mTasks.get(position);
        holder.task=task;
        holder.id.setText("Id: ");
        holder.importance.setText("Importance: ");
        holder.description.setText("Description: ");
        holder.taskId.setText(String.valueOf(task.getId()));
        holder.taskDescription.setText(task.getDescription());
        holder.taskImportance.setText(String.valueOf(task.getImportance()));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView taskId,id,description,importance;
        EditText taskDescription,taskImportance;
        Button updateButton;
        Task task=null;

        void update(Task task){
            taskRepository.updateTask(task);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TaskResource.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .build();

            TaskResource api=retrofit.create(TaskResource.class);

            Call<Void> call=api.updateTask(task.getId(),task,"application/json","Bearer "+tokenRepository.getToken().getAccess_token());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("UpdateTask","Update with succes");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("UpdateTask","Update failed",t);
                }
            });
        }

        TaskViewHolder(View itemView) {
            super(itemView);
            taskId=itemView.findViewById(R.id.taskId);
            id=itemView.findViewById(R.id.id);
            taskDescription=itemView.findViewById(R.id.taskDescription);
            taskImportance=itemView.findViewById(R.id.taskImportance);
            description=itemView.findViewById(R.id.description);
            importance=itemView.findViewById(R.id.importance);
            updateButton=itemView.findViewById(R.id.updateButton);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.setDescription(taskDescription.getText().toString());
                    task.setImportance(Integer.parseInt(taskImportance.getText().toString()));
                    Log.d(TAG,String.valueOf(String.valueOf(taskId.getText())+String.valueOf(task.getDescription())+String.valueOf(task.getImportance())));
                    update(task);
                }
            });

        }
    }
}
