package com.skyblue.shop.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    // request
    @SerializedName("mobile")
    public String mobile;

    @SerializedName("password")
    public String password;

    // response
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("area")
    public String area;

    @SerializedName("landmark")
    public String landmark;

    @SerializedName("pin_code")
    public String pin_code;

    @SerializedName("city")
    public String city;

    @SerializedName("state")
    public String state;

    @SerializedName("message")
    public String message;
}
