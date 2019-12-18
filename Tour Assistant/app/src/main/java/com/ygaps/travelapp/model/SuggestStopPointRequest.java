package com.ygaps.travelapp.model;

import com.ygaps.travelapp.Object.CoordinateSet;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestStopPointRequest {
    @SerializedName("hasOneCoordinate")
    @Expose
    private Boolean hasOneCoordinate = true;
    @SerializedName("coordList")
    @Expose
    private List<CoordinateSet> coordList = null;

    public Boolean getHasOneCoordinate() {
        return hasOneCoordinate;
    }

    public void setHasOneCoordinate(Boolean hasOneCoordinate) {
        this.hasOneCoordinate = hasOneCoordinate;
    }

    public List<CoordinateSet> getCoordList() {
        return coordList;
    }

    public void setCoordList(List<CoordinateSet> coordList) {
        this.coordList = coordList;
    }
}
