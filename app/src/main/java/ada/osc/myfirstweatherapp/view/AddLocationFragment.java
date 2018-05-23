package ada.osc.myfirstweatherapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.Constants;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.model.LocationWrapper;
import ada.osc.myfirstweatherapp.model.WeatherResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Filip on 10/02/2016.
 */
public class AddLocationFragment extends Fragment {



    @BindView(R.id.fragment_add_location_enter_city_edit_text)
    EditText mEnterLocationNameEditText;

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onSuccess() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.location_added_success_toast_message), Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    public void onLocationAlreadyExistsError() {
        mEnterLocationNameEditText.setError(getActivity().getString(R.string.location_already_exists_error_message));
    }

    public void onEmptyStringRequestError() {
        mEnterLocationNameEditText.setError(getActivity().getString(R.string.empty_location_string_error_message));
    }

    public void onLocationDoesntExists() {
        mEnterLocationNameEditText.setError(getActivity().getString(R.string.location_does_not_exists_string_error_message));
    }

    @OnClick(R.id.fragment_add_location_button)
    public void addNewLocation(){
        final String location = mEnterLocationNameEditText.getText().toString();
        if(location.isEmpty() || location.trim().length() ==0){
            onEmptyStringRequestError();
        } else if(isLocationAlreadyExists(location)){
            onLocationAlreadyExistsError();
        } else {
            App.getApiService().getWeather(Constants.APP_ID, location).enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if(response.isSuccessful()){
                        saveLocation(location);
                        onSuccess();
                    } else {
                        onLocationDoesntExists();
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {

                }
            });
        }
    }

    private void saveLocation(String location) {
        realm.beginTransaction();
        LocationWrapper locationObject = realm.createObject(LocationWrapper.class, UUID.randomUUID().toString());
        //Set location with first letter capitilze
        locationObject.setLocation(location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase());
        realm.commitTransaction();
    }

    private boolean isLocationAlreadyExists(String location) {
        location = location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase();
        return realm.where(LocationWrapper.class).equalTo("location", location).findFirst() != null;
    }
}
