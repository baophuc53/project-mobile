package com.example.tourassistant.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailTourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Detail Tour");
    }
}
