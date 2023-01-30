package com.skyblue.shop.model;

public class Products {

    public String id , thumbnail , product_name , title , sale_price , discount_price , rating , gategory_id , stock , seller_id , seller_name;
    public String feature_1 , feature_2 , feature_3 , feature_4 , feature_5 ;


    public Products() {
        this.thumbnail = thumbnail;
        this.product_name = product_name;
        this.title = title;
        this.sale_price = sale_price;
        this.discount_price = discount_price;
        this.rating = rating;
        this.id = id;
        this.feature_1 = feature_1;
        this.feature_2 = feature_2;
        this.feature_3 = feature_3;
        this.feature_4 = feature_4;
        this.feature_5 = feature_5;
        this.gategory_id = gategory_id;
        this.stock = stock;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getTitle() {
        return title;
    }

    public String getSale_price() {
        return sale_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public String getRating() {
        return rating;
    }

    public String getId() {
        return id;
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

    public String getGategory_id() {
        return gategory_id;
    }

    public String getStock() {
        return stock;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setGategory_id(String gategory_id) {
        this.gategory_id = gategory_id;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }
}