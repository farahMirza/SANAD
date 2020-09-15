package com.example.sanad;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;


public class ReachingPatient extends AppCompatActivity {
    public static final String PROVIDER_HAS_BEEN_ARRIVED = "Provider has been arrived";
    public static final String VISIT_HAVE_BEEN_ENDED = "visit have been ended!";
    private double longitude, latitude;
    private int PLACE_PICKER_REQUEST = 1;
    private LatLng chosenPlace;
    private DatabaseReference myRef;
    private FirebaseUser user;
    static double patLang = 1;
    static double patLongt = 1;
    private Button arrived, end, call, choose;
    private static String number, address, patName, proName, times, dates;
    private DatabaseReference ref, patRef, provRef;
    private Geocoder geocoder;
    private List<Address> addresses;
    private String[] timeParts, dateParts;
    private String year0, month0, day0, hour0, minute0, apointTime, apointDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaching_patient);
        choose = (Button) findViewById(R.id.prov_choose);
        arrived = (Button) findViewById(R.id.prov_arrived);
        end = (Button) findViewById(R.id.prov_end);
        call = (Button) findViewById(R.id.call_patient);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ReachingPatient.this);
        apointTime = preferences.getString("ApointTime", "");
        apointDate = preferences.getString("ApointDate", "");
        final String patAddr = preferences.getString("addrs", "");
        dateParts = apointDate.split("/");
        timeParts = apointTime.split(":");
        day0 = dateParts[2];
        year0 = dateParts[0];
        month0 = dateParts[1];
        hour0 = timeParts[0];
        minute0 = timeParts[1];
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(ReachingPatient.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {

                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }
        });
        arrived.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(ReachingPatient.this);
                dialog.setMessage("Confirm notifying patient that  you arrived?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                String send_email = getIntent().getStringExtra("patId");
                                String msg = PROVIDER_HAS_BEEN_ARRIVED;

                                String str = "{"
                                        + "\"app_id\": \"f0d1c236-2c10-40c5-8990-28a903e05a0b\","
                                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                        + "\"data\": {\"foo\": \"bar\"},"
                                        + "\"contents\": {\"en\": \"" + msg + "\"},"
                                        + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"
                                        + "}";
                                sendNotification(str);
                                Toast.makeText(ReachingPatient.this, "Notification have been sent", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ReachingPatient.this);
                dialog.setMessage("Confirm notifying patient that visit have been ended?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String send_email;
                                Calendar c = Calendar.getInstance();
                                int day = c.get(Calendar.DAY_OF_MONTH);
                                int month = c.get(Calendar.MONTH);
                                int year = c.get(Calendar.YEAR);
                                int hour = c.get(Calendar.HOUR);
                                int minute = c.get(Calendar.MINUTE);
                                String d = year + "/" + (month + 1) + "/" + day;
                                String t = hour + ":" + minute;
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                ref = FirebaseDatabase.getInstance().getReference("History").child(user.getUid() + getIntent().getStringExtra("patId") + year0 + month0 + day0 + hour0 + minute0);
                                patRef = FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("patId"));
                                provRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                                patRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //saving the info into history after ending visit
                                        Users u = dataSnapshot.getValue(Users.class);
                                        patName = u.getNAME();
                                        ref.child("patName").setValue(patName);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                provRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Users p = dataSnapshot.getValue(Users.class);
                                        proName = p.getNAME();
                                        ref.child("proName").setValue(proName);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //saving info into history
                                ref.child("provId").setValue(user.getUid());
                                ref.child("patID").setValue(getIntent().getStringExtra("patId"));
                                ref.child("date").setValue(d);
                                ref.child("time").setValue(t);
                                ref.child("patLocation").setValue(patAddr);
                                Log.i("show time", "onClick: " + year0 + month0 + day0 + hour0 + minute0);

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                send_email = getIntent().getStringExtra("patId");
                                String msg = VISIT_HAVE_BEEN_ENDED;
                                String str = "{"
                                        + "\"app_id\": \"f0d1c236-2c10-40c5-8990-28a903e05a0b\","
                                        + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                        + "\"data\": {\"foo\": \"bar\"},"
                                        + "\"contents\": {\"en\": \"" + msg + "\"},"
                                        + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"
                                        + "}";
                                sendNotification(str);
                                Toast.makeText(ReachingPatient.this, "Notification have been sent", Toast.LENGTH_SHORT).show();
                                DatabaseReference r = FirebaseDatabase.getInstance().getReference("Appointments");
                                r.child(user.getUid() + getIntent()
                                        .getStringExtra("patId") + year0 + month0 + day0 + hour0 + minute0).setValue(null);

                                Intent intent = new Intent(ReachingPatient.this, SignIn.class);
                                startActivity(intent);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();


            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("patId"));
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users u = dataSnapshot.getValue(Users.class);
                        number = u.getMOBILE();
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(ReachingPatient.this, data);
                chosenPlace = place.getLatLng();
                longitude = (double) chosenPlace.longitude;
                latitude = (double) chosenPlace.latitude;
                user = FirebaseAuth.getInstance().getCurrentUser();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String t = preferences.getString("ApointTime", "");
                String d = preferences.getString("ApointDate", "");

                myRef = FirebaseDatabase.getInstance().getReference("Appointments").child(user.getUid() + getIntent().getStringExtra("patId") + year0 + month0 + day0 + hour0 + minute0);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Appointments v = dataSnapshot.getValue(Appointments.class);
                        if (v != null) {
                            patLang = v.getLng();
                            patLongt = v.getLongt();
                        }
                        geocoder = new Geocoder(ReachingPatient.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(patLang, patLongt, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        address = addresses.get(0).getAddressLine(0);
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + patLang + "," + patLongt));
                        startActivity(i);


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("", "Failed to read value.", error.toException());
                    }
                });
//                Intent i = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + patLang + "," + patLongt));
//                startActivity(i);

            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }

    }

    private void sendNotification(String str) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String strJsonBody = str;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
//                    String send_email;
//                    String msg = PROVIDER_HAS_BEEN_ARRIVED;
                    //This is a Simple Logic to Send Notification different Device Programmatically....
//                    if (MainActivity.LoggedIn_User_Email != null)
//                        if (MainActivity.LoggedIn_User_Email.equals(user.getUid())) {
//                            send_email = getIntent().getStringExtra("patId");
//
//                        } else {
//                            send_email = user.getUid();
//                        }
//                    else send_email = getIntent().getStringExtra("patId");

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

//                        String strJsonBody = "{"
//                                + "\"app_id\": \"f0d1c236-2c10-40c5-8990-28a903e05a0b\","
//                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
//                                + "\"data\": {\"foo\": \"bar\"},"
//                                + "\"contents\": {\"en\": \"" + msg + "\"},"
//                                + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"
//                                + "}";


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