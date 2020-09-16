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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import java.util.Arrays;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.firebase.ui.auth.AuthUI;
import com.onesignal.OneSignal;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private String id;
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private boolean flag = true;
    static String LoggedIn_User_Email;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()

    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        //one signal code
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(getApplication()))
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler(getApplication()))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //////


        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);

        } else {
            //signing in
            startActivityForResult(AuthUI.getInstance().
                    createSignInIntentBuilder().
                    setAvailableProviders(providers).
                    setIsSmartLockEnabled(false).
                    setLogo(R.drawable.sa_logo).
                    build(), RC_SIGN_IN);


        }


    }



    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {


            if (resultCode == RESULT_OK) {


// Read from the database
                user = FirebaseAuth.getInstance().getCurrentUser();

                mDatabaseReference = mFirebaseDatabase.getReference("Users");

                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Intent intent = new Intent(MainActivity.this, Welcome.class);
                            startActivity(intent);
                        } else {
                            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                            OneSignal.sendTag("User_ID", user.getUid());
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Users u = postSnapshot.getValue(Users.class);
                                if (u.getID().equals(currentUser.getUid())) {

                                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                    LoggedIn_User_Email = u.getID();
                                    startActivity(intent);
                                    break;
                                } else {
                                    flag = false;
                                }
                                if (flag == false) {
                                    Intent intent = new Intent(MainActivity.this, Welcome.class);
                                    startActivity(intent);
                                }
                            }

                        }
                        user = FirebaseAuth.getInstance().getCurrentUser();

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("", "Failed to read value.", error.toException());
                    }
                });


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "signed in cancelled", Toast.LENGTH_LONG).show();
                finish();
                System.exit(0);

            }

        }


    }
}