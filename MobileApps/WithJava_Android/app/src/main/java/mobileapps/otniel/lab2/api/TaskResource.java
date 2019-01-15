package mobileapps.otniel.lab2.api;

import com.google.gson.Gson;

import org.json.JSONObject;

import mobileapps.otniel.lab2.persistance.token.Token;
import mobileapps.otniel.lab2.viewobjects.Page;
import mobileapps.otniel.lab2.viewobjects.Task;
import mobileapps.otniel.lab2.viewobjects.TaskPage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public  interface TaskResource {
    String BASE_URL="http://192.168.43.47:8080/";
    String ws_base_url="ws://192.168.43.47:8080/my-websocket-endpoint";

    @GET("api/tasks")
    Call<TaskPage> getTasks(@Header("Authorization") String auth);

    @PATCH("api/tasks/{id}")
    Call<Void> updateTask(@Path("id") int id, @Body Task task,@Header("Content-Type") String content_type,@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<Token> getToken(@Header("Content-Type") String contentType, @Header("Authorization") String authkey, @Header("Preference-Applied") String preference, @Field("grant_type")String grant_type, @Field("username") String username, @Field("password")String password);
}
