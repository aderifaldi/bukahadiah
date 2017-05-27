package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.MyGiftListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;
import com.playground.bukahadiah.model.bukahadiah.BHGiftItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWIshboxActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.addWishBox)
    RelativeLayout addWishBox;
    @BindView(R.id.searchProduct)
    LinearLayout searchProduct;
    @BindView(R.id.eventName)
    CustomTextView eventName;
    @BindView(R.id.searchBar)
    RelativeLayout searchBar;
    @BindView(R.id.listWish)
    RecyclerView listWish;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;

    private BHEvent.EventData event;
    private MyGiftListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishbox);
        ButterKnife.bind(this);
        pageTitle.setText("My Wish Box");

        showLoading();

        adapter = new MyGiftListAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        event = (BHEvent.EventData) getIntent().getExtras().getSerializable("event");

        eventName.setText(event.event_name);

        listWish.setAdapter(adapter);
        listWish.setLayoutManager(linearLayoutManager);

//        GetWishList();

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), DetailProductActivity.class)
                        .putExtra("productId", adapter.getData().get(position).item_blid)
                        .putExtra("isFromWishList", true)
                        .putExtra("canConfirm", false)
                        .putExtra("wishItemId", adapter.getData().get(position).item_id));
                goToAnimation();
            }
        });
    }

    private void GetWishList(){
        Call<BHGiftItem> call = apiServiceBH.GetGiftItem(event.event_id);
        call.enqueue(new Callback<BHGiftItem>() {
            @Override
            public void onResponse(Call<BHGiftItem> call, Response<BHGiftItem> response) {
                dismissLoading();
                BHGiftItem apiResponse = response.body();

                if (!apiResponse.isError()){
                    if (apiResponse.data.length > 0){
                        adapter.getData().clear();
                        for (BHGiftItem.GiftItemData gift : apiResponse.data){
                            adapter.getData().add(gift);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                    }else {
                        emptyInfo.setVisibility(View.VISIBLE);
                        emptyInfo.setText("There is no wish item");
                    }
                }

            }

            @Override
            public void onFailure(Call<BHGiftItem> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load wish list
        GetWishList();
    }

    @OnClick({R.id.back, R.id.addWishBox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.addWishBox:
                GlobalVariable.saveTempEventId(getApplicationContext(), event.event_id);
                startActivity(new Intent(getApplicationContext(), AddWishItemActivity.class));
                goToAnimation();
                break;
        }
    }
}
