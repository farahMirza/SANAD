package com.example.sanad;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class CareProvidersPosts extends AppCompatActivity implements OnItemClickListener {
    private ListView lst;
    private ArrayList<Users> userList;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private RadioGroup rdGroup;
    private RadioButton rdButn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_providers_posts);
        lst = (ListView) findViewById(R.id.CareProvider_List);
        rdGroup = (RadioGroup) findViewById(R.id.radio_group);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Users");
        userList = new ArrayList<Users>();
        if (rdGroup != null) {
            int selected = rdGroup.getCheckedRadioButtonId();
            rdButn = (RadioButton) findViewById(selected);

        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Users user = postSnapshot.getValue(Users.class);
                    if (user.getTYPE().equals("doctor")) {
                        if (user.getSpecialization().equals(getIntent().getStringExtra("providerType")) && user.getADDRESS().equals(getIntent().getStringExtra("providerLocation")))
                            userList.add(user);
                    }

                }

                if (userList.isEmpty()) {
                    Toast.makeText(CareProvidersPosts.this, "doctor not found", Toast.LENGTH_SHORT).show();
                }
                CareProviderAdapter adapter = new CareProviderAdapter(CareProvidersPosts.this, userList);
                lst.setAdapter(adapter);
                lst.setOnItemClickListener(CareProvidersPosts.this);

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("places", "Failed to read value.", error.toException());
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Users provider = userList.get(position);
        Intent i = new Intent(CareProvidersPosts.this, Booking.class);
        i.putExtra("ProviderId", provider.getID());
        String msg = provider.getFromday() + "-" + provider.getToDay() + "\n From Hour:" + provider.getFromHour() + "-" + provider.getToHour();
        i.putExtra("ProviderAvailability", msg);
        startActivity(i);

    }

    public void onClickRadioButn(View view)
    {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.femaleBtn:
                if (checked) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Users user = postSnapshot.getValue(Users.class);
                                if (user.getTYPE().equals("doctor")) {
                                    if (user.getSpecialization().equals(getIntent().getStringExtra("providerType")) && user.getADDRESS().equals(getIntent().getStringExtra("providerLocation")) && user.getGENDER().equals("Female"))
                                        userList.add(user);
                                }

                            }

                            CareProviderAdapter adapter = new CareProviderAdapter(CareProvidersPosts.this, userList);
                            lst.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                break;
            case R.id.maleBtn:
                if (checked) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Users user = postSnapshot.getValue(Users.class);
                                if (user.getTYPE().equals("doctor")) {
                                    if (user.getSpecialization().equals(getIntent().getStringExtra("providerType")) && user.getADDRESS().equals(getIntent().getStringExtra("providerLocation")) && user.getGENDER().equals("Male"))
                                        userList.add(user);
                                }

                            }

                            CareProviderAdapter adapter = new CareProviderAdapter(CareProvidersPosts.this, userList);
                            lst.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                break;
        }
    }
}



