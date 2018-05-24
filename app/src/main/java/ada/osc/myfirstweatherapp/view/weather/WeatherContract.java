package ada.osc.myfirstweatherapp.view.weather;

import javax.security.auth.callback.Callback;

import ada.osc.myfirstweatherapp.model.WeatherResponse;

/**
 * Created by avukelic on 24-May-18.
 */
public interface WeatherContract {
    interface View {
        void setCurrentTemperatureValues(double temperatureValues);

        void setMinTemperatureValues(double minTemperatureValues);

        void setMaxTemperatureValues(double maxTemperatureValues);

        void setPressureValues(double pressureValues);

        void setWindValues(double windValues);

        void setWeatherIcon(String iconPath);

        void setDescriptionValues(String descriptionValues);

        void onGetWeatherFailure();
    }

    interface Presenter {

        void setView(WeatherContract.View weatherView);

        void getWeather(String location);
    }
}
