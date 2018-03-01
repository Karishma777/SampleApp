package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class ClassesAPIModel {

    @SerializedName("Status")
    @Expose
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    @Expose
    public String message;

    public class Data{

        @SerializedName("Classes")
        @Expose
        public ArrayList<Classes> classes;

    }


    public class Classes{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

        @SerializedName("Event")
        @Expose
        public String event;

        @SerializedName("BookingDate")
        @Expose
        public String bookingDate;

    }
}
