package in.explicate.fcm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.ClassifiedItemModel;
import in.explicate.fcm.model.ClassifiedResponseModel;
import in.explicate.fcm.model.NewsLetterModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class ClassifiedResponseAdapter extends RecyclerView.Adapter<ClassifiedResponseAdapter.MyViewHolder> {

    private List<ClassifiedItemModel> list;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout llresponse;
        public RelativeLayout llreplies;

        public TextView responseText;
        public TextView responseBy;
        public TextView responsedate;
        public TextView replyText;
        public TextView replyBy;
        public TextView replydate;




        public MyViewHolder(View view) {
            super(view);

            llresponse=(RelativeLayout) view.findViewById(R.id.llresponses);
            llreplies=(RelativeLayout) view.findViewById(R.id.llreplies);

            responseText=(TextView) view.findViewById(R.id.responseText);
            responseBy=(TextView) view.findViewById(R.id.responseBy);
            responsedate=(TextView) view.findViewById(R.id.responsedate);
            replyText=(TextView) view.findViewById(R.id.replyText);
            replyBy=(TextView) view.findViewById(R.id.replyBy);
            replydate=(TextView) view.findViewById(R.id.replydate);


        }
    }

    public ClassifiedResponseAdapter(List<ClassifiedItemModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_responses, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassifiedItemModel model = list.get(position);

        if(model.getType().equalsIgnoreCase("1")){

            holder.llreplies.setVisibility(View.GONE);

            holder.responseText.setText(model.getResponse());
            holder.responseBy.setText("By:"+model.getResponseBy());
            holder.responsedate.setText(model.getDate());

        }else{


            holder.llresponse.setVisibility(View.GONE);

            holder.replyText.setText(model.getResponse());
            holder.replydate.setText("By:"+model.getResponseBy());
            holder.replydate.setText(model.getDate());

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}