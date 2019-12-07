package com.example.tourassistant.Api;


import android.telecom.Call;

import com.example.tourassistant.Object.StopPoint;
import com.example.tourassistant.model.CreateTourRequest;
import com.example.tourassistant.model.CreateTourResponse;
import com.example.tourassistant.model.ListTourResponse;
import com.example.tourassistant.model.LoginByFaceRequest;
import com.example.tourassistant.model.LoginByFaceResponse;
import com.example.tourassistant.model.LoginRequest;
import com.example.tourassistant.model.LoginResponse;
import com.example.tourassistant.model.RegisterRequest;
import com.example.tourassistant.model.RegisterResponse;
import com.example.tourassistant.model.SendCmtRequest;
import com.example.tourassistant.model.StopPointRequest;
import com.example.tourassistant.model.StopPointResponse;
import com.example.tourassistant.model.SuggestStopPointRequest;
import com.example.tourassistant.model.SuggestStopPointResponse;
import com.example.tourassistant.model.TourInfoResponse;
import com.example.tourassistant.model.UpdateAvtRequest;
import com.example.tourassistant.model.UpdateAvtResponse;
import com.example.tourassistant.model.UserInfoResponse;

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

    @POST("/tour/set-stop-points")
    void addStopPoints(@Body StopPointRequest stopPointRequest,
                       Callback<StopPointResponse> callback);

    @GET("/tour/info")
    void getTourInfo(@Query("tourId") Number tourId,
                     Callback<TourInfoResponse> callback);

    @POST("/tour/comment")
    void sendCmttoTour(@Body SendCmtRequest sendCmtRequest,
                       Callback<StopPointResponse> callback);

    @GET("/user/info")
    void getUserInfo(Callback<UserInfoResponse> callback);

}