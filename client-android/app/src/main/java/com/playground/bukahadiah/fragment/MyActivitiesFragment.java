package com.playground.bukahadiah.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.ActivityListAdapter;
import com.playground.bukahadiah.adapter.MyActivityListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aderifaldi on 24/08/2016.
 */
@SuppressLint("ValidFragment")
public class MyActivitiesFragment extends BaseFragment {

    @BindView(R.id.listEvent)
    RecyclerView listEvent;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;
    @BindView(R.id.refreshView)
    SwipeRefreshLayout refreshView;

    private View view;
    private MyActivityListAdapter adapter;
    private GridLayoutManager linearLayoutManager;
    private int userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_event, container, false);
        ButterKnife.bind(this, view);

        refreshView.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        userId = getArguments().getInt("userId");

        adapter = new MyActivityListAdapter(getActivity());
        linearLayoutManager = new GridLayoutManager(getActivity(), 3);

        listEvent.setAdapter(adapter);
        listEvent.setLayoutManager(linearLayoutManager);

//        GetActivity();

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        refreshView.setRefreshing(false);
                        GetActivity();
                    }
                }, 2000);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetActivity();
    }

    private void GetActivity() {
        Call<BHActivity> call = apiServiceBH.GetMyActivity(userId);
        call.enqueue(new Callback<BHActivity>() {
            @Override
            public void onResponse(Call<BHActivity> call, Response<BHActivity> response) {
                BHActivity apiResponse = response.body();

                if (!apiResponse.isError()) {

                    if (apiResponse.data.length > 0){
                        emptyInfo.setVisibility(View.GONE);
                        adapter.getData().clear();
                        for (BHActivity.ActivityData activity : apiResponse.data){
                            adapter.getData().add(activity);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        emptyInfo.setVisibility(View.VISIBLE);
                        emptyInfo.setText("There is no activity");
                    }


                }

            }

            @Override
            public void onFailure(Call<BHActivity> call, Throwable t) {

            }
        });
    }

}
