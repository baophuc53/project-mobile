package com.example.tourassistant.Api;


import com.example.tourassistant.model.ListTourRequest;
import com.example.tourassistant.model.ListTourResponse;
import com.example.tourassistant.model.RegisterRequest;
import com.example.tourassistant.model.RegisterResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface UserService {
    @POST("/user/register/")
    void login(@Body RegisterRequest request, Callback<RegisterResponse> callback);

    @GET("/tour/list")
    void getListTour(@Query("rowPerPage") Number rowPerPage,
                     @Query("pageNum") Number pageNum,
                     @Query("orderBy") String orderBy,
                     @Query("isDesc") Boolean isDesc,
                     Callback<ListTourResponse> callback);


}