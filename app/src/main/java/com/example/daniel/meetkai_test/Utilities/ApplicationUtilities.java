package com.example.daniel.meetkai_test.Utilities;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ApplicationUtilities {

    /**
     * Displays a message as a toast
     *
     * @param message The message to be displayed
     */
    public static void displayToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 50);
        toast.show();
    }
}
