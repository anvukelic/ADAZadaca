package ada.osc.myfirstweatherapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.Constants;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.model.WeatherResponse;
import ada.osc.myfirstweatherapp.network.ApiService;
import ada.osc.myfirstweatherapp.network.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Filip on 26/03/2016.
 */
public class WeatherFragment extends Fragment {

    @BindView(R.id.weather_display_current_temperature_text_view)
    TextView mCurrentTemperature;
    @BindView(R.id.weather_fragment_min_temperature_text_view)
    TextView mMinTemperature;
    @BindView(R.id.weather_fragment_max_temperature_text_view)
    TextView mMaxTemperature;
    @BindView(R.id.weather_display_pressure_text_view)
    TextView mPressure;
    @BindView(R.id.weather_display_wind_text_view)
    TextView mWind;
    @BindView(R.id.weather_display_detailed_description_text_view)
    TextView mDescription;
    @BindView(R.id.weather_display_weather_icon_image_view)
    ImageView mWeatherIcon;

    public static WeatherFragment newInstance(String city) {
        Bundle data = new Bundle();
        data.putString(Constants.CITY_BUNDLE_KEY, city);
        WeatherFragment f = new WeatherFragment();
        f.setArguments(data);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

    }

    @Override
    public void onStart() {
        super.onStart();
        refreshCurrentData();
    }



    public void setCurrentTemperatureValues(double temperatureValues) {
        mCurrentTemperature.setText(getString(R.string.current_temperature_message, temperatureValues));
    }

    public void setMinTemperatureValues(double minTemperatureValues) {
        mMinTemperature.setText(getString(R.string.minimum_temperature_message, minTemperatureValues));
    }

    public void setMaxTemperatureValues(double maxTemperatureValues) {
        mMaxTemperature.setText(getString(R.string.maximum_temperature_message, maxTemperatureValues));
    }

    public void setPressureValues(double pressureValues) {
        mPressure.setText(getString(R.string.pressure_message, pressureValues));

    }

    public void setWindValues(double windValues) {
        mWind.setText(getString(R.string.wind_speed_message, windValues));
    }

    public void setWeatherIcon(String iconPath) {
        Glide.with(getActivity().getApplicationContext()).load(Constants.IMAGE_BASE_URL + iconPath).into(mWeatherIcon);
    }

    public void setDescriptionValues(String descriptionValues) {
        mDescription.setText(descriptionValues);
    }


    public void onFailure() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.weather_fragment_loading_failure_toast_message), Toast.LENGTH_SHORT).show();
    }

    private void refreshCurrentData() {
        if (NetworkUtils.checkIfInternetConnectionIsAvailable(getActivity())) {
            String cityToDisplay = getArguments().getString(Constants.CITY_BUNDLE_KEY);
            App.getApiService().getWeather(Constants.APP_ID, cityToDisplay).enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response != null && response.body() != null) {
                        WeatherResponse data = response.body();
                        setCurrentTemperatureValues(toCelsiusFromKelvin(data.getMain().getTemp()));
                        setPressureValues(data.getMain().getPressure());
                        setDescriptionValues(data.getWeatherObject().getDescription());
                        setWindValues(data.getWind().getSpeed());
                        createWeatherIconValue(data.getWeatherObject().getMain());
                        setMinTemperatureValues(toCelsiusFromKelvin(data.getMain().getTemp_min()));
                        setMaxTemperatureValues(toCelsiusFromKelvin(data.getMain().getTemp_max()));
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    WeatherFragment.this.onFailure();
                }
            });
        }
    }
    private double toCelsiusFromKelvin(double temperature) {
        return temperature - 273;
    }

    private void createWeatherIconValue(String description) {
        if (description != null)
            switch (description) {
                case Constants.SNOW_CASE: {
                    setWeatherIcon(Constants.SNOW);
                    break;
                }
                case Constants.RAIN_CASE: {
                    setWeatherIcon(Constants.RAIN);
                    break;
                }
                case Constants.CLEAR_CASE: {
                    setWeatherIcon(Constants.SUN);
                    break;
                }
                case Constants.MIST_CASE: {
                    setWeatherIcon(Constants.FOG);
                    break;
                }
                case Constants.FOG_CASE: {
                    setWeatherIcon(Constants.FOG);
                    break;
                }
                case Constants.HAZE_CASE: {
                    setWeatherIcon(Constants.FOG);
                    break;
                }

                case Constants.CLOUD_CASE: {
                    setWeatherIcon(Constants.CLOUD);
                    break;
                }
            }
    }
}
