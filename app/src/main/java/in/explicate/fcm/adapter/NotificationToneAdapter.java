package in.explicate.fcm.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.NewsLetterModel;
import in.explicate.fcm.model.ToneModel;

/**
 * Created by Mahesh on 09/12/17.
 */

public class NotificationToneAdapter extends RecyclerView.Adapter<NotificationToneAdapter.MyViewHolder> {

    private List<ToneModel> list;
    private Context context;
    private int lastCheckedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView play;
        public RadioButton radioButton;


        public MyViewHolder(View view) {
            super(view);

            title=(TextView) view.findViewById(R.id.tvTitle);
            play=(ImageView) view.findViewById(R.id.imgPlay);
            radioButton=(RadioButton) view.findViewById(R.id.radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                     notifyDataSetChanged();
                }
            });
        }
    }

    public NotificationToneAdapter(List<ToneModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ringtone, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ToneModel model = list.get(position);
        holder.title.setText(model.getTitle());

        if(AppClass.getToneName().equalsIgnoreCase(model.getTitle())){

            holder.radioButton.setChecked(true);

        }else{

            holder.radioButton.setChecked(position == lastCheckedPosition);

        }


        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mp = MediaPlayer.create(context, model.getUrl());
                mp.start();
            }
        });


    }

    public int getPosition(){
        return  lastCheckedPosition;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
