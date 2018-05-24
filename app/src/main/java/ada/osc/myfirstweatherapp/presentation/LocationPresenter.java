package ada.osc.myfirstweatherapp.presentation;

import android.content.Intent;

import ada.osc.myfirstweatherapp.R;
import ada.osc.myfirstweatherapp.interaction.ApiInteractor;
import ada.osc.myfirstweatherapp.interaction.ApiInteractorImpl;
import ada.osc.myfirstweatherapp.interaction.DbInteractor;
import ada.osc.myfirstweatherapp.interaction.DbInteractorImpl;
import ada.osc.myfirstweatherapp.view.locations.LocationsContract;
import ada.osc.myfirstweatherapp.view.newLocation.AddNewLocationActivity;

/**
 * Created by avukelic on 24-May-18.
 */
public class LocationPresenter implements LocationsContract.Presenter {

    private final DbInteractor dbInteractor;
    private LocationsContract.View locationsView;

    public LocationPresenter() {
        dbInteractor = new DbInteractorImpl();
    }

    @Override
    public void setView(LocationsContract.View locationsView) {
        this.locationsView = locationsView;

    }

    @Override
    public void getLocations() {
        locationsView.showLocations(dbInteractor.getLocations());
    }

    @Override
    public void deleteLocation(String location) {
        dbInteractor.deleteLocation(location);
        locationsView.onLocationRemove();
    }

    @Override
    public void onDrawerItemClicked(int itemId) {
        switch (itemId) {
            case R.id.menu_add_new_location: {
                locationsView.onNewLocationDrawerItemClicked();
                break;
            }
            default:
                locationsView.onLocationDrawerItemClicked(itemId);
        }
    }
}
