package com.mobilepos.abc.abilitypos.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mobilepos.abc.abilitypos.LoginCompanyActivity;
import com.mobilepos.abc.abilitypos.MainActivity;

import java.util.HashMap;

public class SessionManager {
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    // Sharedpref file name
    public static final String MYREF_NAME = "MyPreferences";
    // Email address (make variable public to access from outside)
    private static final String KEY_IMEI = "IMEI";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared pref mode
    // int PRIVATE_MODE = 0;
    private final int MODE = Activity.MODE_PRIVATE;
    // Context
    Context context;
    // Shared Preferences
    private SharedPreferences mySharedPreferences;
    // Editor for Shared preferences
    private SharedPreferences.Editor myEditor;

    public SessionManager(Context context) {
        this.context = context;
        mySharedPreferences = context.getSharedPreferences(MYREF_NAME, MODE);
        myEditor = mySharedPreferences.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String email) {
        // Storing login value as TRUE
        myEditor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        myEditor.putString(KEY_NAME, name);
        // Storing email in pref
        myEditor.putString(KEY_EMAIL, email);
        // commit changes
        myEditor.commit();
    }

    public void passwordSession(long IMEI, String password) {
        // Storing login value as TRUE
        myEditor.putBoolean(IS_LOGIN, true);
        // Storing imei in pref
        myEditor.putString(KEY_IMEI, String.valueOf(IMEI));
        // Storing name in pref
        myEditor.putString(KEY_PASSWORD, password);
        // commit changes
        myEditor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            // Intent intent = new Intent(context, LoginCompanyActivity.class);
            Intent intent = new Intent(context, LoginCompanyActivity.class);
            // Closing all the Activities
            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(intent);
        } else /*if (this.isLoggedIn())*/ {
            Intent intent = new Intent(context, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(intent);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, mySharedPreferences.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, mySharedPreferences.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    public HashMap<String, String> getUserPassword() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user password
        user.put(KEY_IMEI, mySharedPreferences.getString(KEY_IMEI, null));
        user.put(KEY_PASSWORD, mySharedPreferences.getString(KEY_PASSWORD, null));
        return user;
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return mySharedPreferences.getBoolean(IS_LOGIN, false);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        myEditor.clear();
        myEditor.commit();

        // After logout redirect user to Loing Activity
        // Intent intent = new Intent(context, LoginCompanyActivity.class);
        Intent intent = new Intent(context, LoginCompanyActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(intent);
    }


}
