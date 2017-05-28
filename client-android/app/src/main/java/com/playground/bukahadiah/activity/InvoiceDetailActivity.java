package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukahadiah.BHInvoice;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceDetail;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceDetailActivity extends BaseActivity {

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

    private BHInvoiceDetail.InvoiceData invoice;
    private SimpleDateFormat sdf;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        ButterKnife.bind(this);

        pageTitle.setText("Order Success");

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("###,###,###,###", symbols);

        sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");

        invoice = (BHInvoiceDetail.InvoiceData) getIntent().getExtras().getSerializable("invoice");

        idTagihan.setText("Payment Charge " + invoice.invoice.invoice_id);
        userName.setText("Hi " + invoice.invoice.buyer.name + ",");
        dueDate.setText(sdf.format(invoice.invoice.pay_before));
        totalAmount.setText("Rp. " + decimalFormat.format(invoice.invoice.coded_amount));
        transactionDate.setText(sdf.format(invoice.invoice.created_at));
        buyer.setText(invoice.invoice.buyer.name);
        sellerName.setText(invoice.invoice.transactions[0].seller.name);
        productName.setText(invoice.invoice.transactions[0].products[0].name);
        amountTotal.setText("Rp. " + decimalFormat.format(invoice.invoice.total_amount));
        amoundCode.setText("" + invoice.invoice.uniq_code);
        totalPayment.setText("Rp. " + decimalFormat.format(invoice.invoice.coded_amount));
        shippingAddress.setText(invoice.invoice.consignee.name + "\n" +
                invoice.invoice.consignee.address + "\n" +
                invoice.invoice.consignee.area + ", " + invoice.invoice.consignee.city
                + "\n" + invoice.invoice.consignee.province + " - " +
                invoice.invoice.consignee.post_code + "\n" + invoice.invoice.consignee.phone);

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
