package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 11/12/17.
 */

public class ClubHouseAPIModel  {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("ClubHousesList")
        @Expose
        public ArrayList<ClubHousesList> result;

    }


    public class ClubHousesList{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Infra")
        @Expose
        public String infra;

    }


}
