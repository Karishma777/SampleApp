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
import in.explicate.fcm.model.EmergencyContact;
import in.explicate.fcm.util.MyUtility;

/**
 * Created by Mahesh on 19/09/17.
 */

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.MyViewHolder> {

    private List<EmergencyContact> contactList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public TextView mobile;
        public TextView desc;
        public ImageView callAction;
        public ImageView saveAction;


        public MyViewHolder(View view) {
            super(view);

            name=(TextView) view.findViewById(R.id.name);
            email=(TextView) view.findViewById(R.id.email);
            mobile=(TextView) view.findViewById(R.id.mobile);
            desc=(TextView) view.findViewById(R.id.desc);
            callAction=(ImageView)view.findViewById(R.id.img_call);
            saveAction=(ImageView)view.findViewById(R.id.img_save);

        }
    }

    public EmergencyContactAdapter(List<EmergencyContact> list, Context context) {
        this.contactList = list;
        this.context = context;
        Log.e("AT","EmergencyContactAdapter");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contact_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final EmergencyContact model = contactList.get(position);

        holder.name.setText(model.getName());
        holder.desc.setText(model.getDescription());
        holder.email.setText(model.getEmail());
        holder.mobile.setText("M:"+model.getMobile());

        holder.callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtility.callIntent(context,model.getMobile());
            }
        });

        holder.saveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtility.saveContactIntent(context,model.getMobile(),model.getName(),model.getEmail());
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}