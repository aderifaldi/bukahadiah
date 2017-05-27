package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProvince;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.btnPay)
    RelativeLayout btnPay;
    @BindView(R.id.actProvince)
    AutoCompleteTextView actProvince;
    @BindView(R.id.tilProvince)
    TextInputLayout tilProvince;

    private ArrayAdapter<String> adapterProvince;
    private ArrayList<String> arrayProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        pageTitle.setText("Payment");

        arrayProvince = new ArrayList<>();

        actProvince.setTypeface(tfRegular);
        tilProvince.setTypeface(tfLight);

        adapterProvince = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayProvince);
        actProvince.setAdapter(adapterProvince);
        actProvince.setThreshold(1);

        GetProvince();

    }

    private void GetProvince(){
        Call<BLProvince> call = apiServiceBL.GetProvince();
        call.enqueue(new Callback<BLProvince>() {
            @Override
            public void onResponse(Call<BLProvince> call, Response<BLProvince> response) {
                if (response.body().status.equals("OK")){
                    for (int i = 0; i < response.body().provinces.length; i++){
                        arrayProvince.add(response.body().provinces[i]);
                    }

                    actProvince.setText(arrayProvince.get(4));
                }
            }

            @Override
            public void onFailure(Call<BLProvince> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.btnPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.btnPay:
                Toast.makeText(this, actProvince.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
