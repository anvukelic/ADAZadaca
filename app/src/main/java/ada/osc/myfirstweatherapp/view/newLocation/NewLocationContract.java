package ada.osc.myfirstweatherapp.view.newLocation;

import ada.osc.myfirstweatherapp.model.LocationWrapper;

/**
 * Created by avukelic on 24-May-18.
 */
public interface NewLocationContract {

    interface View{
        void onNewLocationAdded();

        void showOnLocationAlreadyExistsError();

        void showOnLocationDoesNotExistsError();

    }

    interface Presenter{

        void setView(NewLocationContract.View newLocationView);

        void addNewLocation(String location);

        boolean isLocationAlreadyOnList(String location);

    }

}
