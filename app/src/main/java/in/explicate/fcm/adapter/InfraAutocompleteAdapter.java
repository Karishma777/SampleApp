package in.explicate.fcm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.explicate.fcm.R;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.MyInfraInfoAPI;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.MyInfraInfoModel;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 12/12/17.
 */

public class InfraAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<MyInfraInfoModel> mInfraList;
    private String selectedProjectId;
    private Context context;

    public InfraAutocompleteAdapter(Context context, int resource) {
        super(context, resource);
        this.context=context;
        mInfraList = new ArrayList<>();
    }

    public void setSelectdProjectId(String id){

        this.selectedProjectId=id;
    }

    @Override
    public int getCount() {
        return mInfraList.size();
    }

    @Override
    public MyInfraInfoModel getItem(int position) {
        return mInfraList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    try{
                        //get data from the web
                     // if(!mInfraList.isEmpty()){
                      //    mInfraList.clear();
                     // }
                    String term = constraint.toString();

                    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<MyInfraInfoAPI> call3 = apiInterface.getNotMyInfra(term,selectedProjectId, AppClass.getUserId());
                    call3.enqueue(new Callback<MyInfraInfoAPI>() {
                        @Override
                        public void onResponse(Call<MyInfraInfoAPI> call, Response<MyInfraInfoAPI> response) {

                            Log.e("Data:",new Gson().toJson(response.body()));

                            //hideLoader();


                            switch (response.code()){

                                case 200:

                                    if(response.body()!=null){

                                        MyInfraInfoAPI model=response.body();

                                        if(model.status.equalsIgnoreCase("Success")){

                                            if(model.data!=null){

                                                if(model.data.infraList.size() >0){

                                                    ArrayList<MyInfraInfoModel> list=new ArrayList<MyInfraInfoModel>();


                                                    for(int i =0;i<model.data.infraList.size();i++){

                                                        MyInfraInfoModel item=new MyInfraInfoModel();
                                                        item.setId(model.data.infraList.get(i).id+"");
                                                        item.setInfra(model.data.infraList.get(i).infra);
                                                        list.add(item);

                                                       // notifyDataSetChanged();

                                                    }

                                                    if(!list.isEmpty()){

                                                        mInfraList.clear();
                                                        mInfraList.addAll(list);
                                                        notifyDataSetChanged();
                                                    }



                                                }

                                            }
                                            else{

                                                //MyUtility.showAlertMessage(context,model.message);

                                            }


                                        }else{

                                            // MyUtility.showAlertMessage(getActivity(),model.message);
                                        }


                                    }else{

                                        // MyUtility.showAlertMessage(getActivity(),MyUtility.FAILED_TO_GET_DATA);
                                    }


                                    break;


                            }

                        }

                        @Override
                        public void onFailure(Call<MyInfraInfoAPI> call, Throwable t) {
                            call.cancel();
                        }
                    });


                        filterResults.values = mInfraList;
                        filterResults.count = mInfraList.size();


                    }catch (Exception e){
                        Log.d("HUS","EXCEPTION "+e);
                    }




                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row_autocomplete_item,parent,false);

        //get Country
        MyInfraInfoModel contry = mInfraList.get(position);
        TextView countryName = (TextView) view.findViewById(R.id.infraName);
        countryName.setText(contry.getInfra());

        return view;
    }


}