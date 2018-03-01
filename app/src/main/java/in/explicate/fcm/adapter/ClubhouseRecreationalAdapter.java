package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.ClassModel;
import in.explicate.fcm.model.EmergencyContact;
import in.explicate.fcm.util.MyUtility;

/**
 * Created by Mahesh on 19/09/17.
 */

public class ClubhouseRecreationalAdapter extends RecyclerView.Adapter<ClubhouseRecreationalAdapter.MyViewHolder> {

    private List<ClassModel> contactList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEvent;
        public TextView tvBookingDate;


        public MyViewHolder(View view) {
            super(view);

            tvName=(TextView) view.findViewById(R.id.tvName);
            tvEvent=(TextView) view.findViewById(R.id.tvEvent);
            tvBookingDate=(TextView) view.findViewById(R.id.tvBookingDate);

        }
    }

    public ClubhouseRecreationalAdapter(List<ClassModel> list, Context context) {
        this.contactList = list;
        this.context = context;
        Log.e("AT","EmergencyContactAdapter");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_classes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassModel model = contactList.get(position);

        holder.tvName.setText(model.getName());
        holder.tvBookingDate.setText(model.getBookingDate());
        holder.tvEvent.setText(model.getEvent());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}