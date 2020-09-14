package com.example.sanad;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ProviderPlace extends AppCompatActivity {
    private ListView lst;
    private static List<String> td;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_place);
        lst = (ListView) findViewById(R.id.country_listview);
        ArrayList<String> list = new ArrayList<String>();
        mSearchView = (SearchView) findViewById(R.id.search);


        // read a message for the database
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference("Places");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                td = (ArrayList<String>) dataSnapshot.getValue();
                adapter = new ArrayAdapter<String>(ProviderPlace.this, android.R.layout.simple_list_item_1, td);
                lst.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("places", "Failed to read value.", error.toException());
            }
        });


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String providerLocation = td.get(position);
                String providerType = getIntent().getStringExtra("ProviderType");
                Intent intent = new Intent(ProviderPlace.this, CareProvidersPosts.class);
                intent.putExtra("providerType", providerType);
                intent.putExtra("providerLocation", providerLocation);
                startActivity(intent);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search for a doctor area
                if (adapter != null)
                    adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


}
