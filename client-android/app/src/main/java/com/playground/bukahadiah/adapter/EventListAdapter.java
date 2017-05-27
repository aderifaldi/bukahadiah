package com.playground.bukahadiah.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.activity.MainActivity;
import com.playground.bukahadiah.activity.UserProfileActivity;
import com.playground.bukahadiah.customui.imageview.CircleImageView;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;
import com.radyalabs.irfan.util.AppUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 29/04/2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BHEvent.EventData> data;
    private SimpleDateFormat sdf;
    private Context context;
    private Point screenSize;

    public EventListAdapter(Context context){
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        data = new ArrayList<>();
        this.context = context;
        screenSize = AppUtility.getScreenSize(context);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<BHEvent.EventData> getData() {
        return data;
    }

    @Override
    public EventListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_event, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final EventListAdapter.MyViewHolder holder, int position) {
        final BHEvent.EventData event = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        if (identity == holder.identity){
            Glide.with(context)
                    .load(event.event_photo)
                    .crossFade()
                    .into(holder.imageEvent);

            Glide.with(context)
                    .load(event.user_photo)
                    .crossFade()
                    .into(holder.imageUser);

            holder.eventName.setText(event.event_name);
            holder.eventDesc.setText(event.event_description);

            if (event.created_date != null){
                holder.eventDate.setText(sdf.format(event.created_date));
            }

        }

        holder.imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalVariable.getUserId(context) != Integer.parseInt(event.user_blid)){
                    context.startActivity(new Intent(context, UserProfileActivity.class)
                            .putExtra("userId", event.user_blid));
                }else {
                    ((MainActivity)context).goToProfile();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        long identity;

        @BindView(R.id.imageEvent)
        ImageView imageEvent;
        @BindView(R.id.eventName)
        CustomTextView eventName;
        @BindView(R.id.eventDate)
        CustomTextView eventDate;
        @BindView(R.id.eventDesc)
        CustomTextView eventDesc;
        @BindView(R.id.imageUser)
        CircleImageView imageUser;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(null, v, position, 0);
            }
        }
    }
}
