package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("emailVerified")
    @Expose
    private String emailVerified="";

    @SerializedName("phoneVerified")
    @Expose
    private String phoneVerified="";

    @SerializedName("token")
    @Expose
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
