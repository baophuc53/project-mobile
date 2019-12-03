package com.example.tourassistant.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.tourassistant.Object.Member;
import com.example.tourassistant.Object.StopPoint;
import com.example.tourassistant.adapter.MemberAdapters;
import com.example.tourassistant.adapter.StopPointAdapters;

import java.util.ArrayList;
import java.util.List;

public class ListStopPointActivity extends AppCompatActivity {

    ListView lvStopPoints;
    StopPointAdapters stopPointAdapters;
    SearchView search;
    TextView NoStopPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stop_point);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView  title = findViewById(R.id.actionbar_textview);
        title.setText("List Stop Point of Tour");
        addControl();
        Show();
        addSeach();
    }

    private void addSeach() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                stopPointAdapters.filterTag(text);
                return false;
            }
        });
    }

    private void Show() {
            Intent intent = getIntent();
            boolean isnull = intent.getBooleanExtra("isNull", true);
            if (isnull == false) {
                List<StopPoint> listStopPoint = (List<StopPoint>) intent.getSerializableExtra("StopPointList");
                stopPointAdapters = new StopPointAdapters(ListStopPointActivity.this, R.layout.items_list_stop_point,
                        (ArrayList<StopPoint>) listStopPoint);
                lvStopPoints.setAdapter(stopPointAdapters);
            }
            else{
                NoStopPoint.setText("No stop point");
                NoStopPoint.setVisibility(View.VISIBLE);
            }
    }

    private void addControl() {
        lvStopPoints = findViewById(R.id.list_stop_point);
        search = findViewById(R.id.search_stopPoint);
        NoStopPoint = findViewById(R.id.noStopPointTv);
    }
}
