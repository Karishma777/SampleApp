package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class CounsilMemberApiModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

     @SerializedName("Message")
     public String message;


    public class Data{


        @SerializedName("CouncilMembers")
        @Expose
        public ArrayList<CouncilMembers> result;

    }


    public class CouncilMembers{

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
