package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 11/12/17.
 */

public class DeviceAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("MyDevicesList")
        @Expose
        public ArrayList<MyDevicesList> result;

    }


    public class MyDevicesList{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("DeviceName")
        @Expose
        public String name;

        @SerializedName("FirstAccess")
        @Expose
        public String firstAccess;
    }

}
