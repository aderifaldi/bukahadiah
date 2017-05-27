package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.imageview.CircleImageView;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.Notification;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;
import com.playground.bukahadiah.model.bukalapak.BLProduct;
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

public class DetailProductActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.imageProduct)
    ImageView imageProduct;
    @BindView(R.id.ratting)
    CustomTextView ratting;
    @BindView(R.id.userCount)
    CustomTextView userCount;
    @BindView(R.id.containerRating)
    LinearLayout containerRating;
    @BindView(R.id.sellerAvatar)
    CircleImageView sellerAvatar;
    @BindView(R.id.sellerName)
    CustomTextView sellerName;
    @BindView(R.id.sellerLevel)
    CustomTextView sellerLevel;
    @BindView(R.id.productName)
    CustomTextView productName;
    @BindView(R.id.productPrice)
    CustomTextView productPrice;
    @BindView(R.id.textIndicator)
    CustomTextView textIndicator;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.addToWishBox)
    RelativeLayout addToWishBox;
    @BindView(R.id.condition)
    CustomTextView condition;
    @BindView(R.id.soldCount)
    CustomTextView soldCount;
    @BindView(R.id.viewedCount)
    CustomTextView viewedCount;
    @BindView(R.id.lastUpdate)
    CustomTextView lastUpdate;
    @BindView(R.id.reviewedCount)
    CustomTextView reviewedCount;
    @BindView(R.id.desc)
    CustomTextView desc;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.buyText)
    CustomTextView buyText;
    @BindView(R.id.btnConfirmation)
    RelativeLayout btnConfirmation;
    @BindView(R.id.btnSeeInBL)
    RelativeLayout btnSeeInBL;

    private BLProductDetail productDetail;
    private BLProduct.Product product;
    private DecimalFormat decimalFormat;
    private DecimalFormatSymbols symbols;
    private SimpleDateFormat sdf;

    private boolean isFromWishList, canDelete, canConfirm;
    private int wishItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        ButterKnife.bind(this);

        isFromWishList = getIntent().getBooleanExtra("isFromWishList", false);
        canConfirm = getIntent().getBooleanExtra("canConfirm", false);
        wishItemId = getIntent().getIntExtra("wishItemId", 0);

        if (isFromWishList && !canConfirm) {
            pageTitle.setText("Delete from Wishbox");
            icon.setImageResource(R.drawable.ic_delete);
            canDelete = true;

            btnConfirmation.setVisibility(View.GONE);
            addToWishBox.setVisibility(View.VISIBLE);
        } else if (!isFromWishList && !canConfirm){
            pageTitle.setText("Add to Wishbox");
            icon.setImageResource(R.drawable.ic_add);
            canDelete = false;

            btnConfirmation.setVisibility(View.GONE);
            addToWishBox.setVisibility(View.VISIBLE);
        } else if (canConfirm){
            pageTitle.setText("Product Detail");
            btnConfirmation.setVisibility(View.VISIBLE);
            addToWishBox.setVisibility(View.GONE);
        }

        sdf = new SimpleDateFormat(GlobalVariable.DATE_FORMAT);

        symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("###,###,###,###", symbols);


        getProductDetail(getIntent().getStringExtra("productId"));

    }

    private void getProductDetail(final String productId) {
        showLoading();
        Call<BLProductDetail> call = apiServiceBL.GetDetailProduct(productId);
        call.enqueue(new Callback<BLProductDetail>() {
            @Override
            public void onResponse(Call<BLProductDetail> call, Response<BLProductDetail> response) {
                dismissLoading();
                productDetail = response.body();
                product = productDetail.product;

                if (productDetail.status.equals("OK")) {

                    String total = "" + productDetail.product.images.length;
                    textIndicator.setText("1/" + total);

                    productName.setText(productDetail.product.name);
                    productPrice.setText("Rp. " + decimalFormat.format(productDetail.product.price));

                    Glide.with(getApplicationContext())
                            .load(productDetail.product.images[0])
                            .crossFade()
                            .into(imageProduct);

                    Glide.with(getApplicationContext())
                            .load(productDetail.product.seller_avatar)
                            .crossFade()
                            .into(sellerAvatar);

                    sellerName.setText(productDetail.product.seller_name);
                    sellerLevel.setText(productDetail.product.city);

                    if (productDetail.product.rating.user_count != 0) {
                        ratting.setText("Rating: " + ("" + productDetail.product.rating.average_rate));
                        userCount.setText("  (" + ("" + productDetail.product.rating.user_count) + ")");
                    } else {
                        ratting.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondaryFont));
                        ratting.setText("(No rating)");
                        userCount.setText("");
                    }

                    condition.setText(productDetail.product.condition);

                    int sold = productDetail.product.sold_count;
                    int viewed = productDetail.product.view_count;

                    if (sold != 0) {
                        soldCount.setText("" + productDetail.product.sold_count + " times");
                    } else {
                        soldCount.setText("-");
                    }

                    if (viewed != 0) {
                        viewedCount.setText("" + productDetail.product.view_count + " times");
                    } else {
                        viewedCount.setText("-");
                    }

                    lastUpdate.setText(sdf.format(productDetail.product.updated_at));
                    desc.setText(Html.fromHtml(productDetail.product.desc));

                }

            }

            @Override
            public void onFailure(Call<BLProductDetail> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.addToWishBox, R.id.imageProduct, R.id.sellerName, R.id.btnSeeInBL, R.id.btnConfirmation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.addToWishBox:
                if (!canDelete) {
                    AddWish();
                } else {
                    DeleteWish(wishItemId);
                }

                break;
            case R.id.imageProduct:
                startActivity(new Intent(getApplicationContext(), PhotoSliderActivity.class)
                        .putExtra("product", product));
                goToAnimation();
                break;
            case R.id.sellerName:
                break;
            case R.id.btnSeeInBL:

                startActivity(new Intent(getApplicationContext(), BukaLapakViewActivity.class)
                        .putExtra("url", productDetail.product.url));
                goToAnimation();

                break;
            case R.id.btnConfirmation:

                startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
                goToAnimation();

//                showLoading();
//
//                SimpleDateFormat sdf = new SimpleDateFormat(GlobalVariable.DATE_FORMAT);
//                Calendar calendar = Calendar.getInstance();
//
//                jsonPost = new JsonObject();
//                jsonPost.addProperty("gift_item_id", wishItemId);
//                jsonPost.addProperty("user_id", GlobalVariable.getUserId(getApplicationContext()));
//                jsonPost.addProperty("friend_id", GlobalVariable.getTempFriendId(getApplicationContext()));
//                jsonPost.addProperty("title", GlobalVariable.getNameUser(getApplicationContext()));
//                jsonPost.addProperty("message", "has confirmed to gift you " + productDetail.product.name);
//                jsonPost.addProperty("create_date", sdf.format(calendar.getTime()));
//
//                Call<ModelBase> call = apiServiceBH.AddNotification(jsonPost);
//                call.enqueue(new Callback<ModelBase>() {
//                    @Override
//                    public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
//                        if (!response.body().isError()){
//                            dismissLoading();
//                            sendNotification();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ModelBase> call, Throwable t) {
//
//                    }
//                });

                break;
        }
    }

    private void sendNotification(){
        jsonPost = new JsonObject();

        JsonObject notification = new JsonObject();
        notification.addProperty("title", GlobalVariable.getNameUser(getApplicationContext()));
        notification.addProperty("message", "has confirmed to gift you " + productDetail.product.name);

        jsonPost.add("data", notification);
        jsonPost.addProperty("to", GlobalVariable.getTempFriendFCMToken(getApplicationContext()));
        jsonPost.addProperty("priority", "High");

        Call<Notification> call = apiServiceNotification.sendNotification(jsonPost);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {

                Notification apiResponse = response.body();

                if (apiResponse.success == 1){
                    Toast.makeText(DetailProductActivity.this, "Confirmation sent!", Toast.LENGTH_SHORT).show();
                    Log.d("SendNotification", "Success Status : " + response.body().success);
                }

            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

            }
        });


    }

    private void DeleteWish(int id){
        showLoading();
        Call<ModelBase> call = apiServiceBH.DeleteGift(id);
        call.enqueue(new Callback<ModelBase>() {
            @Override
            public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
                dismissLoading();
                if (!response.body().isError()){
                    Toast.makeText(DetailProductActivity.this, "Wish deleted from WishBox", Toast.LENGTH_SHORT).show();
                    exitByBackByPresses();
                }
            }

            @Override
            public void onFailure(Call<ModelBase> call, Throwable t) {

            }
        });
    }

    private void AddWish() {
        showLoading();
        jsonPost = new JsonObject();
        jsonPost.addProperty("event_id", GlobalVariable.getTempEventId(getApplicationContext()));
        jsonPost.addProperty("item_name", productDetail.product.name);
        if (productDetail.product.desc.length() >= 115) {
            jsonPost.addProperty("item_desc", productDetail.product.desc.substring(0, 114));
        } else {
            jsonPost.addProperty("item_desc", productDetail.product.desc);
        }
        jsonPost.addProperty("item_blid", productDetail.product.id);
        jsonPost.addProperty("is_completed", false);
        jsonPost.addProperty("item_bl_params", "");

        if (productDetail.product.images.length != 0) {
            jsonPost.addProperty("item_pic", productDetail.product.images[0]);
        }

        Call<ModelBase> call = apiServiceBH.AddWish(jsonPost, GlobalVariable.getUserId(getApplicationContext()));
        call.enqueue(new Callback<ModelBase>() {
            @Override
            public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
                dismissLoading();
                Toast.makeText(DetailProductActivity.this, productDetail.product.name + " added to Wishbox", Toast.LENGTH_SHORT).show();
                exitByBackByPresses();
            }

            @Override
            public void onFailure(Call<ModelBase> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.containerReviewed)
    public void onClick() {
        startActivity(new Intent(getApplicationContext(), ProductReviewActivity.class)
                .putExtra("product", product));
        goToAnimation();
    }
}
