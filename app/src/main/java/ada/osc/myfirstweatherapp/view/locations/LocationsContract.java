package ada.osc.myfirstweatherapp.view.locations;

import java.util.List;

import ada.osc.myfirstweatherapp.model.LocationWrapper;

/**
 * Created by avukelic on 24-May-18.
 */
public interface LocationsContract {

    interface View {
        void showLocations(List<LocationWrapper> locations);

        void onLocationRemove();

        void onNewLocationDrawerItemClicked();

        void onLocationDrawerItemClicked(int itemId);
    }

    interface Presenter {
        void setView(LocationsContract.View locationsView);

        void getLocations();

        void deleteLocation(String location);

        void onDrawerItemClicked(int itemId);
    }
}
