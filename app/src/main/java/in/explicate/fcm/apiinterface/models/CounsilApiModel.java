package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class CounsilApiModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

     @SerializedName("Message")
     public String message;


    public class Data{


        @SerializedName("Councils")
        @Expose
        public ArrayList<Councils> result;

    }


    public class Councils{


        @SerializedName("ID")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

        @SerializedName("Description")
        @Expose
        public String description;

        @SerializedName("Email")
        @Expose
        public String email;
    }


}
