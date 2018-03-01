package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.DeviceModel;
import in.explicate.fcm.model.MyInfraInfoModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class MyDeviceAdapter extends RecyclerView.Adapter<MyDeviceAdapter.MyViewHolder> {

    private List<DeviceModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView type;


        public MyViewHolder(View view) {
            super(view);
            name=(TextView) view.findViewById(R.id.name);
            type=(TextView) view.findViewById(R.id.type);
        }

    }

    public MyDeviceAdapter(List<DeviceModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_infra_info, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DeviceModel model = list.get(position);
        holder.name.setText(model.getDeviceName());
        holder.type.setText(model.getFirstAccess());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}