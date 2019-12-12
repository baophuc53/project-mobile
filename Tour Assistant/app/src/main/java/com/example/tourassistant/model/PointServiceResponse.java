package com.example.tourassistant.model;

import com.example.tourassistant.Object.PointStat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointServiceResponse {
    @SerializedName("pointStats")
    @Expose
    private List<PointStat> pointStats = null;

    public List<PointStat> getPointStats() {
        return pointStats;
    }

    public void setPointStats(List<PointStat> pointStats) {
        this.pointStats = pointStats;
    }
}