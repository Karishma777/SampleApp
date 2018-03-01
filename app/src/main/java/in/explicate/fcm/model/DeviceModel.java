package in.explicate.fcm.model;

/**
 * Created by Mahesh on 11/12/17.
 */

public class DeviceModel {

    private String id;
    private String deviceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFirstAccess() {
        return firstAccess;
    }

    public void setFirstAccess(String firstAccess) {
        this.firstAccess = firstAccess;
    }

    private String firstAccess;
}
