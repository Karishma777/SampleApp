package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Explicate1 on 12/18/2017.
 */

public class ChangePasswordModel {
    @SerializedName("Status")
    @Expose
    public String status;

    @SerializedName("Message")
    @Expose
    public String message;

    @SerializedName("loggedinUserID")
    public String loggedinUserID;


    @SerializedName("oldPassword")
    public String oldPassword;

    @SerializedName("newPassword")
    public String newPassword;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLoggedinUserID() {
        return loggedinUserID;
    }

    public void setLoggedinUserID(String loggedinUserID) {
        this.loggedinUserID = loggedinUserID;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
