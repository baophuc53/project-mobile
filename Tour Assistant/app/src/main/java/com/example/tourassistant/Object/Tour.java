package com.example.tourassistant.Object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tour {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("minCost")
    @Expose
    private Long minCost;
    @SerializedName("maxCost")
    @Expose
    private Long maxCost;
    @SerializedName("startDate")
    @Expose
    private Long startDate;
    @SerializedName("endDate")
    @Expose
    private Long endDate;
    @SerializedName("sourceLat")
    @Expose
    private Long sourceLat;
    @SerializedName("sourceLong")
    @Expose
    private Long sourceLong;
    @SerializedName("desLat")
    @Expose
    private Long desLat;
    @SerializedName("desLong")
    @Expose
    private Long desLong;
    @SerializedName("adults")
    @Expose
    private Long adults;
    @SerializedName("childs")
    @Expose
    private Long childs;
    @SerializedName("isPrivate")
    @Expose
    public Boolean isPrivate=false;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMinCost() {
        return minCost;
    }

    public void setMinCost(Long minCost) {
        this.minCost = minCost;
    }

    public Long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Long maxCost) {
        this.maxCost = maxCost;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(Long sourceLat) {
        this.sourceLat = sourceLat;
    }

    public Long getSourceLong() {
        return sourceLong;
    }

    public void setSourceLong(Long sourceLong) {
        this.sourceLong = sourceLong;
    }

    public Long getDesLat() {
        return desLat;
    }

    public void setDesLat(Long desLat) {
        this.desLat = desLat;
    }

    public Long getDesLong() {
        return desLong;
    }

    public void setDesLong(Long desLong) {
        this.desLong = desLong;
    }

    public Long getAdults() {
        return adults;
    }

    public void setAdults(Long adults) {
        this.adults = adults;
    }

    public Long getChilds() {
        return childs;
    }

    public void setChilds(Long childs) {
        this.childs = childs;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}