package ada.osc.myfirstweatherapp.presentation;

import ada.osc.myfirstweatherapp.Constants;
import ada.osc.myfirstweatherapp.interaction.ApiInteractor;
import ada.osc.myfirstweatherapp.interaction.ApiInteractorImpl;
import ada.osc.myfirstweatherapp.interaction.DbInteractor;
import ada.osc.myfirstweatherapp.interaction.DbInteractorImpl;
import ada.osc.myfirstweatherapp.model.WeatherResponse;
import ada.osc.myfirstweatherapp.view.weather.WeatherContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by avukelic on 24-May-18.
 */
public class WeatherPresenter implements WeatherContract.Presenter {

    private final ApiInteractor apiInteractor;
    private final DbInteractor dbInteractor;

    private WeatherContract.View weatherView;

    public WeatherPresenter() {
        apiInteractor = new ApiInteractorImpl();
        dbInteractor = new DbInteractorImpl();
    }

    @Override
    public void setView(WeatherContract.View weatherView) {
        this.weatherView = weatherView;
    }

    @Override
    public void getWeather(String location) {
        apiInteractor.getWeather(location, getWeatherCallback(location));
    }


    private Callback<WeatherResponse> getWeatherCallback(String location) {
        return new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response != null && response.body() != null) {
                    WeatherResponse data = response.body();
                    weatherView.setCurrentTemperatureValues(toCelsiusFromKelvin(data.getMain().getTemp()));
                    weatherView.setPressureValues(data.getMain().getPressure());
                    weatherView.setDescriptionValues(data.getWeatherObject().getDescription());
                    weatherView.setWindValues(data.getWind().getSpeed());
                    weatherView.setWeatherIcon(createWeatherIconValue(data.getWeatherObject().getMain()));
                    weatherView.setMinTemperatureValues(toCelsiusFromKelvin(data.getMain().getTemp_min()));
                    weatherView.setMaxTemperatureValues(toCelsiusFromKelvin(data.getMain().getTemp_max()));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherView.onGetWeatherFailure();
            }
        };
    }

    private String createWeatherIconValue(String description) {
        if (description != null)
            switch (description) {
                case Constants.SNOW_CASE: {
                    return Constants.SNOW;
                }
                case Constants.RAIN_CASE: {
                    return Constants.RAIN;
                }
                case Constants.CLEAR_CASE: {
                    return Constants.SUN;
                }
                case Constants.MIST_CASE: {
                    return Constants.FOG;
                }
                case Constants.FOG_CASE: {
                    return Constants.FOG;
                }
                case Constants.HAZE_CASE: {
                    return Constants.FOG;
                }
                case Constants.CLOUD_CASE: {
                    return Constants.CLOUD;
                }
            }
        return "";
    }

    private double toCelsiusFromKelvin(double temperature) {
        return temperature - 273;
    }
}
