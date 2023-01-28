package com.skyblue.shop.model;

import com.google.gson.annotations.SerializedName;

public class Registration {
    // response
    @SerializedName("BofdNo")
    public String BofdNo;


    // request
    @SerializedName("name")
    public String name;

    @SerializedName("mobile")
    public String mobile;

    @SerializedName("password")
    public String password;

    @SerializedName("mobile_number_only")
    public String mobile_number_only;

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

    @SerializedName("date")
    public String date;

    @SerializedName("time")
    public String time;

    @SerializedName("time_zone")
    public String time_zone;

    @SerializedName("date_time_zone")
    public String date_time_zone;

    @SerializedName("token")
    public String token;
}
