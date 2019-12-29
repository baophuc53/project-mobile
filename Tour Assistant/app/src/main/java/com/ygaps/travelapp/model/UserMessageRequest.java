package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMessageRequest {
    @SerializedName("tourId")
    @Expose
    String tourId;
    @SerializedName("userId")
    @Expose
    String userId;
    @SerializedName("noti")
    String noti;
    public UserMessageRequest(String tourId,String userId,String noti){
        this.tourId=tourId;
        this.userId=userId;
        this.noti=noti;
    }
}
