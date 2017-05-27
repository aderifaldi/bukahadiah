package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.ProductReviewAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProduct;
import com.playground.bukahadiah.model.bukalapak.BLProductReview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductReviewActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.listReview)
    RecyclerView listReview;
    @BindView(R.id.productName)
    CustomTextView productName;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;

    private BLProduct.Product product;
    private ProductReviewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_review);
        ButterKnife.bind(this);

        pageTitle.setText("Product Reviews");

        adapter = new ProductReviewAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        listReview.setAdapter(adapter);
        listReview.setLayoutManager(linearLayoutManager);

        product = (BLProduct.Product) getIntent().getExtras().getSerializable("product");

        productName.setText(product.name);

        getReview(product.id);

    }

    private void getReview(String productId){
        showLoading();
        Call<BLProductReview> call = apiServiceBL.GetProductReviews(productId);
        call.enqueue(new Callback<BLProductReview>() {
            @Override
            public void onResponse(Call<BLProductReview> call, Response<BLProductReview> response) {
                dismissLoading();
                BLProductReview apiResponse = response.body();

                if (apiResponse.status.equals("OK")){

                    if (apiResponse.reviews.length > 0){
                        emptyInfo.setVisibility(View.GONE);
                        for (BLProductReview.Review review : apiResponse.reviews){
                            adapter.getData().add(review);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        emptyInfo.setVisibility(View.VISIBLE);
                        emptyInfo.setText("There is no reviews");
                    }

                }

            }

            @Override
            public void onFailure(Call<BLProductReview> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        exitByBackByPresses();
    }
}
