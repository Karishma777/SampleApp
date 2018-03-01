package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 28/11/17.
 */

public class CommitteeMembarApi {

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


        @SerializedName("CommitteeMembers")
        @Expose
        public ArrayList<CommitteeMembers> result;

    }


    public class CommitteeMembers{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

        @SerializedName("Infra")
        @Expose
        public String infra;

    }


}
