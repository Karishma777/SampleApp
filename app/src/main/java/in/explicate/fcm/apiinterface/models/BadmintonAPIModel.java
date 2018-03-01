package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class BadmintonAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("AvailableTimeSlotsList")
        @Expose
        public ArrayList<AvailableTimeSlotsList> result;

    }


    public class AvailableTimeSlotsList{


        @SerializedName("Date")
        @Expose
        public String date;

        @SerializedName("CourtName")
         @Expose
        public String courtName;

        @SerializedName("TimeSlot")
        @Expose
        public String timeSlot;

        @SerializedName("Type")
        @Expose
        public String type;
    }


}
