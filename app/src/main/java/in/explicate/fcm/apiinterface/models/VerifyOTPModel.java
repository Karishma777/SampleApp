package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Explicate1 on 12/18/2017.
 */

public class VerifyOTPModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("message")
    public String message;
    @SerializedName("otp")
    public String otp;
    @SerializedName("mobile")
    public String mobile;


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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
