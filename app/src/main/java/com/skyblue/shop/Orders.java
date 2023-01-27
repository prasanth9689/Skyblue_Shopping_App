package com.skyblue.shop;

public class Orders {

    public String product_id , product_name , title , thumbnail , amount;
    public String feature_1 , feature_2 , feature_3 , feature_4 , feature_5 ;
    public String seller_id , seller_name , order_status , status_text, order_date , order_date_time;

    public Orders() {
        this.product_id = product_id;
        this.product_name = product_name;
        this.title = title;
        this.thumbnail = thumbnail;
        this.amount = amount;
        this.feature_1 = feature_1;
        this.feature_2 = feature_2;
        this.feature_3 = feature_3;
        this.feature_4 = feature_4;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.order_status = order_status;
        this.status_text = status_text;
        this.order_date_time = order_date_time;
        this.order_date = order_date;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFeature_1(String feature_1) {
        this.feature_1 = feature_1;
    }

    public void setFeature_2(String feature_2) {
        this.feature_2 = feature_2;
    }

    public void setFeature_3(String feature_3) {
        this.feature_3 = feature_3;
    }

    public void setFeature_4(String feature_4) {
        this.feature_4 = feature_4;
    }

    public void setFeature_5(String feature_5) {
        this.feature_5 = feature_5;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getStatus_text() {
        return status_text;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getFeature_1() {
        return feature_1;
    }

    public String getFeature_2() {
        return feature_2;
    }

    public String getFeature_3() {
        return feature_3;
    }

    public String getFeature_4() {
        return feature_4;
    }

    public String getFeature_5() {
        return feature_5;
    }

    public String getOrder_date() {
        return order_date;
    }
}