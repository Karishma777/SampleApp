package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.apimodels.CommitteeModel;
import in.explicate.fcm.model.CommitteeMemberModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class CommitteeMemberAdapter extends RecyclerView.Adapter<CommitteeMemberAdapter.MyViewHolder> {

    private List<CommitteeMemberModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView infra;


        public MyViewHolder(View view) {
            super(view);

            name=(TextView) view.findViewById(R.id.tvName);
            infra=(TextView) view.findViewById(R.id.tvInfra);
        }
    }

    public CommitteeMemberAdapter(List<CommitteeMemberModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_committees_member, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CommitteeMemberModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.infra.setText(model.getInfra());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}