package com.example.tourassistant.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour);
        SharedPreferences sharedPreferences=getSharedPreferences("Data",0);
        String token=sharedPreferences.getString("token","");
        if(token.isEmpty())
            intent=new Intent(MainActivity.this,LoginActivity.class);
        else intent=new Intent(MainActivity.this,ListTourActivity.class);
        startActivity(intent);
        finish();
    }
}
