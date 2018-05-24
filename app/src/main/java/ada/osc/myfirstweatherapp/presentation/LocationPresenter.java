package ada.osc.myfirstweatherapp.presentation;

import ada.osc.myfirstweatherapp.interaction.ApiInteractor;
import ada.osc.myfirstweatherapp.interaction.DbInteractor;
import ada.osc.myfirstweatherapp.view.locations.LocationsContract;

/**
 * Created by avukelic on 24-May-18.
 */
public class LocationPresenter implements LocationsContract.Presenter {

    private ApiInteractor apiInteractor;
    private DbInteractor dbInteractor;
    private LocationsContract.View locationsView;

    public LocationPresenter(ApiInteractor apiInteractor, DbInteractor dbInteractor) {
        this.apiInteractor = apiInteractor;
        this.dbInteractor = dbInteractor;
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
}
