package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.apiinterface.models.CategoryApiModel;
import in.explicate.fcm.model.ClassifiedModel;
import in.explicate.fcm.model.NewsLetterModel;

/**
 * Created by Mahesh on 14/12/17.
 */

public class ClassifiedCategoryCountAdapter  extends RecyclerView.Adapter<ClassifiedCategoryCountAdapter.MyViewHolder> {

    private List<ClassifiedModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView count;


        public MyViewHolder(View view) {
            super(view);

            subject = (TextView) view.findViewById(R.id.tvcategoryName);
            count = (TextView) view.findViewById(R.id.tvcount);
        }
    }

    public ClassifiedCategoryCountAdapter(List<ClassifiedModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_classified_count, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassifiedModel model = list.get(position);
        holder.subject.setText(model.getCategoryName());
        holder.count.setText(model.getCount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}