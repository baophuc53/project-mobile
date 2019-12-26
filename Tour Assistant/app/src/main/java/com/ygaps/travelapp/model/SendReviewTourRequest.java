package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendReviewTourRequest {
    @SerializedName("tourId")
    @Expose
    private Number tourId;
    @SerializedName("point")
    @Expose
    private Number point;
    @SerializedName("review")
    @Expose
    private String review;

    public Number getTourId() {
        return tourId;
    }

    public void setTourId(Number tourId) {
        this.tourId = tourId;
    }

    public Number getPoint() {
        return point;
    }

    public void setPoint(Number point) {
        this.point = point;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
