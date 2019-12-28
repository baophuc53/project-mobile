package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ygaps.travelapp.Object.SuggestStopPoint;

import java.util.List;

public class SearchStopPointResponse {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("stopPoints")
    @Expose
    private List<SuggestStopPoint> stopPoints = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<SuggestStopPoint> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<SuggestStopPoint> stopPoints) {
        this.stopPoints = stopPoints;
    }
}
