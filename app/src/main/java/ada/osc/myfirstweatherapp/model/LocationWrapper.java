package ada.osc.myfirstweatherapp.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Filip on 10/02/2016.
 */
public class LocationWrapper extends RealmObject {

    @Required
    @PrimaryKey
    private String id;
    @Required
    private String location;

    public LocationWrapper() {
    }

    public LocationWrapper(String location, String id) {
        this.id = id;
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

}
