package com.example.tourassistant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.tourassistant.Object.Tour;
import com.example.tourassistant.adapter.TourAdapters;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Tour tour;
    ArrayList<Tour> toursList = new ArrayList<Tour>();
    TourAdapters tourAdapters;
    ListView lvTours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //title vao giua
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        addControls();
        Show();
    }

    private void Show() {
        tour = new Tour("HCM - Nha Trang",  10, 15, 0, 1, 2, 3, false, 10, 5,10000, 50000, "https://cdnimg.vietnamplus.vn/uploaded/fsmsr/2018_12_03/31.jpg");
        toursList.add(tour);
        tourAdapters = new TourAdapters(MainActivity.this, R.layout.listview_tour_layout, toursList);
        lvTours.setAdapter(tourAdapters);
    }

    private void addControls() {
        lvTours = (ListView) findViewById(R.id.listTour);
    }
}
