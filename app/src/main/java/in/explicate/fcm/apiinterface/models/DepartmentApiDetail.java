package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 04/12/17.
 */

public class DepartmentApiDetail {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

     @SerializedName("Message")
     public String message;


    public class Data{

        @SerializedName("Departments")
        @Expose
        public ArrayList<Departments> result;

    }


    public class Departments{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

    }
}
