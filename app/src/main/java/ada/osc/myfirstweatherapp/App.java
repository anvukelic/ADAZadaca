package ada.osc.myfirstweatherapp;

import android.app.Application;

import ada.osc.myfirstweatherapp.interaction.ApiInteractor;
import ada.osc.myfirstweatherapp.interaction.ApiInteractorImpl;
import ada.osc.myfirstweatherapp.interaction.DbInteractor;
import ada.osc.myfirstweatherapp.interaction.DbInteractorImpl;
import ada.osc.myfirstweatherapp.network.ApiService;
import ada.osc.myfirstweatherapp.network.RetrofitUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;

/**
 * Created by Filip on 01/04/2016.
 */
public class App extends Application {

    private static Retrofit retrofit;
    private static ApiService apiService;
    private static Realm realm;

    private static ApiInteractor apiInteractor;
    private static DbInteractor dbInteractor;

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

        realm = Realm.getDefaultInstance();

        apiInteractor = new ApiInteractorImpl();
        dbInteractor = new DbInteractorImpl();
    }

    public static Realm getRealm() {
        return realm;
    }

    public static ApiInteractor getApiInteractor() {
        return apiInteractor;
    }

    public static DbInteractor getDbInteractor() {
        return dbInteractor;
    }

    public static ApiService getApiService() {
        return apiService;
    }
}
