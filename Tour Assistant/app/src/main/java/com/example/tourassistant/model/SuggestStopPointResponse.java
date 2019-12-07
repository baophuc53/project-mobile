package com.example.tourassistant.model;

import com.example.tourassistant.Object.SuggestStopPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestStopPointResponse {
    @SerializedName("stopPoints")
    @Expose
    private List<SuggestStopPoint> stopPoints = null;

    public List<SuggestStopPoint> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<SuggestStopPoint> stopPoints) {
        this.stopPoints = stopPoints;
    }

}
