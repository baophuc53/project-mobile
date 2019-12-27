package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ygaps.travelapp.Object.ReviewTour;
import com.ygaps.travelapp.Object.Tour;

import java.util.List;

public class ListReviewOfTourResponse {
    @SerializedName("reviewList")
    @Expose
    private List<ReviewTour> reviews = null;

    public List<ReviewTour> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewTour> reviews) {
        this.reviews = reviews;
    }
}
