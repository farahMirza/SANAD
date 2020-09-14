package com.sanad.farah.sanad;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Scanner;

public class  ConformationActivity extends AppCompatActivity {
    private static final String TAG = "Conformation Activity";
    private ListView lst;
    private TextView name, location, specialization, number, time, pName;
    private ImageButton call;
    private Button choose_location, confirm, cancel;
    private DatabaseReference database;
    private DatabaseReference proDatabase;
    private FirebaseUser user;
    private ArrayList<Appointments> appointList;
    int PLACE_PICKER_REQUEST = 1;
    LatLng chosenPlace;
    static double longitude, latitude;
    private String day, hour, year, month, min;
    private boolean flag;
    private String[] timeParts, dateParts, tp, dp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conformation);
        name = (TextView) findViewById(R.id.PN);
        pName = (TextView) findViewById(R.id.APatientName);
        location = (TextView) findViewById(R.id.PL);
        specialization = (TextView) findViewById(R.id.PS);
        number = (TextView) findViewById(R.id.PP);
        time = (TextView) findViewById(R.id.CT);
        call = (ImageButton) findViewById(R.id.phone);
        choose_location = (Button) findViewById(R.id.CL);
        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);
        user = FirebaseAuth.getInstance().getCurrentUser();
        name.setText(getIntent().getStringExtra("prov_name"));
        location.setText(getIntent().getStringExtra("prov_loc"));
        specialization.setText(getIntent().getStringExtra("provSpecial"));
        time.setText(getIntent().getStringExtra("Date") + "  At: " + getIntent().getStringExtra("time"));
        dateParts = getIntent().getStringExtra("Date").split("/");
        timeParts = getIntent().getStringExtra("time").split(":");
        day = dateParts[2];
        year = dateParts[0];
        month = dateParts[1];
        hour = timeParts[0];
        min = timeParts[1];

        number.setText(getIntent().getStringExtra("prov_number"));
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getIntent().getStringExtra("prov_number"), null));
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to write the appointment in data base
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ConformationActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("provName", getIntent().getStringExtra("prov_name"));
                editor.putString("provID", getIntent().getStringExtra("prov_id"));
                editor.putString("Fees", getIntent().getStringExtra("fees"));
                editor.apply();


                if (choose_location.getText().equals("Choose Location"))
                    Toast.makeText(ConformationActivity.this, "Choose Location!", Toast.LENGTH_SHORT).show();
                else {
                    database = FirebaseDatabase.getInstance().getReference("TempAppointments");
                    Appointments point = new Appointments(user.getUid(), getIntent().getStringExtra("prov_id"), getIntent().getStringExtra("prov_name"), user.getDisplayName(), getIntent().getStringExtra("provSpecial"), getIntent().getStringExtra("Date"), getIntent().getStringExtra("time"), getIntent().getStringExtra("prov_loc"), choose_location.getText().toString(), getIntent().getStringExtra("prov_number"), latitude, longitude);
                    database.child(getIntent().getStringExtra("prov_id") + user.getUid() + year + month + day + hour + min).setValue(point);
                    Intent intent = new Intent(ConformationActivity.this, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    sendNotification();
                    startActivity(intent);
                    Toast.makeText(ConformationActivity.this, "wait for provider response", Toast.LENGTH_LONG).show();


                }
            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        choose_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(ConformationActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(ConformationActivity.this, data);
                String address = String.format("Place:%s", place.getAddress());
                chosenPlace = place.getLatLng();
                longitude = (double) chosenPlace.longitude;
                latitude = (double) chosenPlace.latitude;
                choose_location.setText(address);

            }
        }

    }

    private void sendNotification() {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;
                    String msg = "New patient want to reserve an appointment at: " + time.getText();
                    send_email = getIntent().getStringExtra("prov_id");


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

                        String strJsonBody = "{"
                                + "\"app_id\": \"f0d1c236-2c10-40c5-8990-28a903e05a0b\","
                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + msg + "\"},"
                                + "\"buttons\": [{\"id\": \"confirmBtn\", \"text\": \"Confirm\", \"icon\": \"ic_menu_share\"}, {\"id\": \"declineBtn\", \"text\": \"Decline\", \"icon\": \"ic_menu_send\"}],"
                                + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"
                                + "}";


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
