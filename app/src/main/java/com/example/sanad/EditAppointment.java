package com.example.sanad;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class EditAppointment extends AppCompatActivity {
    private TextView name, special, provLoc, pateLoc, prov_num;
    private Button date, time, save;
    private Calendar c;
    private FirebaseUser user;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);
        name = (TextView) findViewById(R.id.proName);
        special = (TextView) findViewById(R.id.proSpec);
        provLoc = (TextView) findViewById(R.id.proLoc);
        pateLoc = (TextView) findViewById(R.id.pateLoc);
        prov_num = (TextView) findViewById(R.id.proNum);
        date = (Button) findViewById(R.id.date);
        time = (Button) findViewById(R.id.Time);
        save = (Button) findViewById(R.id.save_appointment);

        name.setText(getIntent().getStringExtra("prov_name"));
        special.setText(getIntent().getStringExtra("prov_specialty"));
        provLoc.setText(getIntent().getStringExtra("prov_loc"));
        pateLoc.setText(getIntent().getStringExtra("patient_loc"));
        prov_num.setText(getIntent().getStringExtra("prov_num"));
        date.setText(getIntent().getStringExtra("date"));
        time.setText(getIntent().getStringExtra("time"));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(EditAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            Calendar calender = Calendar.getInstance();
            int hour = calender.get(Calendar.HOUR);
            int minute = calender.get(Calendar.MINUTE);

            @Override
            public void onClick(View v) {
                TimePickerDialog tp = new TimePickerDialog(EditAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
                tp.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user= FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments")
                        .child(getIntent().getStringExtra("prov_id") + user.getUid());
                ref.setValue(null);
                user = FirebaseAuth.getInstance().getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference("Appointments");
                Appointments point = new Appointments(user.getUid(), getIntent().getStringExtra("prov_id"),
                        getIntent().getStringExtra("prov_name"),MyProfile.name.getText().toString(),
                        getIntent().getStringExtra("prov_specialty"),
                        date.getText().toString(),
                        time.getText().toString(),
                        getIntent().getStringExtra("prov_loc"),
                        getIntent().getStringExtra("patient_loc"),
                        getIntent().getStringExtra("prov_num"),0,0);
                ref.child(getIntent().getStringExtra("prov_id") + user.getUid()).setValue(point);
                Toast.makeText(EditAppointment.this, "changes have been saved", Toast.LENGTH_SHORT).show();


            }
        });


    }
}
