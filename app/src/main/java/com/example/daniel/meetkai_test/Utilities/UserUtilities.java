package com.example.daniel.meetkai_test.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daniel.meetkai_test.MeetKai.User;
import com.google.gson.Gson;

public class UserUtilities {
    private static final String SHARED_PREF_USER_DATA = "UserData";
    private static final String USER_INFO = "userJson";
    private static final String USERNAME = "username";

    /**
     *  Caches the user's information
     * @param context
     * @param user - DotifyUser object containing the user's information
     */
    public static void cacheUser(Context context, User user){
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Grab the editor to change the information currently stored in the User's shared preferences
        // folder
        SharedPreferences.Editor editor = userPreferences.edit();
        // Chache the User's username
        editor.putString(USERNAME, user.getUsername());
        // Create a Gson object for JSON utilities
        Gson gson = new Gson();
        // Cache the User object itself
        String userJson = gson.toJson(user);
        // Store the user json object
        editor.putString(USER_INFO, userJson);
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
