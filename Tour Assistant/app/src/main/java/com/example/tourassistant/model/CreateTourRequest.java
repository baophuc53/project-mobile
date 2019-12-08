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
        private double sourceLat;
        @SerializedName("sourceLong")
        @Expose
        private double sourceLong;
        @SerializedName("desLat")
        @Expose
        private double desLat;
        @SerializedName("desLong")
        @Expose
        private double desLong;
        @SerializedName("isPrivate")
        @Expose
        private Boolean isPrivate;
        @SerializedName("avatar")
        @Expose
        private String avatar;


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

        public double getSourceLat() {
            return sourceLat;
        }

        public void setSourceLat(double sourceLat) {
            this.sourceLat = sourceLat;
        }

        public double getSourceLong() {
            return sourceLong;
        }

        public void setSourceLong(double sourceLong) {
            this.sourceLong = sourceLong;
        }

        public double getDesLat() {
            return desLat;
        }

        public void setDesLat(double desLat) {
            this.desLat = desLat;
        }

        public double getDesLong() {
            return desLong;
        }

        public void setDesLong(double desLong) {
            this.desLong = desLong;
        }

        public Boolean getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(Boolean isPrivate) {
            this.isPrivate = isPrivate;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
}