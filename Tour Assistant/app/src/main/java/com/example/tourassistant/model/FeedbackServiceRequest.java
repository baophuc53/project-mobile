package com.example.tourassistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackServiceRequest {
    @SerializedName("serviceId")
    @Expose
    private Integer serviceId = 0;
    @SerializedName("feedback")
    @Expose
    private String feedback = "";
    @SerializedName("point")
    @Expose
    private Integer point = 0;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
