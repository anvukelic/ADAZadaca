package ada.osc.myfirstweatherapp;

import android.app.Application;

import ada.osc.myfirstweatherapp.network.ApiService;
import ada.osc.myfirstweatherapp.network.RetrofitUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by Filip on 01/04/2016.
 */
public class App extends Application {

    private static App sInstance;
    private static Retrofit retrofit;
    private static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = RetrofitUtil.createRetrofit();
        apiService = retrofit.create(ApiService.class);
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("taskie.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public static ApiService getApiService() {
        return apiService;
    }
}
