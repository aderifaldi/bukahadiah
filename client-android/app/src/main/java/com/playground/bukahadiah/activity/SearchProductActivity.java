package com.playground.bukahadiah.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.ProductListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.model.bukalapak.BLProduct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductActivity extends BaseActivity {

    private static final int LOAD_PER_PAGE = 20;

    private boolean loading = false;

    private int pastVisiblesItems = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.searchField)
    EditText searchField;
    @BindView(R.id.searchBar)
    RelativeLayout searchBar;
    @BindView(R.id.listProduct)
    RecyclerView listProduct;

    private String keyWord;
    private int page = 0;

    private ProductListAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        ButterKnife.bind(this);
        pageTitle.setText("Search Products");

        keyWord = getIntent().getStringExtra("keyword");
        searchField.setText(keyWord);

        adapter = new ProductListAdapter(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        listProduct.setAdapter(adapter);
        listProduct.setLayoutManager(gridLayoutManager);

        showLoading();
        searchProduct(keyWord, true);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

                    keyWord = searchField.getText().toString();
                    if (!keyWord.equals("")){
                        page = 0;
                        searchProduct(keyWord, true);
                    }

                    return true;
                }
                return false;
            }
        });

        listProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = gridLayoutManager.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems + 1) >= totalItemCount) {
                        searchProduct(keyWord, false);
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BLProduct.Product product = adapter.getData().get(i);
                startActivity(new Intent(getApplicationContext(), DetailProductActivity.class)
                        .putExtra("productId", product.id)
                        .putExtra("isFromWishList", false)
                        .putExtra("canConfirm", false)
                        .putExtra("wishItemId", 0));
                goToAnimation();
            }
        });

    }

    private void searchProduct(String keyword, final boolean isSearch){
        Call<BLProduct> call = apiServiceBL.SearchProduct(keyword, page + 1, LOAD_PER_PAGE);
        call.enqueue(new Callback<BLProduct>() {
            @Override
            public void onResponse(Call<BLProduct> call, Response<BLProduct> response) {
                dismissLoading();
                BLProduct apiResponse = response.body();

                if (apiResponse.status.equals("OK")){
                    loading = true;
                    page++;

                    if (isSearch){
                        adapter.getData().clear();
                    }

                    for (BLProduct.Product product : apiResponse.products){
                        adapter.getData().add(product);
                        adapter.notifyItemInserted(adapter.getData().size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<BLProduct> call, Throwable t) {

            }
        });

        loading = false;
    }

    @OnClick(R.id.back)
    public void onClick() {
        exitByBackByPresses();
    }
}
