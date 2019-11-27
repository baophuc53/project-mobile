package com.example.tourassistant.Api;


import com.example.tourassistant.model.CreateTourRequest;
import com.example.tourassistant.model.CreateTourResponse;
import com.example.tourassistant.model.ListTourResponse;
import com.example.tourassistant.model.LoginByFaceRequest;
import com.example.tourassistant.model.LoginByFaceResponse;
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
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

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

    @POST("/user/login/by-facebook")
    void loginByFacebook(@Body LoginByFaceRequest loginByFaceRequest,
                         Callback<LoginByFaceResponse> callback);

    @GET("/tour/history-user")
    void getUserTour(@Query("pageIndex") Number pageIndex,
                     @Query("pageSize") String pageSize,
                     Callback<ListTourResponse> callback);

    @POST("/tour/create")
    void createTour(@Body CreateTourRequest createTourRequest,
                    Callback<CreateTourResponse> callback);

    @Multipart
    @POST("/tour/update/avatar-for-tour")
    void updateAvatarTour(@Part("file") TypedFile file, @Part("tourId") String id,
                          Callback<UpdateAvtResponse> callback);
}