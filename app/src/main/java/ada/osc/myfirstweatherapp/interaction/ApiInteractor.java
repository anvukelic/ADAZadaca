package ada.osc.myfirstweatherapp.interaction;

import ada.osc.myfirstweatherapp.model.WeatherResponse;
import retrofit2.Callback;

/**
 * Created by avukelic on 24-May-18.
 */
public interface ApiInteractor {

    void checkLocationIfExists(String location, Callback<WeatherResponse> callback);
}
