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
import com.playground.bukahadiah.adapter.EventListAdapter;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by aderifaldi on 24/08/2016.
 */
@SuppressLint("ValidFragment")
public class EventsFragment extends BaseFragment {

    @BindView(R.id.listEvent)
    RecyclerView listEvent;
    @BindView(R.id.refreshView)
    SwipeRefreshLayout refreshView;

    private View view;
    private EventListAdapter adapter;
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

        adapter = new EventListAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());

        listEvent.setAdapter(adapter);
        listEvent.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Integer.parseInt(adapter.getData().get(i).user_blid) != GlobalVariable.getUserId(getActivity())){
                    startActivity(new Intent(getActivity(), EventDetailActivity.class)
                            .putExtra("event", adapter.getData().get(i)));
                    goToAnimation();
                }else {
                    startActivity(new Intent(getActivity(), MyEventDetailActivity.class)
                            .putExtra("event", adapter.getData().get(i)));
                    goToAnimation();
                }

            }
        });

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetMyEvent();
    }

    private void GetMyEvent() {
        Call<BHEvent> call = apiServiceBH.GetGiftBox(GlobalVariable.getUserId(getActivity()));
        call.enqueue(new Callback<BHEvent>() {
            @Override
            public void onResponse(Call<BHEvent> call, Response<BHEvent> response) {
                BHEvent apiResponse = response.body();

                if (!apiResponse.isError()) {
                    adapter.getData().clear();
                    for (BHEvent.EventData event : apiResponse.data){
                        adapter.getData().add(event);
                        adapter.notifyItemInserted(adapter.getData().size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<BHEvent> call, Throwable t) {

            }
        });
    }

}
