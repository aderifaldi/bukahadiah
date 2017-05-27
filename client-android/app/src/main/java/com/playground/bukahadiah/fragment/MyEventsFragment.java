package com.playground.bukahadiah.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.activity.MyEventDetailActivity;
import com.playground.bukahadiah.adapter.MyEventListAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.helper.GlobalVariable;
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
public class MyEventsFragment extends BaseFragment {

    @BindView(R.id.listEvent)
    RecyclerView listEvent;
    @BindView(R.id.emptyInfo)
    CustomTextView emptyInfo;
    @BindView(R.id.refreshView)
    SwipeRefreshLayout refreshView;

    private View view;
    private MyEventListAdapter adapter;
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

        adapter = new MyEventListAdapter(getActivity());
        linearLayoutManager = new GridLayoutManager(getActivity(), 3);

        listEvent.setAdapter(adapter);
        listEvent.setLayoutManager(linearLayoutManager);

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        refreshView.setRefreshing(false);
                        GetMyEvent();
                    }
                }, 2000);
            }
        });

//        GetMyEvent();
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), MyEventDetailActivity.class)
                        .putExtra("event", adapter.getData().get(i)));
                goToAnimation();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetMyEvent();
    }

    private void GetMyEvent() {
        Call<BHEvent> call = apiServiceBH.GetGiftBoxByUser(userId);
        call.enqueue(new Callback<BHEvent>() {
            @Override
            public void onResponse(Call<BHEvent> call, Response<BHEvent> response) {
                BHEvent apiResponse = response.body();

                if (!apiResponse.isError()) {

                    if (apiResponse.data.length > 0){
                        emptyInfo.setVisibility(View.GONE);
                        adapter.getData().clear();

                        for (BHEvent.EventData event : apiResponse.data){
                            adapter.getData().add(event);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        emptyInfo.setVisibility(View.VISIBLE);
                        emptyInfo.setText("There is no event");
                    }

                }

            }

            @Override
            public void onFailure(Call<BHEvent> call, Throwable t) {

            }
        });
    }

}
