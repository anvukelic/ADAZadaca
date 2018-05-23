package ada.osc.myfirstweatherapp.network;

import ada.osc.myfirstweatherapp.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @GET("/data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("appid") String apiKey, @Query("q") String city);


}
