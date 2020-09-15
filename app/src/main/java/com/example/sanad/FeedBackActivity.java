package com.example.sanad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class FeedBackActivity extends AppCompatActivity {
    private ArrayList<Feed> mArrayList;
    private ListView list;
    private DatabaseReference myRef;
    private String ProvID;
    private FirebaseUser user;
    private FeedBackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        list = (ListView) findViewById(R.id.previewLV);
        user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ProvID = prefs.getString("proID", "");
        myRef = FirebaseDatabase.getInstance().getReference("FeedBack").child(ProvID);
        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s){
                mArrayList=new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds1 : dataSnapshot.getChildren()){
                        Feed feed = ds1.getValue(Feed.class);
                 Feed f = new Feed(feed.getName(), feed.getComment(), feed.getRate());
                   mArrayList.add(f);
                    }
                }

                adapter = new FeedBackAdapter(FeedBackActivity.this, mArrayList);
                list.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                mArrayList = new ArrayList<>();
//                mArrayList.clear();

//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                }

//                if (mArrayList.isEmpty())
//                    Toast.makeText(FeedBackActivity.this, "There is no feedback", Toast.LENGTH_SHORT).show();


            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("places", "Failed to read value.", error.toException());
            }
        });

    }
}
