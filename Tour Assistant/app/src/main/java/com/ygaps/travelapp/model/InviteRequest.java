package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteRequest {

    @SerializedName("tourId")
    @Expose
    private String tourId;
    @SerializedName("invitedUserId")
    @Expose
    private String invitedUserId=null;
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
