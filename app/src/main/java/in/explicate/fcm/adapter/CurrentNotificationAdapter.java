package in.explicate.fcm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.model.NotificationModel;

/**
 * Created by Mahesh on 19/09/17.
 */

public class CurrentNotificationAdapter extends RecyclerView.Adapter<CurrentNotificationAdapter.MyViewHolder> {

    private List<NotificationModel> moviesList;
    private List<NotificationModel> OrgmoviesList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            desc=(TextView) view.findViewById(R.id.desc);
            img=(ImageView) view.findViewById(R.id.imgColor);
        }
    }

    public CurrentNotificationAdapter(List<NotificationModel> moviesList,Context context) {
        this.moviesList = moviesList;
        this.OrgmoviesList =moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notifications, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NotificationModel model = moviesList.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getBody());

        if(model.getPriority().equalsIgnoreCase("L")){

            holder.img.setBackgroundColor(Color.GREEN);

        }else  if(model.getPriority().equalsIgnoreCase("M")){

            holder.img.setBackgroundColor(context.getResources().getColor(R.color.colororange));


        }else if(model.getPriority().equalsIgnoreCase("H")){

            holder.img.setBackgroundColor(Color.RED);

        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ad=new AlertDialog.Builder(context);
                ad.setTitle("Change Priority");
                String []itesm={"LOW","MEDIUM","HIGH"};

                ad.setItems(itesm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                ad.create().show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setFilter(List<NotificationModel> countryModels){
        moviesList = new ArrayList<>();
        moviesList.addAll(countryModels);
        notifyDataSetChanged();
    }

}