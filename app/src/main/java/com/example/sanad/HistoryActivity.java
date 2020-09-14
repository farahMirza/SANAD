package com.example.sanad;

import android.os.Bundle;

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

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    public static final String THERE_IS_NO_HISTORY = "there is no history!";
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private ListView list;
    private static ArrayList<History> array;
    private FirebaseUser user;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        list = (ListView) findViewById(R.id.history_list);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("History");
        array = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    History h = postSnapshot.getValue(History.class);
                    if (user.getUid().toString().equals(h.getPatID()) || user.getUid().toString().equals(h.getProvId())) {
                        History history = new History(h.getProvId(), h.getPatID(), h.getDate(), h.getTime(), h.getPatLocation(), h.getPatName(), h.getProName());
                        array.add(history);
                    }

                }

                adapter = new HistoryAdapter(HistoryActivity.this, array);
                list.setAdapter(adapter);
                if(array.isEmpty())
                Toast.makeText(HistoryActivity.this, THERE_IS_NO_HISTORY, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
