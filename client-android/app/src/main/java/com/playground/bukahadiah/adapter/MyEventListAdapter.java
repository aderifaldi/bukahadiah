package com.playground.bukahadiah.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;
import com.radyalabs.irfan.util.AppUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 29/04/2015.
 */
public class MyEventListAdapter extends RecyclerView.Adapter<MyEventListAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BHEvent.EventData> data;
    private SimpleDateFormat sdf;
    private Context context;
    private Point screenSize;

    public MyEventListAdapter(Context context){
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
    public MyEventListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_my_event, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyEventListAdapter.MyViewHolder holder, int position) {
        BHEvent.EventData event = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        if (identity == holder.identity){

            int layoutHeight = screenSize.x / 3;
            ViewGroup.LayoutParams params = holder.imageEvent.getLayoutParams();
            params.height = layoutHeight;
            holder.imageEvent.setLayoutParams(params);

            Glide.with(context)
                    .load(event.event_photo)
                    .crossFade()
                    .into(holder.imageEvent);

        }

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
