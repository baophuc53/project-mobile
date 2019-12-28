package com.ygaps.travelapp.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Noti {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("notificationType")
    @Expose
    private Integer notificationType;
    @SerializedName("speed")
    @Expose
    private Integer speed;
    @SerializedName("createdByTourId")
    @Expose
    private Integer createdByTourId;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Integer getCreatedByTourId() {
        return createdByTourId;
    }

    public void setCreatedByTourId(Integer createdByTourId) {
        this.createdByTourId = createdByTourId;
    }

}
