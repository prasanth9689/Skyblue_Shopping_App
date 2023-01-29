package com.skyblue.shop.registration;

import android.content.Context;
import android.content.SharedPreferences;

public class RegistrationHandler {
    private static final String PREFE_NAME = "register";
    private static final String KEY_PREFE_MOBILE = "mobile";
    private Context mContext;
    private SharedPreferences.Editor mEditer;
    private SharedPreferences mPreferences;

    public RegistrationHandler(Context context){
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREFE_NAME, Context.MODE_PRIVATE);
        this.mEditer = mPreferences.edit();
    }
}
