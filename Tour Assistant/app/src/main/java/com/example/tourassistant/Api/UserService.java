package com.example.tourassistant.Api;


import com.example.tourassistant.model.RegisterRequest;
import com.example.tourassistant.model.RegisterResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface UserService {
    @POST("/user/register/")
    void login(@Body RegisterRequest request, Callback<RegisterResponse> callback);

    @POST("/logout.php")
    void logout(Callback<Response> callback);

}