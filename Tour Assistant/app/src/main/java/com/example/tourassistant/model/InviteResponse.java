package com.example.tourassistant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("minCost")
    @Expose
    private String minCost;
    @SerializedName("maxCost")
    @Expose
    private String maxCost;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("adults")
    @Expose
    private Integer adults;
    @SerializedName("childs")
    @Expose
    private Integer childs;
    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChilds() {
        return childs;
    }

    public void setChilds(Integer childs) {
        this.childs = childs;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
