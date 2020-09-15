package com.example.sanad;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.

    DatabaseReference myref, appointmentRef;
    FirebaseUser user;
    static Appointments point;
    private Application application;
    private boolean flag = true;
    int x;
    static OSNotificationOpenResult res;
    String str, msg, dateSubString, timeSubString;
    String[] split;


    public ExampleNotificationOpenedHandler(Application application) {
        this.application = application;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        x = 0;
        res = result;
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;
        int id = result.notification.androidNotificationId;
        msg = result.notification.payload.body;

        Log.i("fofo", "notificationOpened: " + x);
        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }
        if (msg.equals("visit have been ended!")) {
            Intent intent = new Intent(application, RatingProvider.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        } else if (result.action.actionID == null)
            startApp();


        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            if (result.action.actionID.equals("confirmBtn")) {

                confirm(result, id);
                ++x;

            } else if (result.action.actionID != null && result.action.actionID.equals("declineBtn")) {
                decline(result, id);

            }

        }

    }


    // The following can be used to open an Activity of your choice.
    // Replace - getApplicationContext() - with any Android Context.
    private void startApp() {

        Intent intent = new Intent(application, SignIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    private void confirm(final OSNotificationOpenResult result, int id) {
        OneSignal.cancelNotification(id);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("abcd", "notificationOpened:bla bla ");
        myref = FirebaseDatabase.getInstance().getReference("TempAppointments");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                res = result;
                Log.i("farah", "onDataChange: " + res.action.actionID);
                if (res.action.actionID != null && res.action.actionID.equals("confirmBtn"))
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        point = postSnapshot.getValue(Appointments.class);
                        if (point.getProviderID().equals(user.getUid())) {
                            FirebaseDatabase datab = FirebaseDatabase.getInstance();
                            DatabaseReference Ref = datab.getReference("Appointments");
                            String[] timeParts, dateParts;
                            String year, month, day;
                            dateParts = point.getDate().split("/");
                            timeParts = point.getTime().split(":");
                            day = dateParts[2];
                            year = dateParts[0];
                            month = dateParts[1];
                            String hour = timeParts[0];
                            String minute = timeParts[1];
                            Ref.child(point.getProviderID() + point.getPatientID() + year + month + day + hour + minute).setValue(point);
                            Intent intent = new Intent(application, SignIn.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(intent);
                            String msg = point.getProvName() + " have been confirmed the appointment";
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            String send_email = point.getPatientID();
                            String strJsonBody = "{"
                                    + "\"app_id\": \"02046bd2-1b21-466d-8a9b-1dd62a62702b\","
                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                    + "\"data\": {\"foo\": \"bar\"},"
                                    + "\"contents\": {\"en\": \"" + msg + "\"},"
                                    + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"
                                    + "}";
                            sendNotification(strJsonBody);
                            myref.child(point.getProviderID() + point.getPatientID() + year + month + day + hour + minute).setValue(null);
                            appointmentRef = FirebaseDatabase.getInstance().getReference("Appointments");
                            appointmentRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    Appointments app = dataSnapshot.getValue(Appointments.class);
                                    if (app.getProviderID().equals(user.getUid()) || app.getPatientID().equals(user.getUid())) {
                                        String[] timeParts = app.getTime().split(":");
                                        String hour0 = timeParts[0];
                                        int hour = Integer.parseInt(hour0);
                                        String minute0 = timeParts[1];
                                        int minute = Integer.parseInt(minute0);
                                        String[] dateParts = app.getDate().split("/");
                                        String day0 = dateParts[2];
                                        int day = Integer.parseInt(day0);
                                        String year0 = dateParts[0];
                                        int year = Integer.parseInt(year0);
                                        String month0 = dateParts[1];
                                        int month = Integer.parseInt(month0);
                                        Calendar beginTime = Calendar.getInstance();
                                        beginTime.set(year, month - 1, day, hour, minute);
                                        Calendar endTime = Calendar.getInstance();
                                        endTime.set(year, month - 1, day, hour + 1, minute);
                                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                                .setData(CalendarContract.Events.CONTENT_URI)
                                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                                .putExtra(CalendarContract.Events.TITLE, "you have an appointment")
                                                .putExtra(CalendarContract.Events.DESCRIPTION, "appointment")
                                                .putExtra(CalendarContract.Events.EVENT_LOCATION, app.getPate_location())
                                                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        application.startActivity(intent);
                                        res.action.actionID = null;
                                        return;
                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                            break;
                        }
                    }
                else {
                    res = result;
                    Log.i("resasa", "onDataChange: " + res.action.actionID);
                }
                return;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
        return;
    }


    private void decline(final OSNotificationOpenResult result, int id) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        str = msg.substring(msg.indexOf(":") + 2);
        split = str.split("  At: ");
        final String dateSubString = split[0];
        final String timeSubString = split[1];
        Log.i("qasqas", "notificationOpened: " + dateSubString);
        Log.i("qasqas2", "notificationOpened: " + timeSubString);
        myref = FirebaseDatabase.getInstance().getReference("TempAppointments");
        myref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                res = result;
                if (res.action.actionID != null && res.action.actionID.equals("declineBtn"))
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        point = postSnapshot.getValue(Appointments.class);
                        if (point.getProviderID().equals(user.getUid()) && point.getTime().equals(timeSubString) && point.getDate().equals(dateSubString)) {
                            String[] timeParts, dateParts;
                            String year, month, day;
                            dateParts = point.getDate().split("/");
                            timeParts = point.getTime().split(":");
                            day = dateParts[2];
                            year = dateParts[0];
                            month = dateParts[1];
                            String hour = timeParts[0];
                            String minute = timeParts[1];
                            res.action.actionID = null;
                            myref.child(point.getProviderID() + point.getPatientID() + year + month + day + hour + minute).setValue(null);
                            String msg = point.getProvName() + " is not available at that time!";
                            String send_email = point.getPatientID();
                            String strJsonBody = "{"
                                    + "\"app_id\": \"02046bd2-1b21-466d-8a9b-1dd62a62702b\","
                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                    + "\"data\": {\"foo\": \"bar\"},"
                                    + "\"contents\": {\"en\": \"" + msg + "\"},"
                                    + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"

                                    + "}";
                            sendNotification(strJsonBody);
                            break;
                        }
                    }
                else
                    res = result;
                return;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void sendNotification(final String str) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);


                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic OGUzYWYzMTItZjI1NC00ODk2LTllMzEtOTMyODZlMmEzYTg1");
                        con.setRequestMethod("POST");
                        String strJsonBody = str;
                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                }
            }
        });

    }

}
