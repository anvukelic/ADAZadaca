package ada.osc.myfirstweatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {
    @Expose
    @SerializedName("main")
    private String main;
    @Expose
    @SerializedName("description")
    private String description;

    public Weather(String main, String description) {
        this.main = main;
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
