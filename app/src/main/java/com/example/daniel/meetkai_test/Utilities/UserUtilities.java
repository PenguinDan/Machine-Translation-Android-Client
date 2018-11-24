package com.example.daniel.meetkai_test.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daniel.meetkai_test.MeetKai.AuthenticationResponse;
import com.example.daniel.meetkai_test.MeetKai.User;
import com.google.gson.Gson;

public class UserUtilities {
    private static final String SHARED_PREF_USER_DATA = "UserData";
    private static final String USER_ACCESS_TOKEN = "access_token";
    private static final String USER_ACCESS_TOKEN_EXPIRATION = "access_token_expiration";
    private static final String USER_REFRESH_TOKEN = "refresh_token";
    private static final String USER_REFRESH_TOKEN_EXPIRATION = "refresh_token_expiration";
    private static final String USER_IS_ADMIN = "is_admin";
    private static final String USERNAME = "username";

    /**
     *  Caches the user's information
     * @param context The application's current context
     * @param authResponse  The response received from the server
     */
    public static void cacheUser(Context context, AuthenticationResponse authResponse){
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Grab the editor to change the information currently stored in the User's shared preferences
        // folder
        SharedPreferences.Editor editor = userPreferences.edit();
        // Chache the User's username
        editor.putString(USERNAME, authResponse.getUsername());
        // Store the user tokens
        editor.putString(USER_ACCESS_TOKEN, authResponse.getAccessTokenValue());
        editor.putString(USER_ACCESS_TOKEN_EXPIRATION, authResponse.getAccessTokenExpiration());
        editor.putString(USER_REFRESH_TOKEN, authResponse.getRefreshTokenValue());
        editor.putString(USER_REFRESH_TOKEN_EXPIRATION, authResponse.getRefreshTokenExpiration());
        editor.putBoolean(USER_IS_ADMIN, authResponse.isAdmin());
        // Apply the changes
        editor.apply();
    }

    /**
     * Check whether there is a user who is currently cached, if there is, return their username,
     * otherwise, return null
     * @return The current logged in User's username, and null otherwise
     */
    public static String isLoggedIn(Context context){
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        return userPreferences.getString(USERNAME, null);
    }

    /**
     * Retrieves the SharedPreferences object that corresponds to the storing the User information
     * @param context - context object to use
     * @return new SharedPreferences object
     */
    private static SharedPreferences getUserSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_USER_DATA, context.MODE_PRIVATE);
    }
}
