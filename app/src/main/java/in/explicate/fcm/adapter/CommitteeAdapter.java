package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.apimodels.CommitteeModel;
import in.explicate.fcm.model.EmergencyContact;
import in.explicate.fcm.util.RoundedImageView;

/**
 * Created by Mahesh on 19/09/17.
 */

public class CommitteeAdapter extends RecyclerView.Adapter<CommitteeAdapter.MyViewHolder> {

    private List<CommitteeModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView desc;
        public TextView email;


        public MyViewHolder(View view) {
            super(view);

            name=(TextView) view.findViewById(R.id.tvName);
            desc=(TextView) view.findViewById(R.id.tvDsc);
            email=(TextView) view.findViewById(R.id.tvEmail);
        }
    }

    public CommitteeAdapter(List<CommitteeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_committees, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CommitteeModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.desc.setText(model.getDesc());
        holder.email.setText("Email:"+model.getEmail());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}