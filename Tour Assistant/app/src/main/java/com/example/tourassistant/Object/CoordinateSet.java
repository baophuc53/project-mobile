package com.example.tourassistant.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoordinateSet {
    @SerializedName("coordinateSet")
    @Expose
    private List<Coord> coordinateSet = null;

    public List<Coord> getCoordinateSet() {
        return coordinateSet;
    }

    public void setCoordinateSet(List<Coord> coordinateSet) {
        this.coordinateSet = coordinateSet;
    }
}
