package ada.osc.myfirstweatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @Expose
    @SerializedName("speed")
    private final double speed;
    @Expose
    @SerializedName("deg")
    private final double deg;

    public Wind(double speed, double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }
}
