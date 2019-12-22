package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ygaps.travelapp.Object.FeedbackList;

import java.util.List;

public class GetListFeedbackServiceResponse {
    @SerializedName("feedbackList")
    @Expose
    private List<FeedbackList> feedbackList = null;

    public List<FeedbackList> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<FeedbackList> feedbackList) {
        this.feedbackList = feedbackList;
    }

}
