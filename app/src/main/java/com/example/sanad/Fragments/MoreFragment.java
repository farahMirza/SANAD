package com.example.sanad.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sanad.About;
import com.example.sanad.HistoryActivity;
import com.example.sanad.MyProfile;
import com.example.sanad.R;
import com.example.sanad.RatePro;
import com.example.sanad.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    private static final String TAG = "More Fragment";
    private TextView profile, about, history;
    private String type;
    private TextView t1,myRate;
    private Button myAgenda;
    private Dialog rankDialog;
    private RatingBar ratingBar;
    private DatabaseReference rateRef,uRef;
    private FirebaseUser user;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_fragment, container, false);

        profile = (TextView) view.findViewById(R.id.my_profile);
        history = (TextView) view.findViewById(R.id.history_btn);
        user = FirebaseAuth.getInstance().getCurrentUser();

       myRate=(TextView) view.findViewById(R.id.myRate);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Read from the database
                Intent intent = new Intent(getContext(), MyProfile.class);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        about = (TextView) view.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), About.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        uRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
     uRef.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             Users u = dataSnapshot.getValue(Users.class);
             if(u.getTYPE().equals("doctor")){
                 rankDialog = new Dialog(getContext(), R.style.FullHeightDialog);

                 myRate.setVisibility(View.VISIBLE);
                 myRate.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         rateRef = FirebaseDatabase.getInstance().getReference("Rating").child(user.getUid());
                         rateRef.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 RatePro rate = dataSnapshot.getValue(RatePro.class);


                                 rankDialog.setContentView(R.layout.rank_dialog);
                                 rankDialog.setCancelable(true);
                                 ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingPro);
                                 t1 = (TextView) rankDialog.findViewById(R.id.t3);

                                 if (rate == null) {
                                     ratingBar.setRating(1);
                                     t1.setText("0");
                                 } else {
                                     ratingBar.setRating(rate.getRating());
                                     t1.setText(rate.getCount() + "");
                                 }


                                 Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                 updateButton.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         rankDialog.dismiss();
                                     }
                                 });
                                 //now that the dialog is set up, it's time to show it
                                 rankDialog.show();

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });
                     }
                 });
             }

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });

        return view;
    }
}
