package com.example.tourassistant.Api;


import com.example.tourassistant.model.CreateTourRequest;
import com.example.tourassistant.model.CreateTourResponse;
import com.example.tourassistant.model.ListTourResponse;
import com.example.tourassistant.model.LoginRequest;
import com.example.tourassistant.model.LoginResponse;
import com.example.tourassistant.model.RegisterRequest;
import com.example.tourassistant.model.RegisterResponse;
import com.example.tourassistant.model.UpdateAvtRequest;
import com.example.tourassistant.model.UpdateAvtResponse;

import java.io.File;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface UserService {
    @POST("/user/register/")
    void register(@Body RegisterRequest request, Callback<RegisterResponse> callback);


    @POST("/user/login")
    void login(@Body LoginRequest request, Callback<LoginResponse> callback);

    @GET("/tour/list")
    void getListTour(@Query("rowPerPage") Number rowPerPage,
                     @Query("pageNum") Number pageNum,
                     @Query("orderBy") String orderBy,
                     @Query("isDesc") Boolean isDesc,
                     Callback<ListTourResponse> callback);

    @POST("/tour/create")
    void createTour(@Body CreateTourRequest createTourRequest,
                    Callback<CreateTourResponse> callback);

    @POST("/tour/update/avatar-for-tour")
    void updateAvatarTour(@Body UpdateAvtRequest updateAvtRequest,
                          Callback<UpdateAvtResponse> callback);
}