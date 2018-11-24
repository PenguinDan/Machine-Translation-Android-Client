package com.example.daniel.meetkai_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.daniel.meetkai_test.Utilities.UserUtilities;

public class SplashScreenActivity extends Activity {
    /***
     * Initializes the application
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check whether the User's information is cached and start the appropriate activity
        // Make sure that the User's refresh token has not expired also
        if (UserUtilities.isLoggedIn(this) != null &&
                UserUtilities.getUserRefreshToken(this) != null) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivityContainer.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, AuthenticationContainer.class));
        }
    }
}
