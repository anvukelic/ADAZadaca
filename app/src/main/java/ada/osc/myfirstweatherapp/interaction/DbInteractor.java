package ada.osc.myfirstweatherapp.interaction;

import java.util.List;

import ada.osc.myfirstweatherapp.model.LocationWrapper;

/**
 * Created by avukelic on 24-May-18.
 */
public interface DbInteractor {
    void saveLocation(String location);
    boolean isLocationAlreadyOnList(String location);
    List<LocationWrapper> getLocations();
    void deleteLocation(String location);
}
