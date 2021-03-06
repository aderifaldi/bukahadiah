package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHBuy;
import com.playground.bukahadiah.model.bukahadiah.BHInvoice;
import com.playground.bukahadiah.model.bukahadiah.BHInvoiceDetail;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;
import com.playground.bukahadiah.model.bukalapak.BLCities;
import com.playground.bukahadiah.model.bukalapak.BLProduct;
import com.playground.bukahadiah.model.bukalapak.BLProvince;
import com.radyalabs.irfan.util.AppUtility;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.actCity)
    AutoCompleteTextView actCity;
    @BindView(R.id.tilCity)
    TextInputLayout tilCity;
    @BindView(R.id.actArea)
    AutoCompleteTextView actArea;
    @BindView(R.id.tilArea)
    TextInputLayout tilArea;
    @BindView(R.id.actAddress)
    AutoCompleteTextView actAddress;
    @BindView(R.id.tilAddress)
    TextInputLayout tilAddress;
    @BindView(R.id.actPostalCode)
    AutoCompleteTextView actPostalCode;
    @BindView(R.id.tilPostalCode)
    TextInputLayout tilPostalCode;
    @BindView(R.id.actName)
    AutoCompleteTextView actName;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.actPhoneNumber)
    AutoCompleteTextView actPhoneNumber;
    @BindView(R.id.tilPhoneNumber)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.paymentMethod)
    Spinner paymentMethod;
    @BindView(R.id.courier)
    Spinner courier;

    private ArrayAdapter<String> adapterProvince, adapterCity, adapterPaymentMethod, adapterCourier;
    private ArrayList<String> arrayProvince, arrayCity;

    private String province, city, name, phone, area, userAddress, postCode, payment, kurir;
    private long price;
    private BLProduct.Product product;
    private int cartId, cartIdBukalapak, wishItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        wishItemId = getIntent().getIntExtra("wishItemId", 0);
        product = (BLProduct.Product) getIntent().getExtras().getSerializable("product");
        price = product.price;

        pageTitle.setText("Shipping & Payment");

        arrayProvince = new ArrayList<>();
        arrayCity = new ArrayList<>();

        actProvince.setTypeface(tfRegular);
        actCity.setTypeface(tfRegular);
        actAddress.setTypeface(tfRegular);
        actArea.setTypeface(tfRegular);
        actPostalCode.setTypeface(tfRegular);
        actName.setTypeface(tfRegular);
        actPhoneNumber.setTypeface(tfRegular);

        tilProvince.setTypeface(tfLight);
        tilCity.setTypeface(tfLight);
        tilAddress.setTypeface(tfLight);
        tilArea.setTypeface(tfLight);
        tilPostalCode.setTypeface(tfLight);
        tilName.setTypeface(tfLight);
        tilPhoneNumber.setTypeface(tfLight);

        adapterProvince = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayProvince);
        adapterPaymentMethod = new ArrayAdapter<>(this, R.layout.item_province, R.id.text);
        adapterCourier = new ArrayAdapter<>(this, R.layout.item_province, R.id.text);

        paymentMethod.setAdapter(adapterPaymentMethod);
        adapterPaymentMethod.add("ATM");

        courier.setAdapter(adapterCourier);

        actProvince.setAdapter(adapterProvince);
        actProvince.setThreshold(1);

        Buy();
        GetProvince();

        actProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                province = actProvince.getText().toString();
                GetCity();
            }
        });

        actCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                city = actCity.getText().toString();
            }
        });

    }

    private void Buy(){
        jsonPost = new JsonObject();
        jsonPost.addProperty("token", GlobalVariable.getBukalapakToken(getApplicationContext()));
        jsonPost.addProperty("product_blid", product.id);
        jsonPost.addProperty("quantity", 1);
        jsonPost.addProperty("user_id", GlobalVariable.getUserId(getApplicationContext()));
        jsonPost.addProperty("item_id", GlobalVariable.getTempItemId(getApplicationContext()));

        showLoading();
        Call<BHBuy> call = apiServiceBH.Buy(jsonPost);
        call.enqueue(new Callback<BHBuy>() {
            @Override
            public void onResponse(Call<BHBuy> call, Response<BHBuy> response) {
                dismissLoading();
                BHBuy apiResponse = response.body();
                if (!apiResponse.isError()){
                    cartId = apiResponse.data.bukahadiah_cart_id;
                    cartIdBukalapak = apiResponse.data.cart_id;;
                    for (String kurir : apiResponse.data.courier){
                        adapterCourier.add(kurir);
                    }
                }
            }

            @Override
            public void onFailure(Call<BHBuy> call, Throwable t) {

            }
        });

    }

    private void GetProvince() {
//        showLoading();
        Call<BLProvince> call = apiServiceBL.GetProvince();
        call.enqueue(new Callback<BLProvince>() {
            @Override
            public void onResponse(Call<BLProvince> call, Response<BLProvince> response) {
//                dismissLoading();
                if (response.body().status.equals("OK")) {
                    for (int i = 0; i < response.body().provinces.length; i++) {
                        arrayProvince.add(response.body().provinces[i]);
                    }
                }
            }

            @Override
            public void onFailure(Call<BLProvince> call, Throwable t) {

            }
        });
    }

    private void GetCity() {
        arrayCity.clear();
        arrayCity = new ArrayList<>();

        showLoading();
        Call<BLCities> call = apiServiceBL.GetCities(province);
        call.enqueue(new Callback<BLCities>() {
            @Override
            public void onResponse(Call<BLCities> call, Response<BLCities> response) {
                dismissLoading();
                if (response.body().status.equals("OK")) {
                    for (int i = 0; i < response.body().cities.length; i++) {
                        arrayCity.add(response.body().cities[i]);
                    }

                    adapterCity = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayCity);
                    actCity.setAdapter(adapterCity);
                    actCity.setThreshold(1);

                    actCity.setText(arrayCity.get(0));
                    city = arrayCity.get(0);
                }
            }

            @Override
            public void onFailure(Call<BLCities> call, Throwable t) {

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

                name = actName.getText().toString();
                phone = "" + actPhoneNumber.getText().toString();
                area = actArea.getText().toString();
                userAddress = actAddress.getText().toString();
                postCode = "" + actPostalCode.getText().toString();
                payment = "atm";
                kurir = adapterCourier.getItem(courier.getSelectedItemPosition());


                String emptyInfo = "still empty";

                if (name.equals("") || name == ""){
                    actName.setError("Name " + emptyInfo);
                } else if (phone.equals("") || phone == ""){
                    actPhoneNumber.setError("Phone number " + emptyInfo);
                } else if (area.equals("") || area == ""){
                    actArea.setError("Area " + emptyInfo);
                } else if (userAddress.equals("") || userAddress == ""){
                    actAddress.setError("Address " + emptyInfo);
                } else if (postCode.equals("") || postCode == ""){
                    actPostalCode.setError("Postal code " + emptyInfo);
                } else {

                    JsonObject paymentInvoice = new JsonObject();
                    JsonObject address = new JsonObject();
                    JsonArray transactionAtribute = new JsonArray();
                    JsonObject transaction = new JsonObject();
                    JsonArray itemIds = new JsonArray();

                    itemIds.add(1);
                    itemIds.add(2);

                    transaction.addProperty("seller_id", product.seller_id);
                    transaction.addProperty("courier", kurir);
                    transaction.addProperty("buyer_notes", "");

                    transactionAtribute.add(transaction);

                    address.addProperty("province", province);
                    address.addProperty("city", city);
                    address.addProperty("area", area);
                    address.addProperty("address", userAddress);
                    address.addProperty("post_code", postCode);

                    paymentInvoice.addProperty("shipping_name", name);
                    paymentInvoice.addProperty("phone", phone);
//                    paymentInvoice.addProperty("deposit_reduction_amount", price);
                    paymentInvoice.add("address", address);
                    paymentInvoice.add("transactions_attributes", transactionAtribute);

                    jsonPost = new JsonObject();
                    jsonPost.add("payment_invoice", paymentInvoice);
                    jsonPost.addProperty("payment_method", payment);
                    jsonPost.addProperty("cart_id", cartIdBukalapak);

                    AppUtility.logD("JsonPost", "JsonPost : " + jsonPost.toString());
                    CreateInvoice();

                }

                break;
        }
    }

    private void CreateInvoice(){
        showLoading();
        Call<BHInvoiceDetail> call = apiServiceBH.CreateInvoice(jsonPost, cartId,
                GlobalVariable.getUserId(getApplicationContext()),
                GlobalVariable.getBukalapakToken(getApplicationContext()));

        call.enqueue(new Callback<BHInvoiceDetail>() {
            @Override
            public void onResponse(Call<BHInvoiceDetail> call, Response<BHInvoiceDetail> response) {
                dismissLoading();
                if (!response.body().isError()){
                    BHInvoiceDetail.InvoiceData invoice = response.body().data;
                    if (invoice != null){
                        startActivity(new Intent(getApplicationContext(), InvoiceDetailActivity.class)
                                .putExtra("invoice", invoice));
                    }
                }
            }

            @Override
            public void onFailure(Call<BHInvoiceDetail> call, Throwable t) {

            }
        });

    }

}
