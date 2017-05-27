package com.playground.bukahadiah.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.activity.EventDetailActivity;
import com.playground.bukahadiah.activity.MyEventDetailActivity;
import com.playground.bukahadiah.adapter.ActivityListAdapter;
import com.playground.bukahadiah.adapter.EventListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHActivity;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aderifaldi on 24/08/2016.
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment {

    @BindView(R.id.listEvent)
    RecyclerView listEvent;
    @BindView(R.id.refreshView)
    SwipeRefreshLayout refreshView;

    private View view;
    private ActivityListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        refreshView.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        adapter = new ActivityListAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());

        listEvent.setAdapter(adapter);
        listEvent.setLayoutManager(linearLayoutManager);

        GetActivity();

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

    private void GetActivity() {
        Call<BHActivity> call = apiServiceBH.GetActivity(GlobalVariable.getUserId(getActivity()));
        call.enqueue(new Callback<BHActivity>() {
            @Override
            public void onResponse(Call<BHActivity> call, Response<BHActivity> response) {
                BHActivity apiResponse = response.body();

                if (!apiResponse.isError()) {
                    adapter.getData().clear();
                    for (BHActivity.ActivityData activity : apiResponse.data){
                        adapter.getData().add(activity);
                        adapter.notifyItemInserted(adapter.getData().size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<BHActivity> call, Throwable t) {

            }
        });
    }

}
