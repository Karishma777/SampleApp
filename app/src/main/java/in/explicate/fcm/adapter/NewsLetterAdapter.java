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
import in.explicate.fcm.model.NewsLetterModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class NewsLetterAdapter extends RecyclerView.Adapter<NewsLetterAdapter.MyViewHolder> {

    private List<NewsLetterModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView desc;


        public MyViewHolder(View view) {
            super(view);

            subject=(TextView) view.findViewById(R.id.subject);
            desc=(TextView) view.findViewById(R.id.desc);
        }
    }

    public NewsLetterAdapter(List<NewsLetterModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news_letter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NewsLetterModel model = list.get(position);
        holder.subject.setText(model.getSubject());
        holder.desc.setText(model.getDesc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}