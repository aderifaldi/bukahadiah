package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.GiftListAdapter;
import com.playground.bukahadiah.adapter.MyGiftListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.helper.ImageDownloader;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;
import com.playground.bukahadiah.model.bukahadiah.BHEventDetail;
import com.playground.bukahadiah.model.bukahadiah.BHGiftItem;
import com.radyalabs.irfan.util.AppUtility;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.addWish)
    RelativeLayout addWish;
    @BindView(R.id.eventImage)
    ImageView eventImage;
    @BindView(R.id.eventName)
    CustomTextView eventName;
    @BindView(R.id.eventDate)
    CustomTextView eventDate;
    @BindView(R.id.eventDesc)
    CustomTextView eventDesc;
    @BindView(R.id.listWish)
    RecyclerView listWish;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;

    private BHEvent.EventData event;
    private SimpleDateFormat sdf;
    private Point screenSize;

    private GiftListAdapter adapter;
    private GridLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);

        addWish.setVisibility(View.GONE);

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        screenSize = AppUtility.getScreenSize(this);

        adapter = new GiftListAdapter(this);
        linearLayoutManager = new GridLayoutManager(this, 3);

        listWish.setAdapter(adapter);
        listWish.setLayoutManager(linearLayoutManager);

        pageTitle.setText("Event");

        event = (BHEvent.EventData) getIntent().getExtras().getSerializable("event");

        GetEventDetail();

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), DetailProductActivity.class)
                        .putExtra("productId", adapter.getData().get(position).item_blid)
                        .putExtra("isFromWishList", false)
                        .putExtra("canConfirm", true)
                        .putExtra("wishItemId", adapter.getData().get(position).item_id));
                goToAnimation();
            }
        });

    }

    private void GetEventDetail(){
        showLoading();
        Call<BHEventDetail> call = apiServiceBH.GetGiftBoxDetail(event.event_id);
        call.enqueue(new Callback<BHEventDetail>() {
            @Override
            public void onResponse(Call<BHEventDetail> call, Response<BHEventDetail> response) {
                dismissLoading();
                BHEventDetail eventDetail = response.body();
                if (!eventDetail.isError()){

                    GlobalVariable.saveTempFriendFCMToken(getApplicationContext(), eventDetail.data.user_fcm_token);

//                    if (eventDetail.data.event_photo != null || eventDetail.data.event_photo != ""){
//                        ImageDownloader imageDownloader = new ImageDownloader(event.event_photo,
//                                getApplicationContext(), new ImageDownloader.OnImageFinishDownload() {
//                            @Override
//                            public void onFinish(Bitmap bitmap, int returnCode) {
//                                if (bitmap != null) {
//                                    eventImage.setImageBitmap(bitmap);
//                                    eventImage.setVisibility(View.VISIBLE);
//
//                                    int layoutHeight = screenSize.x * bitmap.getHeight()/bitmap.getWidth();
//                                    ViewGroup.LayoutParams params = eventImage.getLayoutParams();
//                                    params.height = layoutHeight;
//                                    eventImage.setLayoutParams(params);
//                                }
//                            }
//                        });
//                        imageDownloader.setSizeOption(screenSize.x, true);
//                        imageDownloader.execute();
//                    }

                    Glide.with(getApplicationContext())
                            .load(event.event_photo)
                            .crossFade()
                            .into(eventImage);

                    eventName.setText(eventDetail.data.event_name);
                    eventDesc.setText(eventDetail.data.event_description);
                    if (eventDetail.data.created_date != null){
                        eventDate.setText(sdf.format(eventDetail.data.created_date));
                    }

                    GetWishList();
                }
            }

            @Override
            public void onFailure(Call<BHEventDetail> call, Throwable t) {

            }
        });
    }

    private void GetWishList(){
        Call<BHGiftItem> call = apiServiceBH.GetGiftItem(event.event_id);
        call.enqueue(new Callback<BHGiftItem>() {
            @Override
            public void onResponse(Call<BHGiftItem> call, Response<BHGiftItem> response) {
                BHGiftItem apiResponse = response.body();

                if (!apiResponse.isError()){
                    if (apiResponse.data.length > 0){
                        emptyInfo.setVisibility(View.GONE);
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

    @OnClick({R.id.back, R.id.addWish, R.id.btnSaveDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.addWish:
                startActivity(new Intent(getApplicationContext(), MyWIshboxActivity.class)
                        .putExtra("event", event));
                goToAnimation();
                break;
            case R.id.btnSaveDate:
                try {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra(CalendarContract.Events.TITLE, event.event_name);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.created_date);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.created_date);
                    intent.putExtra(CalendarContract.Events.ALL_DAY, false);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, event.event_description);
                    startActivity(intent);
                }catch (Exception e){

                }
                break;
        }
    }
}
