package com.example.tourassistant.Api;


import com.example.tourassistant.model.LoginRequest;
import com.example.tourassistant.model.LoginResponse;
import com.example.tourassistant.model.RegisterRequest;
import com.example.tourassistant.model.RegisterResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface UserService {
    @POST("/user/register/")
    void register(@Body RegisterRequest request, Callback<RegisterResponse> callback);

    @POST("/user/login")
    void login(@Body LoginRequest request, Callback<LoginResponse> callback);

}