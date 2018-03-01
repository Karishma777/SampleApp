package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.NewsLetterModel;
import in.explicate.fcm.model.StandardProcModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class StandardProcAdapter extends RecyclerView.Adapter<StandardProcAdapter.MyViewHolder> {

    private List<StandardProcModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;


        public MyViewHolder(View view) {
            super(view);

            subject=(TextView) view.findViewById(R.id.subject);
        }
    }

    public StandardProcAdapter(List<StandardProcModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_standard_proc, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final StandardProcModel model = list.get(position);
        holder.subject.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}