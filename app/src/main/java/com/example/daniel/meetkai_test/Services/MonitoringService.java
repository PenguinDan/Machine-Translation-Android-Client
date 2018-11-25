package com.example.daniel.meetkai_test.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.daniel.meetkai_test.Interfaces.ResultReceiverCallback;

import org.jetbrains.annotations.Nullable;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;

public class MonitoringService extends IntentService {
    // Monitoring Services
    public static final String BEGIN_MONITORING = "bm";
    // Class constants
    private static final String TAG = MonitoringService.class.getSimpleName();
    private static final int SERVER_MONITOR_PORT = 30000;
    // Class Variables
    private static ResultReceiverCallback<String> resultReceiverCallback;

    public MonitoringService() {
        super(MonitoringService.class.getName());
    }

    /**
     * Sets the callback fort this service
     * @param callback
     */
    public static void setResultReceiverCallback(ResultReceiverCallback<String> callback) {
        resultReceiverCallback = callback;
    }

    /**
     * Begins a specified service
     *
     * @param intent Contains the service to start
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        switch(intent.getAction()) {
            case BEGIN_MONITORING: {
                Log.d(TAG, "Begin monitoring services");
                beginMonitoring(intent);
            }
            break;
        }
    }


    /**
     * Begins monitoring the annotations sent to the server
     */
    private void beginMonitoring(Intent intent) {
        try{
            // Get the receiver callback class
            final ResultReceiver receiver = intent.getParcelableExtra("receiver");
            Bundle bundle = new Bundle();

            // Tell the server that we are open to monitoring
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress inet4Address = Inet4Address.getByName("www.penguindan-test.gq");

            // Create the request to tell the server that we are listening
            byte[] requestBytes = "connect".getBytes();
            DatagramPacket packet = new DatagramPacket(
                    requestBytes,
                    requestBytes.length,
                    inet4Address,
                    SERVER_MONITOR_PORT
            );

            // Send the request to the server
            clientSocket.send(packet);


            // Listen on a port for UDP messages from the server
            byte[] responseBuffer = new byte[1000];
            // Receive the new socket to connect to
            DatagramPacket responsePacket = new DatagramPacket(
                    responseBuffer,
                    responseBuffer.length
            );

            while(true) {
                clientSocket.receive(responsePacket);
                String text = new String(responseBuffer, 0, responsePacket.getLength());
                bundle.putString("annotation", text);
                receiver.send(0, bundle);
            }
        }catch(Exception ex) {
            Log.d(TAG, "beginMonitoring-> Error");
            ex.printStackTrace();
        }
    }

}
