package com.ygaps.travelapp.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoordMember {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double _long;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }
}
