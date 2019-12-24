package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Object.NotificationObj;
import com.ygaps.travelapp.adapter.NotificationAdapter;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    NotificationAdapter adapter;
    ArrayList<NotificationObj>  objArrayList;
    ListView listNotification;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //title vao giua
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView title = findViewById(R.id.actionbar_textview);
        title.setText("Notification");

        Init();
        Authorize();
        addEvent();
    }

    private void addEvent() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                GetData();
            }
        });
    }


    private void Init() {
        objArrayList=new ArrayList<>();
        adapter=new NotificationAdapter(this, R.layout.items_notification,objArrayList);
        listNotification=findViewById(R.id.listNotification);
        sharedPreferences=getSharedPreferences("Notification",0);
        GetData();
    }

    private void GetData() {
        adapter.clear();
        int length=sharedPreferences.getInt("length",0);
        for(int i=length-1;i>=0;i--)
        {
            String json=sharedPreferences.getString("Notification"+i,"");
            NotificationObj notificationObj=new Gson().fromJson(json,NotificationObj.class);
            adapter.add(notificationObj);
        }
        listNotification.setAdapter(adapter);
    }

    private void Authorize() {
        SharedPreferences sharedPreferences = getSharedPreferences("Data", 0);
        String Token = sharedPreferences.getString("token", "");


        MyAPIClient.getInstance().setAccessToken(Token);
    }
}
