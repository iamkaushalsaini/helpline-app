package com.hlit.helplinetelecom;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_FULLNAME = "keyfullname";
    // private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    //  private static final String KEY_img = "keyimg";
    private static final String KEY_PHONE_NUMBER = "keyphone_number";
    private static final String KEY_ID = "keyid";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FULLNAME, user.getName());
        // editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        // editor.putString(KEY_img, user.getUser_img());
        editor.putString(KEY_PHONE_NUMBER, user.getMobile());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FULLNAME, null),
                // sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                //sharedPreferences.getString(KEY_img, null),
                sharedPreferences.getString(KEY_PHONE_NUMBER, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //  ctx.startActivity(new Intent(ctx, login.class));
    }
}

