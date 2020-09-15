package com.example.sanad;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Booking extends AppCompatActivity {
    public static final String CHOOSE_DATE_AND_TIME = "choose Date and time";
    public static final String CHOOSE_TIME = "Choose Time";
    public static final String CHOOSE_DATE = "Choose Date";
    private RatingBar Rate;
    private TextView provName, provSpecial, phone_number, provdierDescription, providerFees, providerTime, ProvdierLocation, availabilty;
    private Users user;
    private Button chooseDate, chooseTime, book, preview;
    private String providerID, maxDate;
    private boolean flag = true;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView profilePicture;
    private DatabaseReference proDatabase;
    private String[] timeParts, dateParts, tp, dp;
    private FirebaseUser u;
    boolean check;
    String day0, hour0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        profilePicture = (ImageView) findViewById(R.id.img);
        provName = (TextView) findViewById(R.id.prov_name);
        provSpecial = (TextView) findViewById(R.id.prov_specializtion);
        Rate = (RatingBar) findViewById(R.id.bar);
        provdierDescription = (TextView) findViewById(R.id.descrip);
        providerFees = (TextView) findViewById(R.id.money);
        providerTime = (TextView) findViewById(R.id.time);
        ProvdierLocation = (TextView) findViewById(R.id.prov_address);
        availabilty = (TextView) findViewById(R.id.available);
        phone_number = (TextView) findViewById(R.id.phone_number);
       // preview = (Button) findViewById(R.id.preview);
        chooseDate = (Button) findViewById(R.id.choose_date);
        chooseTime = (Button) findViewById(R.id.choose_time);
        book = (Button) findViewById(R.id.booking);
//profile pic
        storage = FirebaseStorage.getInstance();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rating").child(getIntent().getStringExtra("ProviderId"));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RatePro r = dataSnapshot.getValue(RatePro.class);
                if (r != null) {
                    Rate.setRating(r.getRating());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("ProviderId"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(Users.class);
                //profile pic
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReferenceFromUrl("gs://sanad-7f6ef.appspot.com").child(user.getID());
                try {
                    final File localFile = File.createTempFile("images", "jpeg");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePicture.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                } catch (IOException e) {
                }
                ////////////////////////////////////////
                provName.setText(user.getNAME());
                provSpecial.setText(user.getSpecialization());
                provdierDescription.setText(user.getDescription());
                providerFees.setText(user.getFees() + "JD");
                providerTime.setText(user.getWaiting_time() + "Minutes");
                ProvdierLocation.setText(user.getADDRESS());
                phone_number.setText(user.getMOBILE());
                providerID = user.getID();
                availabilty.setText(getIntent().getStringExtra("ProviderAvailability"));
                maxDate = user.getToDay();
                FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                if (user.getID().equals(u.getUid())) {
                    flag = false;
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
        chooseTime.setOnClickListener(new View.OnClickListener() {
            Calendar calender = Calendar.getInstance();
            int hour = calender.get(Calendar.HOUR);
            int minute = calender.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {

                TimePickerDialog tp = new TimePickerDialog(Booking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        chooseTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                tp.show();
            }
        });
        chooseDate.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chooseDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 7 * (24 * 60 * 60 * 1000L));

                dpd.show();
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseTime.getText().equals(CHOOSE_TIME) || chooseDate.getText().equals(CHOOSE_DATE))
                    Toast.makeText(Booking.this, "choose date & time!", Toast.LENGTH_SHORT).show();


                else {
                    u = FirebaseAuth.getInstance().getCurrentUser();
                    if (u.getUid().equals(providerID)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Booking.this);
                        dialog.setMessage("You can't reserve your self!").setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = dialog.create();
                        alert.setTitle("Note");
                        alert.show();

                    } else {
                        proDatabase = FirebaseDatabase.getInstance().getReference("Appointments");
                        proDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dateParts = chooseDate.getText().toString().split("/");
                                day0 = dateParts[2];
                                timeParts = chooseTime.getText().toString().split(":");
                                hour0 = timeParts[0];
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Appointments app = postSnapshot.getValue(Appointments.class);
                                        if (app.getProviderID().equals(providerID)) {
                                            tp = app.getTime().split(":");
                                            String h0 = tp[0];
                                            dp = app.getDate().split("/");
                                            String d0 = dp[2];
                                            if (d0.equals(day0) && h0.equals(hour0)) {

                                                check = false;
                                                break;

                                            }
                                        }
                                        check = true;
                                    }
                                    if(check==false){
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(Booking.this);
                                        dialog.setMessage("Provider is not available at that time,choose another time!").setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = dialog.create();
                                        alert.setTitle("Note");
                                        alert.show();
                                    }
                                    if (check) {
                                        Intent intent = new Intent(Booking.this, ConformationActivity.class);
                                        intent.putExtra("prov_name", provName.getText());
                                        intent.putExtra("prov_loc", ProvdierLocation.getText());
                                        intent.putExtra("provSpecial", provSpecial.getText());
                                        intent.putExtra("prov_number", phone_number.getText());
                                        intent.putExtra("prov_id", providerID);
                                        intent.putExtra("Date", chooseDate.getText());
                                        intent.putExtra("time", chooseTime.getText());
                                        intent.putExtra("fees", providerFees.getText());
                                        startActivity(intent);

                                    }
                                } else {
                                    Intent intent = new Intent(Booking.this, ConformationActivity.class);
                                    intent.putExtra("prov_name", provName.getText());
                                    intent.putExtra("prov_loc", ProvdierLocation.getText());
                                    intent.putExtra("provSpecial", provSpecial.getText());
                                    intent.putExtra("prov_number", phone_number.getText());
                                    intent.putExtra("prov_id", providerID);
                                    intent.putExtra("Date", chooseDate.getText());
                                    intent.putExtra("time", chooseTime.getText());
                                    intent.putExtra("fees", providerFees.getText());
                                    startActivity(intent);

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }


            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Booking.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("proID", getIntent().getStringExtra("prov_id"));
        editor.apply();

    }

}
