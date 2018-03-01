package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Explicate1 on 12/18/2017.
 */

public class RegistrationModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Message")
    public String message;

    @SerializedName("userName")
    public String userName;
    @SerializedName("password")
    public String password;

    @SerializedName("selectedInfraID")
    public String selectedInfraID;
    @SerializedName("citizenID")
    public String citizenID;

    @SerializedName("email")
    public String email;

    @SerializedName("mobile")
    public String mobile;
    @SerializedName("osflag")
    public String osflag;

    @SerializedName("fcmID")
    public String fcmID;


    @SerializedName("deviceID")
    public String deviceID;

    @SerializedName("deviceName")
    public String deviceName;

    @SerializedName("name")
    public String name;


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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelectedInfraID() {
        return selectedInfraID;
    }

    public void setSelectedInfraID(String selectedInfraID) {
        this.selectedInfraID = selectedInfraID;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOsflag() {
        return osflag;
    }

    public void setOsflag(String osflag) {
        this.osflag = osflag;
    }

    public String getFcmID() {
        return fcmID;
    }

    public void setFcmID(String fcmID) {
        this.fcmID = fcmID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



