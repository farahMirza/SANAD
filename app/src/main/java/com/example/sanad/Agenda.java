package com.example.sanad;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class Agenda extends AppCompatActivity {
    private ListView lst;
    private DatabaseReference ref;
 //    static int hour, minute;
    static int hour, minute;
   private Location location;
    private FirebaseUser user;
    private String patId;
    private String date0, time0;
    private String addr;
    static ArrayList<Appointments> appoints;

   long count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
       lst=(ListView) findViewById(R.id.agendaID);
       user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Appointments");
       // Read from the database

      appoints = new ArrayList<>();



        ref.addValueEventListener(new ValueEventListener() {

           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                   Appointments appointments = postsnapshot.getValue(Appointments.class);
                   if (appointments.getProviderID().equals(user.getUid())) {
                        appoints.add(appointments);
                        addr = appointments.getPate_location();
                        date0 = appointments.getDate();
                        time0 = appointments.getTime();
                        patId = appointments.getPatientID();
                        String[] dateParts = appointments.getDate().split("/");
                        String day0 = dateParts[2];
                        int day = Integer.parseInt(day0);
                        String month0 = dateParts[1];
                        int month = Integer.parseInt(month0);
                        String year0 = dateParts[0];
                       int year = Integer.parseInt(year0);
                        Calendar c = Calendar.getInstance();
                        int day2 = c.get(Calendar.DAY_OF_MONTH);
                        if (day != day2) {
                            appoints.remove(appointments);
                            continue;
                        }




                    }
                }
               AgendaAdapter adapter = new AgendaAdapter(Agenda.this, appoints);
               lst.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               Log.w("", "Failed to read value.", error.toException());
            }
        });







    }


}
