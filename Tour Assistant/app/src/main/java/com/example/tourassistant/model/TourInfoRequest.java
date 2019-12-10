package com.example.tourassistant.model;

import com.google.gson.annotations.SerializedName;

public class TourInfoRequest {
    @SerializedName("tourId")
    private Number tourId;

    public Number getTourId() {
        return tourId;
    }

    public void setTourId(Number tourId) {
        this.tourId = tourId;
    }
}
