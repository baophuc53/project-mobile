package com.example.tourassistant.Api;

import android.text.TextUtils;


import com.example.tourassistant.Activity.Constants;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;


public class MyAPIClient {
    private static MyAPIClient instance;

    private RestAdapter adapter;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    private MyAPIClient(final String Header) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", Header);
            }
        };
        adapter = new RestAdapter.Builder()
                .setEndpoint(Constants.APIEndpoint)
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    public RestAdapter getAdapter() {
        return adapter;
    }

    public static MyAPIClient getInstance(String Header) {
        if (instance == null)
            instance = new MyAPIClient(Header);
        return instance;
    }

    public static MyAPIClient getInstance() {
        if (instance == null)
            instance = new MyAPIClient(null);
        return instance;
    }
}
