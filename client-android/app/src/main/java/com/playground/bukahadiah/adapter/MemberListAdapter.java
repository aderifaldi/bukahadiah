package com.playground.bukahadiah.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.imageview.CircleImageView;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukahadiah.BHMemberList;
import com.radyalabs.irfan.util.AppUtility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 29/04/2015.
 */
public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BHMemberList.MemberData> data;
    private SimpleDateFormat sdf;
    private Context context;
    private Point screenSize;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    public MemberListAdapter(Context context){
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        data = new ArrayList<>();
        this.context = context;
        screenSize = AppUtility.getScreenSize(context);
        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("###,###,###,###", symbols);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<BHMemberList.MemberData> getData() {
        return data;
    }

    @Override
    public MemberListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_member, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MemberListAdapter.MyViewHolder holder, int position) {
        BHMemberList.MemberData member = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        if (identity == holder.identity){

            Glide.with(context)
                    .load(member.user_photo)
                    .crossFade()
                    .into(holder.userImage);

            holder.name.setText(member.user_name);
            holder.username.setText(member.user_screename);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        long identity;

        @BindView(R.id.userImage) CircleImageView userImage;
        @BindView(R.id.name) CustomTextView name;
        @BindView(R.id.username) CustomTextView username;

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
