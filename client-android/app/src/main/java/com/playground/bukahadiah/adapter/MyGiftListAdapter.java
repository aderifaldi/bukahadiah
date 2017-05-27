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
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukahadiah.BHGiftItem;
import com.radyalabs.irfan.util.AppUtility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 29/04/2015.
 */
public class MyGiftListAdapter extends RecyclerView.Adapter<MyGiftListAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BHGiftItem.GiftItemData> data;
    private Context context;
    private Point screenSize;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    public MyGiftListAdapter(Context context){
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

    public ArrayList<BHGiftItem.GiftItemData> getData() {
        return data;
    }

    @Override
    public MyGiftListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_my_gift, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyGiftListAdapter.MyViewHolder holder, int position) {
        BHGiftItem.GiftItemData gift = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        if (identity == holder.identity){

            Glide.with(context)
                    .load(gift.item_pic)
                    .crossFade()
                    .into(holder.giftImage);

            holder.productName.setText(gift.item_name);
            holder.productDesc.setText(gift.item_desc);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        long identity;

        @BindView(R.id.giftImage) ImageView giftImage;
        @BindView(R.id.productName) CustomTextView productName;
        @BindView(R.id.productDesc) CustomTextView productDesc;

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
