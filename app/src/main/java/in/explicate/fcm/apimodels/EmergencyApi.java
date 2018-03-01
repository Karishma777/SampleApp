package in.explicate.fcm.apimodels;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Mahesh on 19/11/17.
 */

public class EmergencyApi {

    @SerializedName("Status")
    public String status;
    @SerializedName("Data")
    public JSONObject data;
}
