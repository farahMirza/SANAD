package com.example.sanad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RatingProvider extends AppCompatActivity {
    private Button rate;
    private RatingBar bar;
    private TextView providerName, fees, users;
    private DatabaseReference myRef, ref, feedRef, patient;
    private String ProvName, ProvID, ProvFees;
    private float barRate;
    private FirebaseUser user;
    private EditText feed;
    private static RatePro newR;
    private ImageView picture;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_provider);

        picture = (ImageView) findViewById(R.id.pr_image);
        rate = (Button) findViewById(R.id.submit);
        bar = (RatingBar) findViewById(R.id.ratingBar);
        providerName = (TextView) findViewById(R.id.prov);
        fees = (TextView) findViewById(R.id.provFee);
        users = (TextView) findViewById(R.id.userCount);
        feed = (EditText) findViewById(R.id.feedbackComment);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ProvName = prefs.getString("provName", "");
        ProvID = prefs.getString("provID", "");
        ProvFees = prefs.getString("Fees", "");
        //doctor picture
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sanad-7d768.appspot.com/images").child(ProvID);
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    picture.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }
        newR = new RatePro();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Rating").child(ProvID);
        patient = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        feedRef = FirebaseDatabase.getInstance().getReference("FeedBack").child(ProvID).child(user.getUid());
        ref = FirebaseDatabase.getInstance().getReference("Rating").child(ProvID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RatePro r = dataSnapshot.getValue(RatePro.class);
                if (r != null) {
                    users.setText(r.getCount() + "");
                } else users.setText("0");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        providerName.setText(ProvName);
        fees.setText(ProvFees);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                barRate = v;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RatePro r = dataSnapshot.getValue(RatePro.class);
                        if (r == null) {
                            int count = 1;
                            newR = new RatePro(count, barRate);


                        } else {

                            int count = r.getCount() + 1;
                            float rating = (r.getCount() * r.getRating() + barRate) / count;
                            newR = new RatePro(count, rating);


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("", "Failed to read value.", error.toException());
                    }
                });
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                patient.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users u = dataSnapshot.getValue(Users.class);

                        feedRef.child("name").setValue(u.getNAME().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if (feed.getText() != null)
                    feedRef.child("comment").setValue(feed.getText().toString());
                feedRef.child("rate").setValue(barRate);
                ref.setValue(newR);
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
