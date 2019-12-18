package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import com.ygaps.travelapp.Activity.Fragments.GeneralFragment;
import com.ygaps.travelapp.Activity.Fragments.ReviewsFragment;
import com.ygaps.travelapp.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class PointInfoActivity extends AppCompatActivity implements GeneralFragment.OnFragmentInteractionListener, ReviewsFragment.OnFragmentInteractionListener {

    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_point_info);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("General"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        final ViewPager viewPager = findViewById(R.id.pager);
        final com.ygaps.travelapp.adapter.PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), id);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
