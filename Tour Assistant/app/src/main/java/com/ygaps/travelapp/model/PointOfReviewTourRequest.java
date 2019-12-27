package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointOfReviewTourRequest {
    @SerializedName("tourId")
    @Expose
    private String tourId;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
}
