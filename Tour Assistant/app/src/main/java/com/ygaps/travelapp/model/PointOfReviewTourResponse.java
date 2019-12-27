package com.ygaps.travelapp.model;

import android.graphics.Point;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ygaps.travelapp.Object.PointStat;
import com.ygaps.travelapp.Object.Tour;

import java.util.List;

public class PointOfReviewTourResponse {
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
