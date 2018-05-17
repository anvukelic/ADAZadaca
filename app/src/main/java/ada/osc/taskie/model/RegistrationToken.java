package ada.osc.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by avukelic on 16-May-18.
 */
public class RegistrationToken {
    @Expose
    @SerializedName("name")
    public String mUserName;
    @Expose
    @SerializedName("email")
    public String mEmail;
    @Expose
    @SerializedName("password")
    public String mPassword;
}
