package ada.osc.myfirstweatherapp.view.newLocation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.Constants;
import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.model.LocationWrapper;
import ada.osc.myfirstweatherapp.model.WeatherResponse;
import ada.osc.myfirstweatherapp.presentation.NewLocationPresenter;
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
public class AddLocationFragment extends Fragment implements NewLocationContract.View {


    @BindView(R.id.fragment_add_location_enter_city_edit_text)
    EditText mEnterLocationNameEditText;

    private NewLocationContract.Presenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        presenter = new NewLocationPresenter();
        presenter.setView(this);
    }

    @OnClick(R.id.fragment_add_location_button)
    public void addNewLocation(){
        presenter.addNewLocation(mEnterLocationNameEditText.getText().toString());
    }

    @Override
    public void onNewLocationAdded() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.location_added_success_toast_message), Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showOnLocationAlreadyExistsError() {
        mEnterLocationNameEditText.setError(getActivity().getString(R.string.location_already_exists_error_message));
    }

    @Override
    public void showOnLocationDoesNotExistsError() {
        mEnterLocationNameEditText.setError(getActivity().getString(R.string.location_does_not_exists_string_error_message));
    }

    @Override
    public void showOnLocationFieldEmpty() {
        mEnterLocationNameEditText.setError(getActivity().getString(R.string.empty_location_string_error_message));
    }

}
