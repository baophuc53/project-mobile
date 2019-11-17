package com.example.tourassistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTourRequest {

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
    @SerializedName("adults")
    @Expose
    private Long adults;
    @SerializedName("childs")
    @Expose
    private Long childs;
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
    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;

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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

}