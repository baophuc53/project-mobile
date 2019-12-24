package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcessInvitationRequest {
    @SerializedName("tourId")
    @Expose
    private String tourId;
    @SerializedName("isAccepted")
    @Expose
    public Boolean isAccepted;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
}

