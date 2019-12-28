package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ygaps.travelapp.Object.Noti;

import java.util.List;

public class GetNotiResponse {
    @SerializedName("notiList")
    @Expose
    List<Noti>  notifications;

    public List<Noti> getNotifications() {
        return notifications;
    }
}

