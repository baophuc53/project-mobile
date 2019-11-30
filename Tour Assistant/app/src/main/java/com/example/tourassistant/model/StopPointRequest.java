package com.example.tourassistant.model;

import com.example.tourassistant.Object.StopPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopPointRequest {
    @SerializedName("tourId")
    @Expose
    private Integer tourId;
    @SerializedName("stopPoints")
    @Expose
    private List<StopPoint> stopPoints = null;

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public List<StopPoint> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<StopPoint> stopPoints) {
        this.stopPoints = stopPoints;
    }
}
