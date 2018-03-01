package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.FeedbackModel;
import in.explicate.fcm.model.NewsLetterModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class FeedbackMagarpattaAdapter extends RecyclerView.Adapter<FeedbackMagarpattaAdapter.MyViewHolder> {

    private List<FeedbackModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView desc;
        public TextView date;


        public MyViewHolder(View view) {
            super(view);

            subject=(TextView) view.findViewById(R.id.subject);
            desc=(TextView) view.findViewById(R.id.department);
            date=(TextView) view.findViewById(R.id.suggDate);

        }
    }

    public FeedbackMagarpattaAdapter(List<FeedbackModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_suggestion_magarpatta, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeedbackModel model = list.get(position);
        holder.subject.setText(model.getSubject());
        holder.desc.setText(model.getDepartment());
        holder.date.setText(model.getFeedbackDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}