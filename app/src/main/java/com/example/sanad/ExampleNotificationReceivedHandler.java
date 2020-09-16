package com.example.sanad;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    public static final String VISIT_HAVE_BEEN_ENDED = "visit have been ended!";
    Application application;

    public ExampleNotificationReceivedHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String msg = notification.payload.body;

        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null) {
                Log.i("OneSignalExample", "customkey set with value: " + customKey);


            }
            if (msg.equals(VISIT_HAVE_BEEN_ENDED)) {
                Toast.makeText(application, "hi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(application, RatingProvider.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                application.startActivity(intent);
            }
         if (msg.contains("have been confirmed the appointment")) {
            Intent intent = new Intent(application, SignIn.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
             application.startActivity(intent);
         }


        }
    }


}