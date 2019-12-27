package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnRoadNotification {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("long")
    @Expose
    private double _long;
    @SerializedName("tourId")
    @Expose
    private long tourId;
    @SerializedName("userId")
    @Expose
    private long userId;
    @SerializedName("notificationType")
    @Expose
    private Integer notificationType;
    @SerializedName("speed")
    @Expose
    private Integer speed;
    @SerializedName("note")
    @Expose
    private String note;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return _long;
    }

    public void setLong(double _long) {
        this._long = _long;
    }

    public long getTourId() {
        return tourId;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}