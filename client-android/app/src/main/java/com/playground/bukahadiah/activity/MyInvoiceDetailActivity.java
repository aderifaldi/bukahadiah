package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHInvoice;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceDetail;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceList;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

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

    private BHInvoiceList.InvoiceListData invoiceList;
    private SimpleDateFormat sdf;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
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

                BHInvoiceDetail invoice = response.body();
                if (!response.body().isError()){
                    if (invoice != null){
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

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
