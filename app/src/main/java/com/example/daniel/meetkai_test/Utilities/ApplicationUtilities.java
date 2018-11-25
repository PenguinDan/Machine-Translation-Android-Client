package com.example.daniel.meetkai_test.Utilities;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

    public static String prettyPrintJson(String jsonString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonString);
        return gson.toJson(je);
    }
}
