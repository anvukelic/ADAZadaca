package ada.osc.taskie;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import retrofit2.Retrofit;

/**
 * Created by avukelic on 19-May-18.
 */
public class App extends Application {

    private static Retrofit retrofit;
    private static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = RetrofitUtil.createRetrofit();
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return apiService;
    }

}
