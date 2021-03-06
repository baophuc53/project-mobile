package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinRequest {

    @SerializedName("tourId")
    @Expose
    private String tourId;
    @SerializedName("invitedUserId")
    @Expose
    private String invitedUserId="1";
    @SerializedName("isInvited")
    @Expose
    public Boolean isInvited;

    public String getTourId() {
        return tourId;
    }

    public String getInvitedUserId() {
        return invitedUserId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public void setInvitedUserId(String invitedUserId) {
        this.invitedUserId = invitedUserId;
    }
}
