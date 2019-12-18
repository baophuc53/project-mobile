package com.ygaps.travelapp.model;

import com.google.gson.annotations.SerializedName;

public class TokenRequest {
    @SerializedName("fcmToken")
    private String fcmToken;
    @SerializedName("deviceId")
    private String devideId;
    @SerializedName("platform")
    private int platform=1;
    @SerializedName("appVersion")
    private String appVersion="1.0.0";

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDevideId() {
        return devideId;
    }

    public void setDevideId(String devideId) {
        this.devideId = devideId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
