package com.playground.bukahadiah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.HomePagerAdapter;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.customui.viewpager.NonSwipeableViewPager;
import com.playground.bukahadiah.fragment.AddNewFragment;
import com.playground.bukahadiah.fragment.EventsFragment;
import com.playground.bukahadiah.fragment.HomeFragment;
import com.playground.bukahadiah.fragment.ProfileFragment;
import com.playground.bukahadiah.fragment.SearchFragment;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.ModelBase;
import com.radyalabs.irfan.util.AppUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final int MENUS = 5;

    @BindView(R.id.btnHome)
    ImageView btnHome;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.btnAdd)
    ImageView btnAdd;
    @BindView(R.id.btnHeart)
    ImageView btnHeart;
    @BindView(R.id.btnUser)
    ImageView btnUser;
    @BindView(R.id.body)
    NonSwipeableViewPager body;
    @BindView(R.id.bgBtnAdd)
    RelativeLayout bgBtnAdd;
    @BindView(R.id.btnMenuTop)
    ImageView btnMenuTop;
    @BindView(R.id.menuTop)
    RelativeLayout menuTop;
    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;

    private HomePagerAdapter homePagerAdapter;

    private boolean isSearchFriend, isNotification, isAddEvent, isSettings, isAddPost;

    private HomeFragment home;
    private SearchFragment search;
    private AddNewFragment addNew;
    private EventsFragment heart;
    private ProfileFragment profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        home = new HomeFragment();
        search = new SearchFragment();
        addNew = new AddNewFragment();
        heart = new EventsFragment();
        profile = new ProfileFragment();

        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), MENUS, home,
                search, addNew, heart, profile);

        body.setCurrentItem(0);
        body.setAdapter(homePagerAdapter);
        body.setOffscreenPageLimit(5);

        isSearchFriend = false;
        isAddEvent = false;
        isNotification = true;
        isSettings = false;
        isAddPost = false;

        menuTop.setVisibility(View.VISIBLE);
        btnMenuTop.setImageResource(R.drawable.notification);

        AppUtility.logD("Token", "Token BL : " + GlobalVariable.getBukalapakToken(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalVariable.getIsProfileUpdated(getApplicationContext())){
            UpdateProfilePicture();
        }
    }

    @OnClick({R.id.menuHome, R.id.menuSearch, R.id.menuAdd, R.id.menuHeart, R.id.menuUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuHome:
                menuHomeSelected();
                body.setCurrentItem(0);
                break;
            case R.id.menuHeart:
                menuHeartSelected();
                body.setCurrentItem(1);
                break;
            case R.id.menuAdd:
                startActivity(new Intent(getApplicationContext(), AddPostActivity.class));
                menuHomeSelected();
                body.setCurrentItem(0);
                break;
            case R.id.menuSearch:
                menuSearchSelected();
                body.setCurrentItem(3);
                break;
            case R.id.menuUser:
                menuUserSelected();
                body.setCurrentItem(4);
                break;
        }
    }

    private void menuHomeSelected() {

        pageTitle.setText(getResources().getString(R.string.app_name));

        btnHome.setImageResource(R.drawable.ic_home_selected);
        btnSearch.setImageResource(R.drawable.ic_search);
        bgBtnAdd.setBackgroundResource(R.drawable.bg_add_new);
        btnHeart.setImageResource(R.drawable.ic_heart);
        btnUser.setImageResource(R.drawable.ic_user);

        menuTop.setVisibility(View.VISIBLE);
        btnMenuTop.setImageResource(R.drawable.notification);

        isSearchFriend = false;
        isAddEvent = false;
        isNotification = true;
        isSettings = false;
        isAddPost = false;

        search.hideSearchBar();
    }

    private void menuSearchSelected() {

        pageTitle.setText(getResources().getString(R.string.friends));

        btnHome.setImageResource(R.drawable.ic_home);
        btnSearch.setImageResource(R.drawable.ic_search_selected);
        bgBtnAdd.setBackgroundResource(R.drawable.bg_add_new);
        btnHeart.setImageResource(R.drawable.ic_heart);
        btnUser.setImageResource(R.drawable.ic_user);

        menuTop.setVisibility(View.VISIBLE);
        btnMenuTop.setImageResource(R.drawable.ic_search_white);

        isSearchFriend = true;
        isAddEvent = false;
        isNotification = false;
        isSettings = false;
        isAddPost = false;

        search.hideSearchBar();
    }

    private void menuAddSelected() {

        pageTitle.setText(getResources().getString(R.string.addPost));

        btnHome.setImageResource(R.drawable.ic_home);
        btnSearch.setImageResource(R.drawable.ic_search);
        bgBtnAdd.setBackgroundResource(R.drawable.bg_add_new_selected);
        btnHeart.setImageResource(R.drawable.ic_heart);
        btnUser.setImageResource(R.drawable.ic_user);

        menuTop.setVisibility(View.GONE);
        btnMenuTop.setImageResource(R.drawable.send_post);

        search.hideSearchBar();

        isAddPost = true;
        isSearchFriend = false;
        isAddEvent = false;
        isNotification = false;
        isSettings = false;
    }

    private void menuHeartSelected() {

        pageTitle.setText(getResources().getString(R.string.event));

        btnHome.setImageResource(R.drawable.ic_home);
        btnSearch.setImageResource(R.drawable.ic_search);
        bgBtnAdd.setBackgroundResource(R.drawable.bg_add_new);
        btnHeart.setImageResource(R.drawable.ic_heart_selected);
        btnUser.setImageResource(R.drawable.ic_user);

        menuTop.setVisibility(View.VISIBLE);
        btnMenuTop.setImageResource(R.drawable.ic_add);

        isSearchFriend = false;
        isAddEvent = true;
        isNotification = false;
        isSettings = false;
        isAddPost = false;

        search.hideSearchBar();
    }

    private void menuUserSelected() {

        pageTitle.setText(getResources().getString(R.string.profile));

        btnHome.setImageResource(R.drawable.ic_home);
        btnSearch.setImageResource(R.drawable.ic_search);
        bgBtnAdd.setBackgroundResource(R.drawable.bg_add_new);
        btnHeart.setImageResource(R.drawable.ic_heart);
        btnUser.setImageResource(R.drawable.ic_user_selected);

        menuTop.setVisibility(View.VISIBLE);
        btnMenuTop.setImageResource(R.drawable.ic_logout);

        isSearchFriend = false;
        isAddEvent = false;
        isNotification = false;
        isSettings = true;
        isAddPost = false;

        search.hideSearchBar();
    }

    public void goToProfile(){
        body.setCurrentItem(4);
        pageTitle.setText(getResources().getString(R.string.profile));

        btnHome.setImageResource(R.drawable.ic_home);
        btnSearch.setImageResource(R.drawable.ic_search);
        bgBtnAdd.setBackgroundResource(R.drawable.bg_add_new);
        btnHeart.setImageResource(R.drawable.ic_heart);
        btnUser.setImageResource(R.drawable.ic_user_selected);

        menuTop.setVisibility(View.VISIBLE);
        btnMenuTop.setImageResource(R.drawable.ic_logout);

        isSearchFriend = false;
        isAddEvent = false;
        isNotification = false;
        isSettings = true;
        isAddPost = false;

        search.hideSearchBar();
    }

    private void UpdateProfilePicture(){
//        showLoading();
        jsonPost = new JsonObject();
        jsonPost.addProperty("user_photo", GlobalVariable.getUserProfileImage(getApplicationContext()));

        Call<ModelBase> call = apiServiceBH.UpdatePhoto(jsonPost, GlobalVariable.getUserId(getApplicationContext()));
        call.enqueue(new Callback<ModelBase>() {
            @Override
            public void onResponse(Call<ModelBase> call, Response<ModelBase> response) {
//                dismissLoading();
                if (!response.body().isError()){
                    AppUtility.logD("UpdateProfile", "Update profile photo success");
                }
            }

            @Override
            public void onFailure(Call<ModelBase> call, Throwable t) {

            }
        });
    }

    public void Refresh(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        backAnimation();
        finish();
    }

    @OnClick(R.id.menuTop)
    public void onClick() {
        if (isSearchFriend){
            search.showSearchBar();
        }else {
            if (isAddEvent){
                //Add new event
                startActivity(new Intent(getApplicationContext(), AddEventActivity.class));
                goToAnimation();
            }else if (isAddPost){
                //Goto notification
//                addNew.SendPost();
            }else if (isSettings){
                //Goto settings
//                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
//                goToAnimation();

                GlobalVariable.saveIsLogin(getApplicationContext(), false);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                backAnimation();
                finish();
            }  else {
                startActivity(new Intent(getApplicationContext(), NotificationListActivity.class));
                goToAnimation();
            }
        }
    }
}
