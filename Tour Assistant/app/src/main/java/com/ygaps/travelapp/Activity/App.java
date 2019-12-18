package com.ygaps.travelapp.Activity;

import android.app.Application;

import com.facebook.appevents.AppEventsLogger;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(getApplicationContext());
    }
}