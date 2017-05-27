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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.playground.bukahadiah.R;
import com.playground.bukahadiah.activity.UserProfileActivity;
import com.playground.bukahadiah.adapter.MemberListAdapter;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHMemberList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aderifaldi on 24/08/2016.
 */
@SuppressLint("ValidFragment")
public class SearchFragment extends BaseFragment {

    @BindView(R.id.searchBar)
    RelativeLayout searchBar;
    @BindView(R.id.searchField)
    EditText searchField;
    @BindView(R.id.listFriend)
    RecyclerView listFriend;
    @BindView(R.id.refreshView)
    SwipeRefreshLayout refreshView;

    private MemberListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        refreshView.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        searchField.setTypeface(tfRegular);

        adapter = new MemberListAdapter(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());

        listFriend.setAdapter(adapter);
        listFriend.setLayoutManager(linearLayoutManager);

        GetMember();
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        refreshView.setRefreshing(false);
                        GetMember();
                    }
                }, 2000);
            }
        });

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getData().get(position).user_blid != null){
                    getActivity().startActivity(new Intent(getActivity(), UserProfileActivity.class)
                            .putExtra("userId", adapter.getData().get(position).user_blid));
                    goToAnimation();
                }

            }
        });

        return view;
    }

    private void GetMember(){
        Call<BHMemberList> call = apiServiceBH.GetListMember(GlobalVariable.getUserId(getActivity()));
        call.enqueue(new Callback<BHMemberList>() {
            @Override
            public void onResponse(Call<BHMemberList> call, Response<BHMemberList> response) {
                BHMemberList apiResponse = response.body();

                if (!apiResponse.isError()){
                    adapter.getData().clear();
                    for (BHMemberList.MemberData member : apiResponse.data){
                        adapter.getData().add(member);
                        adapter.notifyItemInserted(adapter.getData().size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<BHMemberList> call, Throwable t) {

            }
        });
    }

    public void showSearchBar() {
        if (searchBar.getVisibility() == View.VISIBLE){
            searchBar.animate().alpha(0.0f);
            searchBar.setVisibility(View.GONE);
        }else {
            searchBar.animate().alpha(1.0f);
            searchBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideSearchBar() {
        searchBar.setVisibility(View.GONE);
    }

}
