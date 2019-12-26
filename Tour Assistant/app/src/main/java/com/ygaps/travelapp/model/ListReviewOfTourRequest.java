package com.ygaps.travelapp.model;

import com.google.gson.annotations.SerializedName;

public class ListReviewOfTourRequest {
    @SerializedName("tourId")
    private Number tourId;
    @SerializedName("pageIndex")
    private Number pageIndex;

    @SerializedName("pageSize")
    private Number pageSize;

    public Number getTourId() {
        return tourId;
    }

    public void setTourId(Number tourId) {
        this.tourId = tourId;
    }

    public Number getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Number pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Number getPageSize() {
        return pageSize;
    }

    public void setPageSize(Number pageSize) {
        this.pageSize = pageSize;
    }
}
