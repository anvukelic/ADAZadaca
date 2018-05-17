package ada.osc.taskie.networking;

import ada.osc.taskie.model.LoginResponse;
import ada.osc.taskie.model.RegistrationToken;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by avukelic on 16-May-18.
 */
public interface ApiService {
    @POST("api/register/")
    Call<RegistrationToken> registerUser(@Body RegistrationToken registrationToken);

    @POST("api/login/")
    Call<LoginResponse> loginUser(@Body RegistrationToken registrationToken);

    @POST("api/note/")
    Call<Task> postNewTask(@Header("authorization") String header, @Body Task task);

    @POST("api/note/edit")
    Call<Task> updateTask(@Header("authorization") String header, @Body Task task);

    @POST("api/note/delete")
    Call<Void> deleteTask(@Header("authorization") String header, @Query("noteId") String noteId);

    @POST("api/note/favorite")
    Call<Void> faveTask(@Header("authorization") String header, @Query("noteId") String noteId);

    @GET("api/note/")
    Call<TaskList> getTasks(@Header("authorization") String header);
}
