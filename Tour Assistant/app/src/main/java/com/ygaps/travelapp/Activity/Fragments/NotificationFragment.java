package com.ygaps.travelapp.Activity.Fragments;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Object.NotificationObj;
import com.ygaps.travelapp.adapter.NotificationAdapter;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    NotificationAdapter adapter;
    ArrayList<NotificationObj>  objArrayList;
    ListView listNotification;
    Activity currentActivity;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        Init(view);
        addEvent();
        return view;
    }
    private void addEvent() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                GetData();
            }
        });
    }


    private void Init(View view) {
        currentActivity=getActivity();
        objArrayList=new ArrayList<>();
        listNotification=view.findViewById(R.id.listNotification);
        sharedPreferences=currentActivity.getSharedPreferences("Notification",0);
        GetData();
    }

    private void GetData() {
        objArrayList.clear();
        int length=sharedPreferences.getInt("length",0);
        for(int i=length-1;i>=0;i--)
        {
            String json=sharedPreferences.getString("Notification"+i,"");
            NotificationObj notificationObj=new Gson().fromJson(json,NotificationObj.class);
            objArrayList.add(notificationObj);
        }
        adapter=new NotificationAdapter(currentActivity, R.layout.items_notification,objArrayList);
        listNotification.setAdapter(adapter);
    }

}
