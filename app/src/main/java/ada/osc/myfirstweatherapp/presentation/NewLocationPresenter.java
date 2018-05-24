package ada.osc.myfirstweatherapp.presentation;

import ada.osc.myfirstweatherapp.interaction.ApiInteractor;
import ada.osc.myfirstweatherapp.interaction.DbInteractor;
import ada.osc.myfirstweatherapp.model.WeatherResponse;
import ada.osc.myfirstweatherapp.view.newLocation.NewLocationContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by avukelic on 24-May-18.
 */
public class NewLocationPresenter implements NewLocationContract.Presenter {

    private ApiInteractor apiInteractor;
    private DbInteractor dbInteractor;
    private NewLocationContract.View newLocationView;

    public NewLocationPresenter(ApiInteractor apiInteractor, DbInteractor dbInteractor) {
        this.apiInteractor = apiInteractor;
        this.dbInteractor = dbInteractor;
    }

    @Override
    public void setView(NewLocationContract.View newLocationView) {
        this.newLocationView = newLocationView;
    }

    @Override
    public void addNewLocation(String location) {
        apiInteractor.checkLocationIfExists(location, getWeatherCheckCallback(location));
    }

    @Override
    public boolean isLocationAlreadyOnList(String location) {
        return dbInteractor.isLocationAlreadyOnList(location);
    }

    private Callback<WeatherResponse> getWeatherCheckCallback(final String location) {
        return new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful()){
                    saveLocation(location);
                    newLocationView.onNewLocationAdded();
                } else {
                    newLocationView.showOnLocationDoesNotExistsError();
                }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
            }
        };
    }

    private void saveLocation(String location) {
        dbInteractor.saveLocation(location);
    }
}
