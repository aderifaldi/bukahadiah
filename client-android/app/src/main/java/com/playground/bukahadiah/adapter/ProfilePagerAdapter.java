package com.playground.bukahadiah.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.playground.bukahadiah.fragment.MyActivitiesFragment;
import com.playground.bukahadiah.fragment.EventsFragment;
import com.playground.bukahadiah.fragment.MyEventsFragment;

/**
 * Created by aderifaldi on 14/09/2016.
 */
public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private MyActivitiesFragment activities;
    private MyEventsFragment events;
    private int userId;

    public ProfilePagerAdapter(FragmentManager fm, int numOfTabs, MyActivitiesFragment activities,
                               MyEventsFragment events, int userId) {
        super(fm);
        this.numOfTabs = numOfTabs;

        this.activities = activities;
        this.events = events;
        this.userId = userId;

    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);

        if (position == 0){

            activities.setArguments(bundle);

            return activities;
        } else {

            events.setArguments(bundle);

            return events;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
