package com.gios.freshngreen.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.gios.freshngreen.utils.Constants.KEY_MOBILE;
import static com.gios.freshngreen.utils.Constants.KEY_NAME;
import static com.gios.freshngreen.utils.Constants.KEY_USERID;
import static com.gios.freshngreen.utils.Constants.KEY_USERNAME;
import static com.gios.freshngreen.utils.Constants.SHARED_PREF_KEY;

public class SharedPref {
    private SharedPreferences sharedPreferences;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
    }
    public void clearAllPrefs(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERID, userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USERID, "");
    }

    public void setMobile(String mobile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOBILE, mobile);
        editor.apply();
    }

    public String getMobile() {
        return sharedPreferences.getString(KEY_MOBILE, "");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }


}
