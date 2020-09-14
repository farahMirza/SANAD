package com.example.sanad.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.sanad.Agenda;
import com.example.sanad.R;
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

public class HomeFragment extends Fragment {
    private DatabaseReference ref, rateRef;
    private FirebaseUser user;
    private LinearLayout patientLayout;
    private TableLayout providerLayout;
    private ImageView pateintImg;
    private ImageView providerImg;
    private Button myAgenda, myRate;
    private Dialog rankDialog;
    private RatingBar ratingBar;
    private TextView t1;
    private ImageView proAnim, femaleDoc, scientest, disable, suger, handicap;
    private Context context;
    private RelativeLayout relative, patRelative;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        patientLayout = (LinearLayout) view.findViewById(R.id.patient_layout);
        providerLayout = (TableLayout) view.findViewById(R.id.provider_layout);
        relative = (RelativeLayout) view.findViewById(R.id.relative);
        patRelative = (RelativeLayout) view.findViewById(R.id.patRelative);
        pateintImg = (ImageView) view.findViewById(R.id.patient_home_img);
        proAnim = (ImageView) view.findViewById(R.id.proAnim);
        femaleDoc = (ImageView) view.findViewById(R.id.female_doc);
        scientest = (ImageView) view.findViewById(R.id.scientest);
        providerImg = (ImageView) view.findViewById(R.id.provider_home_img);
        handicap = (ImageView) view.findViewById(R.id.handicap);
        suger = (ImageView) view.findViewById(R.id.suger);
        disable = (ImageView) view.findViewById(R.id.disable);

        myAgenda = (Button) view.findViewById(R.id.myAgenda);
        myRate = (Button) view.findViewById(R.id.myRate);
        rankDialog = new Dialog(getContext(), R.style.FullHeightDialog);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users u = dataSnapshot.getValue(Users.class);
                if (u.getTYPE().equals("doctor")) {

                    patientLayout.setVisibility(View.GONE);
                    providerLayout.setVisibility(View.VISIBLE);
                    pateintImg.setVisibility(View.GONE);
                    providerImg.setVisibility(View.VISIBLE);
                    relative.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(context, R.anim.righttoleft);
                    Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.lefttoright);
                    proAnim.startAnimation(anim2);
                    femaleDoc.startAnimation(anim);
                    scientest.startAnimation(anim);

                } else {
                    Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.lefttoright);
                    Animation animation3 = AnimationUtils.loadAnimation(context, R.anim.fadein);
                    handicap.startAnimation(animation1);
                    disable.startAnimation(animation2);
                    suger.startAnimation(animation3);
                    patRelative.setVisibility(View.VISIBLE);
                    relative.setVisibility(View.GONE);
                    patientLayout.setVisibility(View.VISIBLE);
                    providerLayout.setVisibility(View.GONE);
                    pateintImg.setVisibility(View.VISIBLE);
                    providerImg.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Agenda.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
