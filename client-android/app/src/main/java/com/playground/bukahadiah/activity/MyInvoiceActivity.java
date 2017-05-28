package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.MyInvoiceListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInvoiceActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.listInvoice)
    RecyclerView listInvoice;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;

    private MyInvoiceListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invoice);
        ButterKnife.bind(this);

        pageTitle.setText("My Invoices");

        adapter = new MyInvoiceListAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        listInvoice.setAdapter(adapter);
        listInvoice.setLayoutManager(linearLayoutManager);

        GetInvoice();

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), MyInvoiceDetailActivity.class)
                        .putExtra("invoice", adapter.getData().get(i)));
                goToAnimation();
            }
        });

    }

    private void GetInvoice(){
        showLoading();
        Call<BHInvoiceList> call = apiServiceBH.GetInvoice(GlobalVariable.getUserId(getApplicationContext()));
        call.enqueue(new Callback<BHInvoiceList>() {
            @Override
            public void onResponse(Call<BHInvoiceList> call, Response<BHInvoiceList> response) {
                dismissLoading();
                BHInvoiceList apiResponse = response.body();

                if (!apiResponse.isError()){
                    if (apiResponse.data.length > 0){
                        for (BHInvoiceList.InvoiceListData invoice : apiResponse.data){
                            adapter.getData().add(invoice);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        emptyInfo.setVisibility(View.VISIBLE);
                        emptyInfo.setText("There id no invoice");
                    }
                }

            }

            @Override
            public void onFailure(Call<BHInvoiceList> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
