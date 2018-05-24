package ada.osc.myfirstweatherapp.interaction;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.Constants;
import ada.osc.myfirstweatherapp.model.WeatherResponse;
import retrofit2.Callback;

/**
 * Created by avukelic on 24-May-18.
 */
public class ApiInteractorImpl implements ApiInteractor {

    public ApiInteractorImpl() {
    }

    @Override
    public void checkLocationIfExists(String location, Callback<WeatherResponse> callback) {
        App.getApiService().getWeather(Constants.APP_ID, location).enqueue(callback);
    }

    @Override
    public void getWeather(String location, Callback<WeatherResponse> callback) {
        App.getApiService().getWeather(Constants.APP_ID,location).enqueue(callback);
    }
}
