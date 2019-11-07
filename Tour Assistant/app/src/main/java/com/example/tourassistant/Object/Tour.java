package com.example.tourassistant.Object;

import java.io.Serializable;

public class Tour implements Serializable {
    private String name;
    private Number starDate;
    private Number endDate;
    private Number sourceLat;
    private Number sourceLong;
    private Number desLat;
    private Number desLong;
    private Boolean isPrivate;
    private Number adults;
    private Number childs;
    private Number minCost;
    private Number maxCost;
    private String Avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getStarDate() {
        return starDate;
    }

    public void setStarDate(Number starDate) {
        this.starDate = starDate;
    }

    public Number getEndDate() {
        return endDate;
    }

    public void setEndDate(Number endDate) {
        this.endDate = endDate;
    }

    public Number getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(Number sourceLat) {
        this.sourceLat = sourceLat;
    }

    public Number getSourceLong() {
        return sourceLong;
    }

    public void setSourceLong(Number sourceLong) {
        this.sourceLong = sourceLong;
    }

    public Number getDesLat() {
        return desLat;
    }

    public void setDesLat(Number desLat) {
        this.desLat = desLat;
    }

    public Number getDesLong() {
        return desLong;
    }

    public void setDesLong(Number desLong) {
        this.desLong = desLong;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Number getAdults() {
        return adults;
    }

    public void setAdults(Number adults) {
        this.adults = adults;
    }

    public Number getChilds() {
        return childs;
    }

    public void setChilds(Number childs) {
        this.childs = childs;
    }

    public Number getMinCost() {
        return minCost;
    }

    public void setMinCost(Number minCost) {
        this.minCost = minCost;
    }

    public Number getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(Number maxCost) {
        this.maxCost = maxCost;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public Tour(String name, Number starDate, Number endDate, Number sourceLat, Number sourceLong, Number desLat, Number desLong, Boolean isPrivate, Number adults, Number childs, Number minCost, Number maxCost, String avatar) {
        this.name = name;
        this.starDate = starDate;
        this.endDate = endDate;
        this.sourceLat = sourceLat;
        this.sourceLong = sourceLong;
        this.desLat = desLat;
        this.desLong = desLong;
        this.isPrivate = isPrivate;
        this.adults = adults;
        this.childs = childs;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.Avatar = avatar;
    }

    public Tour() {
    }

}
