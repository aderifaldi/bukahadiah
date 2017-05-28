package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.Notification;
import com.playground.bukahadiah.model.bukahadiah.BHInvoice;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceDetail;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceList;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;
import com.playground.bukahadiah.model.bukalapak.BLProductDetail;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInvoiceDetailActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.idTagihan)
    CustomTextView idTagihan;
    @BindView(R.id.userName)
    CustomTextView userName;
    @BindView(R.id.dueDate)
    CustomTextView dueDate;
    @BindView(R.id.totalAmount)
    CustomTextView totalAmount;
    @BindView(R.id.transactionDate)
    CustomTextView transactionDate;
    @BindView(R.id.buyer)
    CustomTextView buyer;
    @BindView(R.id.sellerName)
    CustomTextView sellerName;
    @BindView(R.id.productName)
    CustomTextView productName;
    @BindView(R.id.amountTotal)
    CustomTextView amountTotal;
    @BindView(R.id.amoundCode)
    CustomTextView amoundCode;
    @BindView(R.id.totalPayment)
    CustomTextView totalPayment;
    @BindView(R.id.shippingAddress)
    CustomTextView shippingAddress;
    @BindView(R.id.status) CustomTextView status;

    private BHInvoiceList.InvoiceListData invoiceList;
    private SimpleDateFormat sdf;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;
    private boolean canConfirm;
    private BHInvoiceDetail invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invoice_detail);
        ButterKnife.bind(this);

        pageTitle.setText("My Invoice");

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("###,###,###,###", symbols);

        sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");

        invoiceList = (BHInvoiceList.InvoiceListData) getIntent().getExtras().getSerializable("invoice");
        GetInvoiceDetail();

    }

    private void GetInvoiceDetail(){
        showLoading();
        Call<BHInvoiceDetail> call = apiServiceBH.GetInvoiceDetail(invoiceList.invoice_id,
                GlobalVariable.getUserId(getApplicationContext()),
                GlobalVariable.getBukalapakToken(getApplicationContext()));

        call.enqueue(new Callback<BHInvoiceDetail>() {
            @Override
            public void onResponse(Call<BHInvoiceDetail> call, Response<BHInvoiceDetail> response) {
                dismissLoading();

                invoice = response.body();
                if (!response.body().isError()){
                    if (invoice != null){

                        if (invoice.data.invoice.state.equals("expired")){
                            canConfirm = false;
                            status.setText("EXPIRED");
                        }else if (invoice.data.invoice.state.equals("payment_chosen")){
                            canConfirm = false;
                            status.setText("WAITING PAYMENT");
                        }else {
                            canConfirm = true;
                            status.setText("SEND CONFIRMATION");
                        }

                        idTagihan.setText("Payment Charge " + invoice.data.invoice.invoice_id);
                        userName.setText("Hi " + invoice.data.invoice.buyer.name + ",");
                        dueDate.setText(sdf.format(invoice.data.invoice.pay_before));
                        totalAmount.setText("Rp. " + decimalFormat.format(invoice.data.invoice.coded_amount));
                        transactionDate.setText(sdf.format(invoice.data.invoice.created_at));
                        buyer.setText(invoice.data.invoice.buyer.name);
                        sellerName.setText(invoice.data.invoice.transactions[0].seller.name);
                        productName.setText(invoice.data.invoice.transactions[0].products[0].name);
                        amountTotal.setText("Rp. " + decimalFormat.format(invoice.data.invoice.total_amount));
                        amoundCode.setText("" + invoice.data.invoice.uniq_code);
                        totalPayment.setText("Rp. " + decimalFormat.format(invoice.data.invoice.coded_amount));
                        shippingAddress.setText(invoice.data.invoice.consignee.name + "\n" +
                                invoice.data.invoice.consignee.address + "\n" +
                                invoice.data.invoice.consignee.area + ", " + invoice.data.invoice.consignee.city
                                + "\n" + invoice.data.invoice.consignee.province + " - " +
                                invoice.data.invoice.consignee.post_code + "\n" + invoice.data.invoice.consignee.phone);
                    }
                }
            }

            @Override
            public void onFailure(Call<BHInvoiceDetail> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.status)
    protected void status(){
        if (canConfirm){
            showLoading();

            SimpleDateFormat sdf = new SimpleDateFormat(GlobalVariable.DATE_FORMAT);
            Calendar calendar = Calendar.getInstance();

            jsonPost = new JsonObject();
            jsonPost.addProperty("gift_item_id", invoiceList.item_id);
            jsonPost.addProperty("user_id", GlobalVariable.getUserId(getApplicationContext()));
            jsonPost.addProperty("friend_id", GlobalVariable.getTempFriendId(getApplicationContext()));
            jsonPost.addProperty("title", GlobalVariable.getNameUser(getApplicationContext()));
            jsonPost.addProperty("message", "has confirmed to gift you " + invoice.data.invoice.transactions[0].products[0].name);
            jsonPost.addProperty("create_date", sdf.format(calendar.getTime()));

            Call<ModelBase> call = apiServiceBH.AddNotification(jsonPost);
            call.enqueue(new Callback<ModelBase>() {
                @Override
                public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
                    if (!response.body().isError()){
                        dismissLoading();
                        sendNotification();
                    }
                }

                @Override
                public void onFailure(Call<ModelBase> call, Throwable t) {

                }
            });
        }
    }

    private void sendNotification(){
        jsonPost = new JsonObject();

        JsonObject notification = new JsonObject();
        notification.addProperty("title", GlobalVariable.getNameUser(getApplicationContext()));
        notification.addProperty("message", "has confirmed to gift you " + invoice.data.invoice.transactions[0].products[0].name);

        jsonPost.add("data", notification);
        jsonPost.addProperty("to", GlobalVariable.getTempFriendFCMToken(getApplicationContext()));
        jsonPost.addProperty("priority", "High");

        Call<Notification> call = apiServiceNotification.sendNotification(jsonPost);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {

                Notification apiResponse = response.body();

                if (apiResponse.success == 1){
                    Toast.makeText(MyInvoiceDetailActivity.this, "Confirmation sent!", Toast.LENGTH_SHORT).show();
                    Log.d("SendNotification", "Success Status : " + response.body().success);
                }

            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
