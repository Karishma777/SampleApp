package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 11/12/17.
 */

public class MyInfraInfoAPI {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{

        @SerializedName("User Infra")
        @Expose
        public ArrayList<UserInfra> userInfra;

        @SerializedName("Owners")
        @Expose
        public ArrayList<Owners> owners;

        @SerializedName("Occupant")
        @Expose
        public ArrayList<Owners> occupant;

        @SerializedName("InfraList")
        @Expose
        public ArrayList<InfraList> infraList;



    }


    public class InfraList{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Infra")
        @Expose
        public String infra;

    }


    public class Owners{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

    }


    public class UserInfra{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Infra")
        @Expose
        public String infra;

    }


}
