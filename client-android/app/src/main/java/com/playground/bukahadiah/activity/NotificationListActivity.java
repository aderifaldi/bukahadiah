package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.NotificationListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHNotification;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends BaseActivity {

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.listNotification)
    RecyclerView listNotification;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;

    private NotificationListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        ButterKnife.bind(this);

        pageTitle.setText("Notifications");

        adapter = new NotificationListAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        listNotification.setAdapter(adapter);
        listNotification.setLayoutManager(linearLayoutManager);

        showLoading();
        GetNotification();

    }

    private void GetNotification(){
        Call<BHNotification> call = apiServiceBH.GetNotification(GlobalVariable.getUserId(getApplicationContext()));

        call.enqueue(new Callback<BHNotification>() {
            @Override
            public void onResponse(Call<BHNotification> call, Response<BHNotification> response) {
                dismissLoading();

                BHNotification apiResponse = response.body();
                if (!apiResponse.isError()){
                    if (apiResponse.data.length > 0){
                        emptyInfo.setVisibility(View.GONE);

                        for (BHNotification.NotificationData notification : apiResponse.data){
                            adapter.getData().add(notification);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                    }else {
                        emptyInfo.setVisibility(View.VISIBLE);
                        emptyInfo.setText("There is no notification");
                    }
                }

            }

            @Override
            public void onFailure(Call<BHNotification> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        exitByBackByPresses();
    }
}
