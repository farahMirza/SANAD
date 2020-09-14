package com.example.sanad.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sanad.AppointmentAdapter;
import com.example.sanad.Appointments;
import com.example.sanad.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import androidx.fragment.app.Fragment;

public class AppointmentsFragment extends Fragment {


    private ListView lst;
    private ArrayList<Appointments> appointList;
    private FirebaseUser user;
    private DatabaseReference database;
    private ImageView icon, add;
    private ImageButton phone;
    private TextView txt;
    private Button cancel;
    private String key;
    private Context context;
    private BottomNavigationView nav;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appointment_fragment, container, false);
        lst = (ListView) view.findViewById(R.id.LList);
        user = FirebaseAuth.getInstance().getCurrentUser();
        icon = (ImageView) view.findViewById(R.id.no_appointment_icon);
        add = (ImageView) view.findViewById(R.id.add);
        phone = (ImageButton) view.findViewById(R.id.phone);
        txt = (TextView) view.findViewById(R.id.no_appointment_txt);
        icon.setVisibility(View.GONE);
        txt.setVisibility(View.GONE);
        nav = (BottomNavigationView) view.findViewById(R.id.bottom_nav);
        cancel = (Button) view.findViewById(R.id.ACBtn);
        database = FirebaseDatabase.getInstance().getReference("Appointments");
        appointList = new ArrayList<Appointments>();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Appointments point = postSnapshot.getValue(Appointments.class);
                    if (point != null)
                        //to check that the appoint belongs to patient or doctor
                        if (point.getPatientID().equals(user.getUid()) || point.getProviderID().equals(user.getUid()))
                            appointList.add(point);
                }
                if (appointList.isEmpty()) {
                    icon.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);
                }
                AppointmentAdapter adapter = new AppointmentAdapter(context, appointList);
                lst.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("", "Failed to read value.");

            }
        });


        return view;
    }


}
