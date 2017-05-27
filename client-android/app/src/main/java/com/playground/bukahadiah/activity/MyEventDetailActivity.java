package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.ImageDownloader;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;
import com.radyalabs.irfan.util.AppUtility;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyEventDetailActivity extends BaseActivity {

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
    @BindView(R.id.btnSaveDate)
    RelativeLayout btnSaveDate;

    private BHEvent.EventData event;
    private SimpleDateFormat sdf;
    private Point screenSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_event);
        ButterKnife.bind(this);

        btnSaveDate.setVisibility(View.GONE);

        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        screenSize = AppUtility.getScreenSize(this);

        pageTitle.setText("My Event");

        event = (BHEvent.EventData) getIntent().getExtras().getSerializable("event");

        if (event.event_photo != null || event.event_photo != ""){
            ImageDownloader imageDownloader = new ImageDownloader(event.event_photo,
                    getApplicationContext(), new ImageDownloader.OnImageFinishDownload() {
                @Override
                public void onFinish(Bitmap bitmap, int returnCode) {
                    if (bitmap != null) {
                        eventImage.setImageBitmap(bitmap);
                        eventImage.setVisibility(View.VISIBLE);

                        int layoutHeight = screenSize.x * bitmap.getHeight()/bitmap.getWidth();
                        ViewGroup.LayoutParams params = eventImage.getLayoutParams();
                        params.height = layoutHeight;
                        eventImage.setLayoutParams(params);
                    }
                }
            });
            imageDownloader.setSizeOption(screenSize.x, true);
            imageDownloader.execute();
        }

//        Glide.with(getApplicationContext())
//                .load(event.event_photo)
//                .crossFade()
//                .into(eventImage);

        eventName.setText(event.event_name);
        eventDesc.setText(event.event_description);
        if (event.created_date != null){
            eventDate.setText(sdf.format(event.created_date));
        }

    }

    @OnClick({R.id.back, R.id.addWish})
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
        }
    }
}
