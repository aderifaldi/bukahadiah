package com.playground.bukahadiah.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.playground.bukahadiah.R;
import com.playground.bukahadiah.adapter.ProfilePagerAdapter;
import com.playground.bukahadiah.customui.imageview.CircleImageView;
import com.playground.bukahadiah.customui.textview.CustomTextView;
import com.playground.bukahadiah.fragment.MyActivitiesFragment;
import com.playground.bukahadiah.fragment.MyEventsFragment;
import com.playground.bukahadiah.helper.GlobalVariable;
import com.playground.bukahadiah.model.bukahadiah.BHUser;
import com.playground.bukahadiah.model.bukahadiah.BHUserDetail;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends BaseActivity {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String FILE_EXTENTION = ".png";
    private static final String FILE_NAME = "event_image";

    private static final int RESULT_LOAD_IMAGE_FROM_CAMERA = 10;
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 11;

    @BindView(R.id.pageTitle)
    CustomTextView pageTitle;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.imageUser)
    CircleImageView imageUser;
    @BindView(R.id.follower)
    CustomTextView follower;
    @BindView(R.id.following)
    CustomTextView following;
    @BindView(R.id.containerFollower)
    LinearLayout containerFollower;
    @BindView(R.id.nameUser)
    CustomTextView nameUser;
    @BindView(R.id.username)
    CustomTextView username;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.txtActivities)
    CustomTextView txtActivities;
    @BindView(R.id.menuActivities)
    RelativeLayout menuActivities;
    @BindView(R.id.txtEvents)
    CustomTextView txtEvents;
    @BindView(R.id.menuEvents)
    RelativeLayout menuEvents;
    @BindView(R.id.pagerProfile)
    ViewPager pagerProfile;
    @BindView(R.id.txtFollow)
    CustomTextView txtFollow;
    @BindView(R.id.btnFollow)
    RelativeLayout btnFollow;

    private CharSequence pagerTitle[] = {"", ""};
    private int numbOfTabs;
    private ProfilePagerAdapter adapter;
    private MyActivitiesFragment activities;
    private MyEventsFragment events;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        pageTitle.setText("Profile");

        userId = getIntent().getStringExtra("userId");

        numbOfTabs = pagerTitle.length;

        activities = new MyActivitiesFragment();
        events = new MyEventsFragment();

        adapter = new ProfilePagerAdapter(getSupportFragmentManager(), numbOfTabs, activities, events, Integer.parseInt(userId));

        pagerProfile.setAdapter(adapter);
        pagerProfile.setOffscreenPageLimit(numbOfTabs);
        tabLayout.setupWithViewPager(pagerProfile);

        pagerProfile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    txtActivities.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    txtEvents.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                }else {
                    txtActivities.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    txtEvents.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        GetMemberInfo();
    }

    private void GetMemberInfo(){
        Call<BHUserDetail> call = apiServiceBH.MemberProfile(GlobalVariable.getUserId(getApplicationContext()), userId);
        call.enqueue(new Callback<BHUserDetail>() {
            @Override
            public void onResponse(Call<BHUserDetail> call, Response<BHUserDetail> response) {
                BHUserDetail user = response.body();
                if (!user.isError()){
                    nameUser.setText(user.data[0].user_name);
                    username.setText(user.data[0].user_screename);
                    follower.setText("" + user.data[0].followers_count);
                    following.setText("" + user.data[0].following_count);

                    if (user.data[0].is_following){
                        txtFollow.setText("Following");
                    }else {
                        txtFollow.setText("Follow");
                    }

                    Glide.with(getApplicationContext())
                            .load(user.data[0].user_photo)
                            .crossFade()
                            .into(imageUser);
                }
            }

            @Override
            public void onFailure(Call<BHUserDetail> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.back, R.id.imageUser, R.id.btnFollow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                exitByBackByPresses();
                break;
            case R.id.btnFollow:
                break;
        }
    }
}
