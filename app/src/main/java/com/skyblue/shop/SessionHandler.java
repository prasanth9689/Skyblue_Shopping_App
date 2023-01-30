package com.skyblue.shop;

import android.content.Context;
import android.content.SharedPreferences;

import com.skyblue.shop.model.User;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CITY = "city";
    private static final String KEY_AREA = "area";
    private static final String KEY_LANDMARK = "landmark";
    private static final String KEY_PIN_CODE = "pin_code";
    private static final String KEY_STATE = "state";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param mobile
     * @param name
     */
    public void loginUser(String user_id , String mobile, String name , String area , String landmark , String pin_code , String city , String state) {
        mEditor.putString(KEY_USER_ID, user_id);
        mEditor.putString(KEY_MOBILE, mobile);
        mEditor.putString(KEY_NAME, name);
        mEditor.putString(KEY_CITY, city);
        mEditor.putString(KEY_AREA, area);
        mEditor.putString(KEY_LANDMARK, landmark);
        mEditor.putString(KEY_PIN_CODE, pin_code);
        mEditor.putString(KEY_STATE, state);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    public void updateLoginUser(String mobile, String name , String area , String landmark , String pin_code , String city , String state) {
        mEditor.putString(KEY_MOBILE, mobile);
        mEditor.putString(KEY_NAME, name);
        mEditor.putString(KEY_CITY, city);
        mEditor.putString(KEY_AREA, area);
        mEditor.putString(KEY_LANDMARK, landmark);
        mEditor.putString(KEY_PIN_CODE, pin_code);
        mEditor.putString(KEY_STATE, state);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setMobile(mPreferences.getString(KEY_MOBILE, KEY_EMPTY));
        user.setName(mPreferences.getString(KEY_NAME, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));
        user.setId(mPreferences.getString(KEY_USER_ID, KEY_EMPTY));
        user.setArea(mPreferences.getString(KEY_AREA, KEY_EMPTY));
        user.setLandmark(mPreferences.getString(KEY_LANDMARK, KEY_EMPTY));
        user.setCity(mPreferences.getString(KEY_CITY, KEY_EMPTY));
        user.setPin_code(mPreferences.getString(KEY_PIN_CODE, KEY_EMPTY));
        user.setState(mPreferences.getString(KEY_STATE, KEY_EMPTY));
        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }


}
