package com.playground.bukahadiah.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.RatingCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProduct;
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
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BLProduct.Product> data;
    private SimpleDateFormat sdf;
    private Context context;
    private Point screenSize;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    public ProductListAdapter(Context context){
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

    public ArrayList<BLProduct.Product> getData() {
        return data;
    }

    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_product, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductListAdapter.MyViewHolder holder, int position) {
        BLProduct.Product product = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        if (identity == holder.identity){

            int layoutHeight = screenSize.x / 2;
            ViewGroup.LayoutParams params = holder.imageProduct.getLayoutParams();
            params.height = layoutHeight;
            holder.imageProduct.setLayoutParams(params);

            if (product.images.length > 0){
                Glide.with(context)
                        .load(product.images[0])
                        .crossFade()
                        .into(holder.imageProduct);
            }

            holder.productName.setText(product.name);
            holder.productPrice.setText("Rp. " + decimalFormat.format(product.price));

            if (product.rating.user_count != 0){
                holder.ratting.setText("Rating: " + ("" + product.rating.average_rate));
                holder.userCount.setText("  (" + ("" + product.rating.user_count) + ")");
            }else {
                holder.ratting.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryFont));

                holder.ratting.setText("(No rating)" );
                holder.userCount.setText("");
            }

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        long identity;

        @BindView(R.id.imageProduct) ImageView imageProduct;
        @BindView(R.id.productName) CustomTextView productName;
        @BindView(R.id.productPrice) CustomTextView productPrice;
        @BindView(R.id.containerItem) RelativeLayout containerItem;
        @BindView(R.id.containerRating) LinearLayout containerRating;
        @BindView(R.id.ratting) CustomTextView ratting;
        @BindView(R.id.userCount) CustomTextView userCount;

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
