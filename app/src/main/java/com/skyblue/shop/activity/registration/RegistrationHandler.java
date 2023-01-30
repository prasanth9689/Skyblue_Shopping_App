package com.skyblue.shop.activity.registration;

import android.content.Context;
import android.content.SharedPreferences;

import com.skyblue.shop.model.RegistrationSpModel;

public class RegistrationHandler {
    private static final String PREFE_NAME = "register";
    private static final String KEY_PREFE_MOBILE = "mobile";
    private static final String KEY_PREFE_PASSWORD = "password";
    private static final String KEY_PREFE_COUNTRY_NAME = "country_name";
    private static final String KEY_PREFE_COUNTRY_NAME_CODE = "country_name_code";
    private static final String KEY_PHONE_CODE = "phone_code";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditer;
    private SharedPreferences mPreferences;

    public RegistrationHandler(Context mContext){
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE);
        this.mEditer = mPreferences.edit();
    }

    public void saveMobileNo(String mobile, String country_name, String country_name_code, String phone_code ){
        mEditer.putString(KEY_PREFE_MOBILE, mobile);
        mEditer.putString(KEY_PREFE_COUNTRY_NAME, country_name);
        mEditer.putString(KEY_PREFE_COUNTRY_NAME_CODE, country_name_code);
        mEditer.putString(KEY_PHONE_CODE, phone_code);
        mEditer.commit();
    }

    public void savePassword(String password){
        mEditer.putString(KEY_PREFE_PASSWORD, password);
        mEditer.commit();
    }

    public RegistrationSpModel getRegisterData(){
        RegistrationSpModel registrationSPMOdel = new RegistrationSpModel();
        registrationSPMOdel.setMobile(mPreferences.getString(KEY_PREFE_MOBILE, KEY_EMPTY));
        registrationSPMOdel.setCountry_name(mPreferences.getString(KEY_PREFE_COUNTRY_NAME, KEY_EMPTY));
        registrationSPMOdel.setCountry_name_code(mPreferences.getString(KEY_PREFE_COUNTRY_NAME_CODE, KEY_EMPTY));
        registrationSPMOdel.setPhone_code(mPreferences.getString(KEY_PHONE_CODE, KEY_EMPTY));
        registrationSPMOdel.setPassword(mPreferences.getString(KEY_PREFE_PASSWORD, KEY_EMPTY));
        return registrationSPMOdel;
    }
}
