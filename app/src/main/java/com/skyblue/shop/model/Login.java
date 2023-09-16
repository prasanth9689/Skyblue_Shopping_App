package com.skyblue.shop.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Login {
    // request
    @SerializedName("mobile")
    public String mobile;

    @SerializedName("password")
    public String password;

    // response
    @SerializedName("status")
    public String status;

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

    @SerializedName("data")
    public List<Data> data = null;

    public class Data{
        @SerializedName("name")
        public String name;

        @SerializedName("message")
        public String message;

        @SerializedName("id")
        public String id;

        @SerializedName("area")
        public String area;

        @SerializedName("landmark")
        public String landmark;

        @SerializedName("city")
        public String city;

        @SerializedName("pin_code")
        public String pin_code;

        @SerializedName("state")
        public String state;
    }
}
