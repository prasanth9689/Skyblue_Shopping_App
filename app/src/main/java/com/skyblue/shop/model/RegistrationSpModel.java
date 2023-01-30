package com.skyblue.shop.model;

public class RegistrationSpModel {
    String mobile , password , country_name, country_name_code, phone_code;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public void setCountry_name_code(String country_name_code) {
        this.country_name_code = country_name_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getCountry_name_code() {
        return country_name_code;
    }

    public String getPhone_code() {
        return phone_code;
    }
}
