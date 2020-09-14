package com.sanad.farah.sanad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseByProName extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ArrayList<Users> userList;
    private ListView lst;
    private SearchView mSearchView;
    CareProviderAdapter adapter;
    ArrayList<Users> tempArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_by_pro_name);
        lst = (ListView) findViewById(R.id.by_name_list);
        mSearchView=(SearchView)findViewById(R.id.search_name);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Users");
        userList = new ArrayList<Users>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Users user = postSnapshot.getValue(Users.class);
                    if (user.getTYPE().equals("provider")) {
                        userList.add(user);
                    }

                }
                if (userList.isEmpty()) {
                    Toast.makeText(ChooseByProName.this, "provider not found", Toast.LENGTH_SHORT).show();
                }
                 adapter = new CareProviderAdapter(ChooseByProName.this, userList);
                lst.setAdapter(adapter);

                lst.setOnItemClickListener(ChooseByProName.this);
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //search for a provider name

                        tempArrayList = new ArrayList<Users>();
                        for (Users c : userList) {
                            if (newText.equalsIgnoreCase(c.getNAME())) {
                                tempArrayList.add(c);
                            }
                        }
                        adapter = new CareProviderAdapter(ChooseByProName.this, tempArrayList);
                        lst.setAdapter(adapter);

                        return false;
                    }

                });
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
        Intent i = new Intent(ChooseByProName.this, Booking.class);
        i.putExtra("ProviderId", provider.getID());
        String msg = provider.getFromday() + "-" + provider.getToDay() + "From Hour:" + provider.getFromHour() + "-" + provider.getToHour();
        i.putExtra("ProviderAvailability", msg);
        startActivity(i);
    }


}
