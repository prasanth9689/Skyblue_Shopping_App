package com.skyblue.shop.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderCancel {
    // request
    @SerializedName("product_id")
    public String product_id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("status")
    public String status;

    @SerializedName("data")
    public List<Data> data = null;

    public class Data{
        @SerializedName("message")
        public String message;
    }
}
