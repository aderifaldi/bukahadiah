package com.playground.bukahadiah.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProductCategory;
import com.playground.bukahadiah.model.bukalapak.BLProductReview;
import com.radyalabs.irfan.util.AppUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 29/04/2015.
 */
public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BLProductReview.Review> data;
    private SimpleDateFormat sdf;
    private Context context;
    private Point screenSize;

    public ProductReviewAdapter(Context context){
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        data = new ArrayList<>();
        this.context = context;
        screenSize = AppUtility.getScreenSize(context);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<BLProductReview.Review> getData() {
        return data;
    }

    @Override
    public ProductReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_product_review, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductReviewAdapter.MyViewHolder holder, int position) {
        BLProductReview.Review review = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        holder.user.setText(review.sender_name);
        holder.date.setText(sdf.format(review.updated_at));
        holder.message.setText(review.body);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        long identity;

        @BindView(R.id.user)
        CustomTextView user;
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.message)
        CustomTextView message;

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
