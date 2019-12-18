package com.ygaps.travelapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ygaps.travelapp.Activity.Fragments.GeneralFragment;
import com.ygaps.travelapp.Activity.Fragments.ReviewsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNoOfTabs;
    int id;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs, int id) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.id = id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        switch (position) {
            case 0:
                GeneralFragment generalFragment = new GeneralFragment();
                generalFragment.setArguments(args);
                return generalFragment;

            case 1:
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                reviewsFragment.setArguments(args);
                return reviewsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
