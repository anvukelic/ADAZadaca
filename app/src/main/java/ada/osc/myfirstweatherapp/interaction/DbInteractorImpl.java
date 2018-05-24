package ada.osc.myfirstweatherapp.interaction;

import java.util.List;

import ada.osc.myfirstweatherapp.App;
import ada.osc.myfirstweatherapp.model.LocationWrapper;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by avukelic on 24-May-18.
 */
public class DbInteractorImpl implements DbInteractor {

    public DbInteractorImpl() {
    }

    @Override
    public void saveLocation(final String location) {
        App.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(LocationWrapper.class, location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase());
            }
        });
    }

    @Override
    public boolean isLocationAlreadyOnList(String location) {
        location = location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase();
        return App.getRealm().where(LocationWrapper.class).equalTo("location", location).findFirst() != null;
    }

    @Override
    public List<LocationWrapper> getLocations() {
        return App.getRealm().where(LocationWrapper.class).findAll();
    }

    @Override
    public void deleteLocation(final String location) {
        App.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LocationWrapper> result = realm.where(LocationWrapper.class)
                        .equalTo("location", location)
                        .findAll();
                result.deleteAllFromRealm();
            }
        });
    }


}
