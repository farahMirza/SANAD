package com.example.sanad;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyProfile extends AppCompatActivity {

    static TextView name;
    private TextView tFees, feesView, Email, email, phone, availableRang, FromDView, ToDView, fromHourView, ToHourView, twaitingTime, waitingTimeView, FromD, ToD, fromHour, ToHour;
    private EditText fees, waitingTime, Ephone;
    private Button Edit, save, tFromD, tToD, tfromHour, tToHour;
    private DatabaseReference database;
    private FirebaseUser user;
    private DatePickerDialog dpd;
    private Calendar c;
    private LinearLayout emailLayout;
    private boolean flag = false;
    private String type;
    private FirebaseStorage storage;
    private StorageReference storageRef, storageRefernce;
    private ImageView profilePicture;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    String feesTempVar, waitingTempVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        profilePicture = (ImageView) findViewById(R.id.profile_image);
        tFees = (TextView) findViewById(R.id.Txt_profile_fees);
        fees = (EditText) findViewById(R.id.profile_fees);
        feesView = (TextView) findViewById(R.id.view_fees);
        availableRang = (TextView) findViewById(R.id.view_DateRange);
        name = (TextView) findViewById(R.id.txt_profile_name);
        FromDView = (TextView) findViewById(R.id.view_fromD);
        ToDView = (TextView) findViewById(R.id.view_toDay);
        fromHourView = (TextView) findViewById(R.id.view_fromH);
        ToHourView = (TextView) findViewById(R.id.view_toH);
        waitingTimeView = (TextView) findViewById(R.id.view_waiting_time);
        Email = (TextView) findViewById(R.id.view_mail);
        email = (TextView) findViewById(R.id.Txt_profile_mail);
        phone = (TextView) findViewById(R.id.Txt_profile_number);
        Ephone = (EditText) findViewById(R.id.profile_number);
        FromD = (TextView) findViewById(R.id.profile_fromD);
        ToD = (TextView) findViewById(R.id.profile_toDay);
        fromHour = (TextView) findViewById(R.id.profile_fromH);
        ToHour = (TextView) findViewById(R.id.profile_toH);
        waitingTime = (EditText) findViewById(R.id.profile_waiting_time);
        twaitingTime = (TextView) findViewById(R.id.Txt_profile_waiting_time);
        tFromD = (Button) findViewById(R.id.Bprofile_fromD);
        tToD = (Button) findViewById(R.id.Bprofile_toDay);
        tfromHour = (Button) findViewById(R.id.Bprofile_fromH);
        tToHour = (Button) findViewById(R.id.Bprofile_toH);
        Edit = (Button) findViewById(R.id.view_editBtn);
        save = (Button) findViewById(R.id.view_saveBtn);
        emailLayout=(LinearLayout)findViewById(R.id.email_layout);
        //change pro pic
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (save.getVisibility() == View.VISIBLE) {
                    chooseImage();
                } else {
                    Toast.makeText(MyProfile.this, "Click Edit button to change photo", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //profile pic
        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = storage.getReferenceFromUrl("gs://sanad-7d768.appspot.com/images").child(user.getUid());
        try {
            final File localFile = File.createTempFile("images", "jpg");
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
        //////////////////////////
        save.setVisibility(View.GONE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyProfile.this);
        type = prefs.getString("TYPE", "");
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        tFromD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(MyProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        tFromD.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 7 * (24 * 60 * 60 * 1000L));
                dpd.show();
            }
        });

        tToD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(MyProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        tToD.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 7 * (24 * 60 * 60 * 1000L));
                dpd.show();
            }
        });
        tfromHour.setOnClickListener(new View.OnClickListener() {
            Calendar calender = Calendar.getInstance();
            int hour = calender.get(Calendar.HOUR);
            int minute = calender.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(MyProfile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tfromHour.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                tp.show();
            }
        });
        tToHour.setOnClickListener(new View.OnClickListener() {
            Calendar calender = Calendar.getInstance();
            int hour = calender.get(Calendar.HOUR);
            int minute = calender.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(MyProfile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tToHour.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                tp.show();
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user = FirebaseAuth.getInstance().getCurrentUser();
                Users u = dataSnapshot.getValue(Users.class);
                if (u.getTYPE().equals("patient")) {
                    flag = true;
                    name.setText(u.getNAME());
                    if (!user.getEmail().equals("")) {
                        emailLayout.setVisibility(View.VISIBLE);
                        email.setText(user.getEmail());
                    }

                    phone.setText(u.getMOBILE());
                    feesView.setVisibility(View.GONE);
                    waitingTimeView.setVisibility(View.GONE);
                    fromHourView.setVisibility(View.GONE);
                    ToHourView.setVisibility(View.GONE);
                    FromDView.setVisibility(View.GONE);
                    ToDView.setVisibility(View.GONE);
                    availableRang.setVisibility(View.GONE);

                } else if (u.getTYPE().equals("doctor")) {
                    name.setText(u.getNAME());
                    if (!user.getEmail().equals("")) {
                        emailLayout.setVisibility(View.VISIBLE);
                        email.setText(user.getEmail());
                    }

                    phone.setText(u.getMOBILE());
                    tFees.setText(u.getFees() + "JD");
                    feesTempVar = u.getFees();
                    waitingTempVar = u.getWaiting_time();
                    twaitingTime.setText(u.getWaiting_time() + "Minutes");
                    fromHour.setText(u.getFromHour());
                    ToHour.setText(u.getToHour());
                    FromD.setText(u.getFromday());
                    ToD.setText(u.getToDay());
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    Edit.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    tFees.setVisibility(View.GONE);
                    twaitingTime.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);
                    Ephone.setVisibility(View.VISIBLE);
                    Ephone.setText(phone.getText());


                } else if (flag == false) {
                    Edit.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    tFees.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);
                    Ephone.setVisibility(View.VISIBLE);
                    Ephone.setText(phone.getText());
                    fees.setVisibility(View.VISIBLE);
                    fees.setText(feesTempVar);
                    twaitingTime.setVisibility(View.GONE);
                    waitingTime.setVisibility(View.VISIBLE);
                    waitingTime.setText(waitingTempVar);
                    tFromD.setVisibility(View.VISIBLE);
                    tToD.setVisibility(View.VISIBLE);
                    tToHour.setVisibility(View.VISIBLE);
                    tfromHour.setVisibility(View.VISIBLE);
                    FromD.setVisibility(View.GONE);
                    ToD.setVisibility(View.GONE);
                    ToHour.setVisibility(View.GONE);
                    fromHour.setVisibility(View.GONE);
                    tFromD.setText(FromD.getText());
                    tToD.setText(ToD.getText());
                    tToHour.setText(ToHour.getText());
                    tfromHour.setText(fromHour.getText());
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    database.child("mobile").setValue(Ephone.getText().toString());
                    phone.setVisibility(View.VISIBLE);
                    phone.setText(Ephone.getText());
                    Ephone.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    Edit.setVisibility(View.VISIBLE);
                    uploadImage();
                    Toast.makeText(MyProfile.this, "information have been changed", Toast.LENGTH_SHORT).show();


                } else if (flag == false) {
                    database.child("mobile").setValue(Ephone.getText().toString());
                    database.child("fees").setValue(fees.getText().toString());
                    database.child("fromHour").setValue(tfromHour.getText().toString());
                    database.child("fromday").setValue(tFromD.getText().toString());
                    database.child("toDay").setValue(tToD.getText().toString());
                    database.child("toHour").setValue(tToHour.getText().toString());
                    database.child("waiting_time").setValue(waitingTime.getText().toString());
                    phone.setVisibility(View.VISIBLE);
                    phone.setText(Ephone.getText());
                    Ephone.setVisibility(View.GONE);
                    tfromHour.setVisibility(View.GONE);
                    tFromD.setVisibility(View.GONE);
                    tToHour.setVisibility(View.GONE);
                    tToD.setVisibility(View.GONE);
                    fromHour.setVisibility(View.VISIBLE);
                    FromD.setVisibility(View.VISIBLE);
                    ToHour.setVisibility(View.VISIBLE);
                    ToD.setVisibility(View.VISIBLE);
                    fromHour.setText(tfromHour.getText());
                    FromD.setText(tFromD.getText());
                    ToD.setText(tToD.getText());
                    ToHour.setText(tToHour.getText());
                    fees.setVisibility(View.GONE);
                    tFees.setVisibility(View.VISIBLE);
                    tFees.setText(fees.getText());
                    waitingTime.setVisibility(View.GONE);
                    twaitingTime.setVisibility(View.VISIBLE);
                    twaitingTime.setText(waitingTime.getText());
                    save.setVisibility(View.GONE);
                    Edit.setVisibility(View.VISIBLE);
                    uploadImage();
                    Toast.makeText(MyProfile.this, "information have been changed", Toast.LENGTH_SHORT).show();


                }
            }

        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            user = FirebaseAuth.getInstance().getCurrentUser();
            storageRefernce = storage.getReference();
            StorageReference ref = storageRefernce.child("images/" + user.getUid().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MyProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MyProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

}
