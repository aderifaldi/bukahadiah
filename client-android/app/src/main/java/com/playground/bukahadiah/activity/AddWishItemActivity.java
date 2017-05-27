package com.playground.bukahadiah.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.ProductCategoryAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProduct;
import com.playground.bukahadiah.model.bukalapak.BLProductCategory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWishItemActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.searchProduct)
    LinearLayout searchProduct;
    @BindView(R.id.searchField)
    EditText searchField;
    @BindView(R.id.searchBar)
    RelativeLayout searchBar;
    @BindView(R.id.listCategory)
    RecyclerView listCategory;

    private ProductCategoryAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish_item);
        ButterKnife.bind(this);

        pageTitle.setText("Add Wish Item");

        adapter = new ProductCategoryAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        listCategory.setAdapter(adapter);
        listCategory.setLayoutManager(linearLayoutManager);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

                    Intent intent = new Intent(getApplicationContext(), SearchProductActivity.class);
                    intent.putExtra("keyword", searchField.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    searchField.setText("");
                    return true;
                }
                return false;
            }
        });

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BLProductCategory.Categories category = adapter.getData().get(i);
                startActivity(new Intent(getApplicationContext(), ProductByCategoryActivity.class)
                        .putExtra("categoryName", category.name)
                        .putExtra("categoryId", category.id));
                goToAnimation();
            }
        });

        getCategory();

    }

    private void getCategory(){

        showLoading();

        Call<BLProductCategory> call = apiServiceBL.GetProductCategory();
        call.enqueue(new Callback<BLProductCategory>() {
            @Override
            public void onResponse(Call<BLProductCategory> call, Response<BLProductCategory> response) {
                dismissLoading();

                BLProductCategory apiResponse = response.body();

                if (apiResponse.status.equals("OK")){
                    for (BLProductCategory.Categories categories : apiResponse.categories){
                        adapter.getData().add(categories);
                        adapter.notifyItemInserted(adapter.getData().size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<BLProductCategory> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.wishBox, R.id.searchPoduct})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.wishBox:
                break;
            case R.id.searchPoduct:
                if (searchBar.getVisibility() == View.VISIBLE){
                    searchBar.animate().alpha(0.0f);
                    searchBar.setVisibility(View.GONE);
                }else {
                    searchBar.animate().alpha(1.0f);
                    searchBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
