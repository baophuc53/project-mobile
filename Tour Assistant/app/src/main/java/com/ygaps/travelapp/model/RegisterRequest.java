package com.ygaps.travelapp.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("password")
    
    private String password;
    @SerializedName("fullName")
    
    private String fullName="";
    @SerializedName("email")
    
    private String email;
    @SerializedName("phone")
    
    private String phone;
    @SerializedName("address")
    
    private String address="";
    @SerializedName("dob")
    
    private String dob="";
    @SerializedName("gender")

    private int gender;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

}
