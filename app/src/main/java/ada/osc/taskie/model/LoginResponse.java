package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by avukelic on 16-May-18.
 */
public class LoginResponse {
    @Expose
    @SerializedName("token")
    public String mToken;
}
