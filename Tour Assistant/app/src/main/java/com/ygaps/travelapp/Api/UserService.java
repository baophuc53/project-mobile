package com.ygaps.travelapp.Api;


import com.ygaps.travelapp.Object.CoordMember;
import com.ygaps.travelapp.model.ChangePasswordRequest;
import com.ygaps.travelapp.model.ChangePasswordResponse;
import com.ygaps.travelapp.model.CreateTourRequest;
import com.ygaps.travelapp.model.CreateTourResponse;
import com.ygaps.travelapp.model.DefaultResponse;
import com.ygaps.travelapp.model.DetailServiceResponse;
import com.ygaps.travelapp.model.FeedbackServiceRequest;
import com.ygaps.travelapp.model.GPSServiceRequest;
import com.ygaps.travelapp.model.GetListFeedbackServiceResponse;
import com.ygaps.travelapp.model.GetNotiResponse;
import com.ygaps.travelapp.model.InviteRequest;
import com.ygaps.travelapp.model.JoinRequest;
import com.ygaps.travelapp.model.ListReviewOfTourResponse;
import com.ygaps.travelapp.model.ListTourResponse;
import com.ygaps.travelapp.model.LoginByFaceRequest;
import com.ygaps.travelapp.model.LoginByFaceResponse;
import com.ygaps.travelapp.model.LoginRequest;
import com.ygaps.travelapp.model.LoginResponse;
import com.ygaps.travelapp.model.OnRoadNotification;
import com.ygaps.travelapp.model.PointOfReviewTourResponse;
import com.ygaps.travelapp.model.PointServiceResponse;
import com.ygaps.travelapp.model.ProcessInvitationRequest;
import com.ygaps.travelapp.model.RegisterRequest;
import com.ygaps.travelapp.model.RegisterResponse;
import com.ygaps.travelapp.model.SearchUserResponse;
import com.ygaps.travelapp.model.SendCmtRequest;
import com.ygaps.travelapp.model.SendOTPRequest;
import com.ygaps.travelapp.model.SendOTPResponse;
import com.ygaps.travelapp.model.SendReviewTourRequest;
import com.ygaps.travelapp.model.SendReviewTourResponse;
import com.ygaps.travelapp.model.StopPointRequest;
import com.ygaps.travelapp.model.StopPointResponse;
import com.ygaps.travelapp.model.SuggestStopPointRequest;
import com.ygaps.travelapp.model.SuggestStopPointResponse;
import com.ygaps.travelapp.model.TokenRequest;
import com.ygaps.travelapp.model.TourInfoResponse;
import com.ygaps.travelapp.model.UpdateAvtResponse;
import com.ygaps.travelapp.model.UpdateTourResponse;
import com.ygaps.travelapp.model.UpdateTourResquest;
import com.ygaps.travelapp.model.UpdateUserInfoRequest;
import com.ygaps.travelapp.model.UpdateUserInfoResponse;
import com.ygaps.travelapp.model.UserInfoResponse;
import com.ygaps.travelapp.model.VerifyOTPtoChangePassRequest;
import com.ygaps.travelapp.model.VerifyOTPtoChangePassResponse;

import java.util.List;

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

    @GET("/tour/get/feedback-service")
    void getFeedbackService(@Query("serviceId") int id,
                            @Query("pageIndex") int index,
                            @Query("pageSize") int size,
                            Callback<GetListFeedbackServiceResponse> callback);

    @GET("/user/info")
    void getUserInfo(Callback<UserInfoResponse> callback);

    @POST("/user/edit-info")
    void updateUserInfo(@Body UpdateUserInfoRequest userInfoRequest,
                       Callback<UpdateUserInfoResponse> callback);

    @POST("/user/update-password")
    void changePassword(@Body ChangePasswordRequest changePasswordRequest,
                        Callback<ChangePasswordResponse> callback);

    @POST("/tour/add/member")
    void inviteMember(@Body InviteRequest inviteRequest, Callback<DefaultResponse> callback);

    @POST("/tour/add/member")
    void joinTour(@Body JoinRequest joinRequest, Callback<DefaultResponse> callback);

    @GET("/user/search")
    void searchUser(@Query("searchKey") String searchKey,
                    @Query("pageIndex") int pageIndex,
                    @Query("pageSize") String pageSize,
                    Callback<SearchUserResponse> callback);

    @POST("/user/notification/put-token")
    void RegisterToken(@Body TokenRequest tokenRequest, Callback<DefaultResponse> callback);


    @GET("/tour/remove-stop-point")
    void RemoveStopPoint(@Query("stopPointId") int id,
                         Callback<StopPointResponse> callback);

    @POST("/tour/response/invitation")
    void ResponseInvitation(@Body ProcessInvitationRequest processInvitationRequest,Callback<DefaultResponse> callback);

    @POST("/user/request-otp-recovery")
    void sendOTP(@Body SendOTPRequest sendOTPRequest,
                       Callback<SendOTPResponse> callback);

    @POST("/user//user/verify-otp-recovery")
    void verifyOTP(@Body VerifyOTPtoChangePassRequest verifyOTPtoChangePassRequest,
                 Callback<VerifyOTPtoChangePassResponse> callback);

    @GET("/tour/get/review-point-stats")
    void getTourReviewPoint(@Query("tourId") Long id, Callback<PointOfReviewTourResponse> callback);

    @GET("/tour/get/review-list")
    void getListReviews(@Query("tourId") Number tourId,
                        @Query("pageIndex") Number pageIndex,
                        @Query("pageSize") Number pageSize,
                        Callback<ListReviewOfTourResponse> callback);

    @POST("/tour/add/review")
    void sendReview(@Body SendReviewTourRequest sendReviewTourRequest,
                   Callback<SendReviewTourResponse> callback);

    @POST("/tour/update-tour")
    void updateTourInfo(@Body UpdateTourResquest updateTourResquest,
                    Callback<UpdateTourResponse> callback);

    @POST("/tour/add/notification-on-road")
    void notificationOnRoad(@Body OnRoadNotification onRoadNotification,Callback<DefaultResponse> callback);

    @GET("/tour/get/noti-on-road")
    void getNotiByTourId(@Query("tourId") Number tourId,
                         @Query("pageIndex") Number pageIndex,
                         @Query("pageSize") Number pageSize,
                         Callback<GetNotiResponse> callback);

    @POST("/tour/current-users-coordinate")
    void getUserCooord(@Body GPSServiceRequest gpsServiceRequest,
                       Callback<List<CoordMember>> callback);
}