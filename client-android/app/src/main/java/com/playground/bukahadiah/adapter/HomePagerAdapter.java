package com.playground.bukahadiah.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.playground.bukahadiah.fragment.AddNewFragment;
import com.playground.bukahadiah.fragment.EventsFragment;
import com.playground.bukahadiah.fragment.HomeFragment;
import com.playground.bukahadiah.fragment.ProfileFragment;
import com.playground.bukahadiah.fragment.SearchFragment;

/**
 * Created by aderifaldi on 14/09/2016.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private HomeFragment home;
    private SearchFragment search;
    private AddNewFragment addNew;
    private EventsFragment heart;
    private ProfileFragment profile;

    public HomePagerAdapter(FragmentManager fm, int numOfTabs, HomeFragment home,
                            SearchFragment search, AddNewFragment addNew, EventsFragment heart,
                            ProfileFragment profile) {
        super(fm);
        this.numOfTabs = numOfTabs;

        this.home = home;
        this.search = search;
        this.addNew = addNew;
        this.heart = heart;
        this.profile = profile;

    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0){
            return home;
        } else if (position == 1){
            return heart;
        }else if (position == 2){
            return addNew;
        }else if (position == 3){
            return search;
        }else {
            return profile;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
