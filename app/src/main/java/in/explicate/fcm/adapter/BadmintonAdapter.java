package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.BadmintonModel;
import in.explicate.fcm.model.NewsLetterModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class BadmintonAdapter extends RecyclerView.Adapter<BadmintonAdapter.MyViewHolder> {

    private List<BadmintonModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView type;
        public TextView time;


        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.courtName);
            type=(TextView) view.findViewById(R.id.type);
            time=(TextView) view.findViewById(R.id.timeslot);
        }

    }

    public BadmintonAdapter(List<BadmintonModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_badminton, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BadmintonModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.type.setText(model.getType());

        try{

            String [] arr =model.getTimeslot().toString().trim().split(" ");
            holder.time.setText(arr[0]+"\n"+"To"+"\n"+arr[2]);


        }catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}