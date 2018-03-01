package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.ClassifiedModel;

/**
 * Created by Mahesh on 14/12/17.
 */

public class ClassifiedCategoryAdapter extends RecyclerView.Adapter<ClassifiedCategoryAdapter.MyViewHolder> {

    private List<ClassifiedModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView desc;
        public TextView count;

        public MyViewHolder(View view) {
            super(view);
            subject = (TextView) view.findViewById(R.id.tvcategoryName);
            count = (TextView) view.findViewById(R.id.tvResponseCount);
            desc=(TextView)view.findViewById(R.id.tvDescription);
        }
    }

    public ClassifiedCategoryAdapter(List<ClassifiedModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_classified_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassifiedModel model = list.get(position);
        holder.subject.setText(model.getSubject());
        holder.count.setText("Responses :"+model.getResponseCount());
        holder.desc.setText(model.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}