package com.example.tourassistant.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StopPoint implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("arrivalAt")
    @Expose
    private Long arrivalAt;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("leaveAt")
    @Expose
    private Long leaveAt;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("maxCost")
    @Expose
    private Long maxCost;
    @SerializedName("minCost")
    @Expose
    private Long minCost;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("serviceTypeId")
    @Expose
    private int serviceTypeId;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public Long getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(Long arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Long getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(Long leaveAt) {
        this.leaveAt = leaveAt;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public Long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Long maxCost) {
        this.maxCost = maxCost;
    }

    public Long getMinCost() {
        return minCost;
    }

    public void setMinCost(Long minCost) {
        this.minCost = minCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double get_long() {
        return _long;
    }

    public void set_long(Double _long) {
        this._long = _long;
    }

}