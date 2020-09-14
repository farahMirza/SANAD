package com.example.sanad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class set_information extends AppCompatActivity {
    public static final String USER_MUST_BE_18_OR_ABOVE = "user must be 18 or above!";
    public static final String ENTER_NAME = "Enter Name";
    public static final String ENTER_ALL_YOUR_INFORMATIONS = "Enter all your informations";
    public static final String INFORMATION_HAVE_BEEN_ADDED = "information have been added";
    public static final String ENTER_PHONE_NUMBER = "Enter Phone Number";
    public static final String SELECT_BIRTH_DATE = "Select BirthDate";
    private DatePickerDialog dpd;
    private Calendar c;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button BDate, submit_button, fromDay, toDay, fromHour, toHour;
    private RadioGroup rd;
    private RadioButton radiobtn;
    private EditText phone_number, fees, description, user_name, waiting;
    private DatabaseReference ref;
    private TextView dateRange, special_text, fromD, toD, toH, fromH;
    private Users u;
    private ArrayList<String> locationArray;
    private ArrayAdapter<String> adapter;
    private Spinner location, specialization;
    private String address, special;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ArrayList<String> specializationArray;
    private ImageView image_pickerBtn;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_information);
        image_pickerBtn = (ImageView) findViewById(R.id.user_image);
        BDate = (Button) findViewById(R.id.birth_Date);
        location = (Spinner) findViewById(R.id.location);
        rd = (RadioGroup) findViewById(R.id.radio_group);
        phone_number = (EditText) findViewById(R.id.phone_number);
        fromH = (TextView) findViewById(R.id.fromH);
        fromD = (TextView) findViewById(R.id.fromD);
        toD = (TextView) findViewById(R.id.toD);
        toH = (TextView) findViewById(R.id.toH);
        special_text = (TextView) findViewById(R.id.specialization_text);
        //picker---------------------------------
        fromDay = (Button) findViewById(R.id.from_day);
        toDay = (Button) findViewById(R.id.today);
        fromHour = (Button) findViewById(R.id.from_hour);
        toHour = (Button) findViewById(R.id.to_hour);
        //---------------------------------------------
        submit_button = (Button) findViewById(R.id.database_button);
        fees = (EditText) findViewById(R.id.fees);
        specialization = (Spinner) findViewById(R.id.specialized);
        dateRange = (TextView) findViewById(R.id.DateRange);
        user_name = (EditText) findViewById(R.id.user_name);
        description = (EditText) findViewById(R.id.user_description);
        waiting = (EditText) findViewById(R.id.waiting_time);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        image_pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Specialization");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                specializationArray = (ArrayList<String>) dataSnapshot.getValue();
                adapter = new ArrayAdapter<String>(set_information.this, android.R.layout.simple_spinner_item, specializationArray);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                specialization.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("places", "Failed to read value.", error.toException());
            }
        });
        specialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                special = specializationArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Places");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                locationArray = (ArrayList<String>) dataSnapshot.getValue();
                adapter = new ArrayAdapter<String>(set_information.this, android.R.layout.simple_spinner_item, locationArray);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                location.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("places", "Failed to read value.", error.toException());
            }
        });
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                address = locationArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //read users
        String str = getIntent().getStringExtra("TYPE");
        if (str.equals("patient")) {
            fromHour.setVisibility(View.GONE);
            toHour.setVisibility(View.GONE);
            fromDay.setVisibility(View.GONE);
            toDay.setVisibility(View.GONE);
            specialization.setVisibility(View.GONE);
            fees.setVisibility(View.GONE);
            dateRange.setVisibility(View.GONE);
            waiting.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            fromH.setVisibility(View.GONE);
            fromD.setVisibility(View.GONE);
            toH.setVisibility(View.GONE);
            toD.setVisibility(View.GONE);
            special_text.setVisibility(View.GONE);


        }

        BDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(set_information.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        Calendar userAge = new GregorianCalendar(year, month, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, -18);
                        if (minAdultAge.before(userAge)) {
                            BDate.setText(SELECT_BIRTH_DATE);
                            Toast.makeText(set_information.this, USER_MUST_BE_18_OR_ABOVE, Toast.LENGTH_SHORT).show();
                        } else BDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                }, year, month, day);
                picker.show();


            }
        });


        //add information to database
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //radio button
                int selectedId = rd.getCheckedRadioButtonId();
                radiobtn = (RadioButton) findViewById(selectedId);
                //----------------------------------------
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                if (getIntent().getStringExtra("TYPE").equals("patient")) {
                    if ( user_name.getText().equals(ENTER_NAME)
                            || phone_number.getText().equals(ENTER_PHONE_NUMBER) || BDate.getText().equals(SELECT_BIRTH_DATE))
                    {
                        Toast.makeText(set_information.this, ENTER_ALL_YOUR_INFORMATIONS, Toast.LENGTH_SHORT).show();
                    } else {
                        Users u = new Users( user.getDisplayName(), null, phone_number.getText().toString(), user.getUid(), radiobtn.getText().toString(), BDate.getText().toString(), address, getIntent().getStringExtra("TYPE"));
                        ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("Users").child(user.getUid()).setValue(u);
                        Intent in = new Intent(set_information.this, SignIn.class);
                        in.putExtra("TYPE", "patient");
                        uploadImage();
                        startActivity(in);
                        Toast.makeText(set_information.this, INFORMATION_HAVE_BEEN_ADDED, Toast.LENGTH_LONG).show();
                    }
                } else if (getIntent().getStringExtra("TYPE").equals("provider")) {
                    if ( user_name.getText().equals(ENTER_NAME) || phone_number.getText().equals(ENTER_PHONE_NUMBER) || BDate.getText().equals(SELECT_BIRTH_DATE) || fromHour.getText().equals("choose hour") || fromDay.equals("choose day") || toHour.equals("choose hour") || toDay.equals("choose day") ||
                            fees.getText().equals("Fees") || waiting.getText().equals("Enter waiting time in minutes"))
                    {
                        Toast.makeText(set_information.this, ENTER_ALL_YOUR_INFORMATIONS, Toast.LENGTH_SHORT).show();
                    } else {
                        Users u = new Users( user_name.getText().toString(), null, phone_number.getText().toString(), user.getUid(), radiobtn.getText().toString(), BDate.getText().toString(), address, getIntent().getStringExtra("TYPE"), fees.getText().toString(), fromDay.getText().toString(), toDay.getText().toString(), fromHour.getText().toString(), toHour.getText().toString(), special, description.getText().toString(), waiting.getText().toString(), 0);
                        ref = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(user.getUid()).setValue(u);
                        Intent in = new Intent(set_information.this, SignIn.class);
                        in.putExtra("TYPE", "provider");
                        uploadImage();
                        startActivity(in);
                        Toast.makeText(set_information.this, INFORMATION_HAVE_BEEN_ADDED, Toast.LENGTH_LONG).show();

                    }
                }


            }

        });
        fromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(set_information.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        fromDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 7 * (24 * 60 * 60 * 1000L));
                dpd.show();
            }
        });

        toDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(set_information.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                        toDay.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + 7 * (24 * 60 * 60 * 1000L));
                dpd.show();
            }
        });
        fromHour.setOnClickListener(new View.OnClickListener() {
            Calendar calender = Calendar.getInstance();
            int hour = calender.get(Calendar.HOUR);
            int minute = calender.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(set_information.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        fromHour.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                tp.show();
            }
        });
        toHour.setOnClickListener(new View.OnClickListener() {
            Calendar calender = Calendar.getInstance();
            int hour = calender.get(Calendar.HOUR);
            int minute = calender.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(set_information.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        toHour.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                tp.show();
            }
        });


    }

    //profile pic
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //profile pic
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_pickerBtn.setImageBitmap(bitmap);
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

            StorageReference ref = storageReference.child("images/" + user.getUid().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(set_information.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(set_information.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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