package com.ygaps.travelapp.Api;


import com.ygaps.travelapp.model.ChangePasswordRequest;
import com.ygaps.travelapp.model.ChangePasswordResponse;
import com.ygaps.travelapp.model.CreateTourRequest;
import com.ygaps.travelapp.model.CreateTourResponse;
import com.ygaps.travelapp.model.DetailServiceResponse;
import com.ygaps.travelapp.model.FeedbackServiceRequest;
import com.ygaps.travelapp.model.InviteRequest;
import com.ygaps.travelapp.model.InviteResponse;
import com.ygaps.travelapp.model.ListTourResponse;
import com.ygaps.travelapp.model.LoginByFaceRequest;
import com.ygaps.travelapp.model.LoginByFaceResponse;
import com.ygaps.travelapp.model.LoginRequest;
import com.ygaps.travelapp.model.LoginResponse;
import com.ygaps.travelapp.model.PointServiceResponse;
import com.ygaps.travelapp.model.RegisterRequest;
import com.ygaps.travelapp.model.RegisterResponse;
import com.ygaps.travelapp.model.SearchUserResponse;
import com.ygaps.travelapp.model.SendCmtRequest;
import com.ygaps.travelapp.model.StopPointRequest;
import com.ygaps.travelapp.model.StopPointResponse;
import com.ygaps.travelapp.model.SuggestStopPointRequest;
import com.ygaps.travelapp.model.SuggestStopPointResponse;
import com.ygaps.travelapp.model.TokenRequest;
import com.ygaps.travelapp.model.TourInfoResponse;
import com.ygaps.travelapp.model.UpdateAvtResponse;
import com.ygaps.travelapp.model.UpdateUserInfoRequest;
import com.ygaps.travelapp.model.UpdateUserInfoResponse;
import com.ygaps.travelapp.model.UserInfoResponse;

import retrofit.Callback;
import retrofit.http.Body;
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

    @POST("/tour/suggested-destination-list")
    void suggestStopPoint(@Body SuggestStopPointRequest suggestStopPointRequest,
                          Callback<SuggestStopPointResponse> callback);
                          
    @GET("/tour/info")
    void getTourInfo(@Query("tourId") Number tourId,
                     Callback<TourInfoResponse> callback);

    @POST("/tour/comment")
    void sendCmttoTour(@Body SendCmtRequest sendCmtRequest,
                       Callback<StopPointResponse> callback);

    @GET("/tour/get/service-detail")
    void getDetailService(@Query("serviceId") int id, Callback<DetailServiceResponse> callback);

    @GET("/tour/get/feedback-point-stats")
    void getFeedbackPoint(@Query("serviceId") int id, Callback<PointServiceResponse> callback);

    @POST("/tour/add/feedback-service")
    void sendFeedbackService(@Body FeedbackServiceRequest feedbackServiceRequest, Callback<StopPointResponse> callback);

    @GET("/user/info")
    void getUserInfo(Callback<UserInfoResponse> callback);

    @POST("/user/edit-info")
    void updateUserInfo(@Body UpdateUserInfoRequest userInfoRequest,
                       Callback<UpdateUserInfoResponse> callback);

    @POST("/user/update-password")
    void changePassword(@Body ChangePasswordRequest changePasswordRequest,
                        Callback<ChangePasswordResponse> callback);

    @POST("/tour/add/member")
    void inviteMember(@Body InviteRequest inviteRequest, Callback<InviteResponse> callback);

    @GET("/user/search")
    void searchUser(@Query("searchKey") String searchKey,
                    @Query("pageIndex") int pageIndex,
                    @Query("pageSize") String pageSize,
                    Callback<SearchUserResponse> callback);

    @POST("/user/notification/put-token")
    void RegisterToken(@Body TokenRequest tokenRequest, Callback<String> callback);
}