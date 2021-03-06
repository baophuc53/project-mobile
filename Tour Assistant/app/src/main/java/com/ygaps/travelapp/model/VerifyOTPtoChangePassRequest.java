package com.ygaps.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOTPtoChangePassRequest {
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("verifyCode")
    @Expose
    private String verifyCode;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
