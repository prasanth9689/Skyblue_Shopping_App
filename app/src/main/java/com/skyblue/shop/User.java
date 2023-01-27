package com.skyblue.shop;

import java.util.Date;

public class User {
    String mobile;
    String name;
    String city;
    String area;
    String landmark;
    String pin_code;
    String state;
    String id;
    Date sessionExpiryDate;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getPin_code() {
        return pin_code;
    }

    public String getState() {
        return state;
    }
}
