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
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceList;
import com.radyalabs.irfan.util.AppUtility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aderifaldi on 29/04/2015.
 */
public class MyInvoiceListAdapter extends RecyclerView.Adapter<MyInvoiceListAdapter.MyViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    final private ArrayList<BHInvoiceList.InvoiceListData> data;
    private Context context;
    private Point screenSize;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    public MyInvoiceListAdapter(Context context){
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

    public ArrayList<BHInvoiceList.InvoiceListData> getData() {
        return data;
    }

    @Override
    public MyInvoiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_my_invoice, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyInvoiceListAdapter.MyViewHolder holder, int position) {
        BHInvoiceList.InvoiceListData invoice = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        if (identity == holder.identity){

            Glide.with(context)
                    .load(invoice.item_photo)
                    .crossFade()
                    .into(holder.producImage);

            holder.invoiceCode.setText(invoice.invoice_id);
            holder.productName.setText(invoice.item_name);
            holder.amount.setText("Rp. " + decimalFormat.format(invoice.total_amount));
            holder.status.setText(invoice.status);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int position;
        long identity;

        @BindView(R.id.producImage) ImageView producImage;
        @BindView(R.id.invoiceCode) CustomTextView invoiceCode;
        @BindView(R.id.productName) CustomTextView productName;
        @BindView(R.id.amount) CustomTextView amount;
        @BindView(R.id.status) CustomTextView status;

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
