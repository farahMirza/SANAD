package com.example.sanad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import com.firebase.ui.auth.AuthUI;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean flag = true;
    static String LoggedIn_User_Email;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("jo").build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate data base
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    // Check if user is signed in (non-null) and update UI accordingly.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);
        if (currentUser != null) {
            FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                // The user is new, show them a fancy intro screen!
                Intent intent = new Intent(this, Welcome.class);
                startActivity(intent);
                finish();
            } else {
                // This is an existing user, show them a welcome back screen.
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
            }

        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.sa_logo)      // Set logo drawable
                            // Set theme
                            .build(),
                    RC_SIGN_IN);


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                // you could determine if the user who just signed in is an existing or new one by comparing the user's creation and last sign-in time
                FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    // The user is new, show them a fancy intro screen!
                    Intent intent = new Intent(this, Welcome.class);
                    startActivity(intent);
                    finish();
                } else {
                    // This is an existing user, show them a welcome back screen.
                    Intent intent = new Intent(this, SignIn.class);
                    startActivity(intent);
                    finish();
                }


            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "sign in problem", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "sign in problem", Toast.LENGTH_SHORT).show();

                    return;
                }

                Toast.makeText(this, "unknown error", Toast.LENGTH_SHORT).show();

                Log.e("hk", "Sign-in error: ", response.getError());
            }
        }
    }
}