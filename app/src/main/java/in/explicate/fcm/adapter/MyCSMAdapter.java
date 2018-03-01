package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.ComplaintModel;
import in.explicate.fcm.model.MyInfraInfoModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class MyCSMAdapter extends RecyclerView.Adapter<MyCSMAdapter.MyViewHolder> {

    private List<ComplaintModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView type;
        public TextView status;



        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.complaintNo);
            type=(TextView) view.findViewById(R.id.complaintType);
            status=(TextView) view.findViewById(R.id.status);

        }

    }

    public MyCSMAdapter(List<ComplaintModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_complaint_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ComplaintModel model = list.get(position);
        holder.name.setText(model.getComplaintNo());
        holder.type.setText(model.getComplaintType());
        holder.status.setText(model.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}