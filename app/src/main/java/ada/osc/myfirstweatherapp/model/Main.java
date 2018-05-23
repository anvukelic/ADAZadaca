package ada.osc.myfirstweatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Main {


    @Expose
    @SerializedName("temp")
    private double temp;
    @Expose
    @SerializedName("temp_min")
    private double temp_min;
    @Expose
    @SerializedName("temp_max")
    private double temp_max;
    @Expose
    @SerializedName("humidity")
    private int humidity;
    @Expose
    @SerializedName("pressure")
    private double pressure;


    public Main(double temp, double temp_min, double temp_max, int humidity, double pressure) {
        this.temp = temp;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
