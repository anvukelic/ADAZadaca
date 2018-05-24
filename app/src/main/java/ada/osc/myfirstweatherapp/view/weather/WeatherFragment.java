package ada.osc.myfirstweatherapp.view.weather;

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
import ada.osc.myfirstweatherapp.network.NetworkUtils;
import ada.osc.myfirstweatherapp.presentation.WeatherPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Filip on 26/03/2016.
 */
public class WeatherFragment extends Fragment implements WeatherContract.View {

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

    private WeatherPresenter presenter;

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
        presenter = new WeatherPresenter();
        presenter.setView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshCurrentData();
    }


    private void refreshCurrentData() {
        if (NetworkUtils.checkIfInternetConnectionIsAvailable(getActivity())) {
            String location = getArguments().getString(Constants.CITY_BUNDLE_KEY);
            presenter.getWeather(location);
        }
    }

    @Override
    public void setCurrentTemperatureValues(double temperatureValues) {
        mCurrentTemperature.setText(getString(R.string.current_temperature_message, temperatureValues));
    }

    @Override
    public void setMinTemperatureValues(double minTemperatureValues) {
        mMinTemperature.setText(getString(R.string.minimum_temperature_message, minTemperatureValues));
    }

    @Override
    public void setMaxTemperatureValues(double maxTemperatureValues) {
        mMaxTemperature.setText(getString(R.string.maximum_temperature_message, maxTemperatureValues));
    }

    @Override
    public void setPressureValues(double pressureValues) {
        mPressure.setText(getString(R.string.pressure_message, pressureValues));

    }

    @Override
    public void setWindValues(double windValues) {
        mWind.setText(getString(R.string.wind_speed_message, windValues));
    }

    @Override
    public void setWeatherIcon(String iconPath) {
        Glide.with(getActivity().getApplicationContext()).load(Constants.IMAGE_BASE_URL + iconPath).into(mWeatherIcon);
    }

    @Override
    public void setDescriptionValues(String descriptionValues) {
        mDescription.setText(descriptionValues);
    }

    @Override
    public void onGetWeatherFailure() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.weather_fragment_loading_failure_toast_message), Toast.LENGTH_SHORT).show();
    }
}
