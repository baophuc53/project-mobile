package com.ygaps.travelapp.model;

import com.google.gson.annotations.SerializedName;

public class ListTourRequest {
    @SerializedName("rowPerPage")
    private Number rowPerPage;
    @SerializedName("pageNum")
    private Number pageNum;

    @SerializedName("orderBy")
    private String orderBy="name";
    @SerializedName("isDesc")
    public boolean isDesc=false;

    public void setRowPerPage(Number rowPerPage) {
        this.rowPerPage = rowPerPage;
    }

    public void setPageNum(Number pageNum) {
        this.pageNum = pageNum;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Number getRowPerPage() {
        return rowPerPage;
    }

    public Number getPageNum() {
        return pageNum;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
