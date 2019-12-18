package com.ygaps.travelapp.model;

import java.io.Serializable;
import java.util.List;

import com.ygaps.travelapp.Object.Tour;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListTourResponse implements Serializable {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("tours")
    @Expose
    private List<Tour> tours = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

}