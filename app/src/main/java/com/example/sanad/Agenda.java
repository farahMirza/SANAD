package com.example.sanad;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
//    private CalendarDayView dayView;
//    private DatabaseReference ref;
//    //    static int hour, minute;
//    static int hour, minute;
//    private Location location;
//    private FirebaseUser user;
//    private String patId;
//    private String date0, time0;
//    private String addr;
//    static ArrayList<Appointments> appoints;
//    private ArrayList<IEvent> events;
//    private ArrayList<IPopup> popups;
//    long count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
//        dayView = (CalendarDayView) findViewById(R.id.dayView);
//        dayView.setLimitTime(00, 24);
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        ref = FirebaseDatabase.getInstance().getReference("Appointments");
//        // Read from the database
//        popups = new ArrayList<>();
//        appoints = new ArrayList<>();
//        events = new ArrayList<>();
//
//
//        ref.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
//                    Appointments appointments = postsnapshot.getValue(Appointments.class);
//                    if (appointments.getProviderID().equals(user.getUid())) {
//                        appoints.add(appointments);
//                        addr = appointments.getPate_location();
//                        date0 = appointments.getDate();
//                        time0 = appointments.getTime();
//                        patId = appointments.getPatientID();
//                        String[] dateParts = appointments.getDate().split("/");
//                        String day0 = dateParts[2];
//                        int day = Integer.parseInt(day0);
//                        String month0 = dateParts[1];
//                        int month = Integer.parseInt(month0);
//                        String year0 = dateParts[0];
//                        int year = Integer.parseInt(year0);
//                        Calendar c = Calendar.getInstance();
//                        int day2 = c.get(Calendar.DAY_OF_MONTH);
//                        if (day != day2) {
//                            appoints.remove(appointments);
//                            continue;
//                        }
//
//                        int eventColor = ContextCompat.getColor(getApplicationContext(), R.color.midGray);
//                        Calendar timeStart = Calendar.getInstance();
//                        String[] time = appointments.getTime().split(":");
//                        String hour0 = time[0];
//                        hour = Integer.parseInt(hour0);
//                        String minute0 = time[1];
//                        minute = Integer.parseInt(minute0);
//                        timeStart.set(Calendar.HOUR_OF_DAY, hour);
//                        timeStart.set(Calendar.MINUTE, minute);
//                        Calendar timeEnd = (Calendar) timeStart.clone();
//                        timeEnd.set(Calendar.HOUR_OF_DAY, hour + 1);
//                        timeEnd.set(Calendar.MINUTE, minute);
//                        Event event = new Event(count, timeStart, timeEnd, "Appointment", appointments.getPate_location(), eventColor,appointments);
//                        Log.i("qasrawi2", "onDataChange: " +event.getId()+event.getP().getDate());
//                        Calendar ptimeStart = Calendar.getInstance();
//                        ptimeStart.set(Calendar.HOUR_OF_DAY, hour);
//                        ptimeStart.set(Calendar.MINUTE, minute);
//                        Calendar ptimeEnd = (Calendar) ptimeStart.clone();
//                        ptimeEnd.set(Calendar.HOUR_OF_DAY, hour + 1);
//                        ptimeEnd.set(Calendar.MINUTE, minute);
//                        Popup popup = new Popup();
//                        popup.setStartTime(ptimeStart);
//                        popup.setEndTime(ptimeEnd);
//                        popup.setImageStart("@drawable/bed.png");
//                        popup.setTitle(appointments.getPatientName());
//                        popup.setDescription(appointments.getPate_location());
//                        events.add(event);
//                       popups.add(popup);
//                        dayView.setEvents(events);
//                       dayView.setPopups(popups);
//                        count++;
//
//                    }
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("", "Failed to read value.", error.toException());
//            }
//        });
//
//
//        ((CdvDecorationDefault) (dayView.getDecoration())).setOnEventClickListener(
//                new EventView.OnEventClickListener() {
//                    @Override
//                    public void onEventClick(EventView view, IEvent data) {
//                        Event m = (Event)data;
//                        long id = m.getId();
//                        for(int i=0;i<events.size();i++){
//                            Event ev =(Event) events.get(i);
//                            if (ev.getId() == id ){
//                                Intent intent = new Intent(Agenda.this, ReachingPatient.class);
//                                intent.putExtra("patId", ev.getP().getPatientID());
//                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Agenda.this);
//                                SharedPreferences.Editor editor = prefs.edit();
//                                editor.putString("ApointTime", ev.getP().getTime());
//                                editor.putString("ApointDate", ev.getP().getDate());
//                                editor.putString("addrs", ev.getP().getPate_location());
//                                editor.apply();
//                                startActivity(intent);
//                            }
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onEventViewClick(View view, EventView eventView, IEvent data) {
//                        Event m = (Event)data;
//                        long id = m.getId();
//                        for(int i=0;i<events.size();i++){
//                            Event ev =(Event) events.get(i);
//                            if (ev.getId() == id ){
//                                Intent intent = new Intent(Agenda.this, ReachingPatient.class);
//                                intent.putExtra("patId", ev.getP().getPatientID());
//                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Agenda.this);
//                                SharedPreferences.Editor editor = prefs.edit();
//                                editor.putString("ApointTime", ev.getP().getTime());
//                                editor.putString("ApointDate", ev.getP().getDate());
//                                editor.putString("addrs", ev.getP().getPate_location());
//                                editor.apply();
//                                startActivity(intent);
//                            }
//                        }
//
//                        if (data instanceof Event) {
//                            // change event (ex: set event color)
//                            dayView.setEvents(events);
//                        }
//                    }
//                });
//
//        ((CdvDecorationDefault) (dayView.getDecoration())).setOnPopupClickListener(
//                new PopupView.OnEventPopupClickListener() {
//                    @Override
//                    public void onPopupClick(PopupView view, IPopup data) {
//
//
//                    @Override
//                    public void onQuoteClick(PopupView view, IPopup data) {
//                        Event m = (Event)data;
//                        Log.i("", "onEventClick: "+ m.getId());
//                        Toast.makeText(Agenda.this, data.getTitle(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onLoadData(PopupView view, ImageView start, ImageView end,
//                                           IPopup data) {
//                        start.setImageResource(R.drawable.pate);
//                    }
//                });
//

    }


}
