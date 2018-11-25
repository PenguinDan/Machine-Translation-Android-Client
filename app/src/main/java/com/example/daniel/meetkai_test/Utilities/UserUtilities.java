package com.example.daniel.meetkai_test.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daniel.meetkai_test.Interfaces.OnRetrieveAccessToken;
import com.example.daniel.meetkai_test.MeetKai.AuthenticationResponse;
import com.example.daniel.meetkai_test.MeetKai.Request;
import com.example.daniel.meetkai_test.MeetKai.User;

import java.time.OffsetDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        // Cache the User's username
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
     * Caches the user's new access tokens
     * @param context The application's current context
     * @param authResponse The response received from the server
     */
    public static void cacheNewAccessToken(Context context, AuthenticationResponse authResponse) {
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Grab the editor to change the information current stored in the user's shared preferences
        // folder
        SharedPreferences.Editor editor = userPreferences.edit();
        // Cache the user's new access token
        editor.putString(USER_ACCESS_TOKEN, authResponse.getAccessTokenValue());
        editor.putString(USER_ACCESS_TOKEN_EXPIRATION, authResponse.getAccessTokenExpiration());
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
     * Returns the user's cached RefreshToken if it hasn't expired
     *
     * @param context The application's current context
     * @return The refresh token string, otherwise, returns null
     */
    public static String getUserRefreshToken(Context context) {
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Retrieve the user's refresh token
        String expirationDate = userPreferences.getString(USER_REFRESH_TOKEN_EXPIRATION, null);
        // If the expiration token is null, then return false
        if(expirationDate == null) {
            return null;
        }
        // Turn it into a date object
        OffsetDateTime expiration = OffsetDateTime.parse(expirationDate);

        // If the token has not expired, return the token string, otherwise return null
        if (OffsetDateTime.now().compareTo(expiration) < 0) {
            return userPreferences.getString(USER_REFRESH_TOKEN, null);
        }
        return null;
    }

    /**
     * Returns the user's cached AccessToken if it hasn't expired, if it has expired, begin a
     * request to retrieve a new access token
     *
     * @param context The application's current context
     * @param onRetrieveAccessToken The callback method to be called for the following cases
     *                              : Retrieving an unexpired access token from cache
     *                              : Retrieving a new access token from the server
     */
    public static void getUserAccessToken(final Context context, final OnRetrieveAccessToken onRetrieveAccessToken) {
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Retrieve the user's refresh token
        String expirationDate = userPreferences.getString(USER_ACCESS_TOKEN_EXPIRATION, null);
        // If the expiration token is null, then return false, the user has to log in again
        if(expirationDate == null) {
            onRetrieveAccessToken.onGetAccessToken(null);
        }
        // Turn it into a date object
        OffsetDateTime expiration = OffsetDateTime.parse(expirationDate);

        // If the token has not expired, return the token string, otherwise return null
        if (OffsetDateTime.now().compareTo(expiration) < 0) {
            onRetrieveAccessToken.onGetAccessToken(userPreferences.getString(USER_ACCESS_TOKEN, null));
        } else {
            // Check whether the Refresh token has expired
            String refreshToken = UserUtilities.getUserRefreshToken(context);
            // Create the request object to begin getting a new access token
            Request request = new Request(context);
            Call<AuthenticationResponse> renewTokenRequest = request.renewToken(
                    userPreferences.getString(USERNAME, ""),
                    refreshToken
            );
            renewTokenRequest.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    if (response.code() == Request.ACCEPTED) {
                        // Successful token retrieval, return the access token through the callback
                        AuthenticationResponse authResponse = response.body();
                        UserUtilities.cacheNewAccessToken(context, authResponse);
                        onRetrieveAccessToken.onAccessTokenRenewed(response.code(), authResponse.getAccessTokenValue());
                    } else {
                        onRetrieveAccessToken.onAccessTokenRenewed(response.code(), null);
                    }
                }

                // Access token retrieval failed due to a server error
                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    t.printStackTrace();
                    onRetrieveAccessToken.onAccessTokenRenewed(Request.INTERNAL_SERVER_ERROR,null);
                }
            });
        }
    }

    /**
     * Returns whether the user is an admin
     *
     * @param context The application's current context
     * @return True if the user is an admin, false otherwise, this is verified in the server
     */
    public static boolean userIsAdmin(Context context) {
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Retrieve whether the user is an admin
        return userPreferences.getBoolean(USER_IS_ADMIN, false);
    }

    /**
     * Returns a user object with the user's information
     *
     * @param context The application's current context
     *
     * @return A user object
     */
    public static User getUserObject(Context context) {
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        // Create the user object from their information and return it
        return new User(
                userPreferences.getString(USERNAME, ""),
                userPreferences.getBoolean(USER_IS_ADMIN, false)
        );
    }

    /**
     * Clears the user's current cached information
     *
     * @param context The application's current context
     */
    public static void clearUserCache(Context context) {
        // Retrieve the user's shared preferences folder
        SharedPreferences userPreferences = getUserSharedPreferences(context);
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();
        editor.apply();
    }


    /**
     * Retrieves the SharedPreferences object that corresponds to the storing the User information
     *
     * @param context The application's current context
     *
     * @return new SharedPreferences object
     */
    private static SharedPreferences getUserSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_USER_DATA, context.MODE_PRIVATE);
    }
}
