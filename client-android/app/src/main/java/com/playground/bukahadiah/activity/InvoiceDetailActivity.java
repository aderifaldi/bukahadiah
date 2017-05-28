package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukahadiah.BHInvoice;

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

    private BHInvoice.InvoiceData invoice;
    private SimpleDateFormat sdf;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        ButterKnife.bind(this);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("###,###,###,###", symbols);

        sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");

        invoice = (BHInvoice.InvoiceData) getIntent().getExtras().getSerializable("invoice");

        idTagihan.setText("Payment Charge " + invoice.invoice.invoice_id);
        userName.setText("Hi " + invoice.buyer.name + ",");
        dueDate.setText(sdf.format(invoice.invoice.pay_before));
        totalAmount.setText("Rp. " + decimalFormat.format(invoice.invoice.total_amount));
        transactionDate.setText(sdf.format(invoice.invoice.payment_chosen_at));
        buyer.setText(invoice.buyer.name);
        sellerName.setText(invoice.transactions[0].seller.name);
        productName.setText(invoice.transactions[0].products[0].name);
        amountTotal.setText("Rp. " + decimalFormat.format(invoice.invoice.total_amount));
        amoundCode.setText("" + invoice.invoice.coded_amount);
        totalPayment.setText("Rp. " + decimalFormat.format(invoice.invoice.total_amount));

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
