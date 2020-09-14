package com.example.sanad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.example.sanad.Fragments.AppointmentsFragment;
import com.example.sanad.Fragments.HomeFragment;
import com.example.sanad.Fragments.MoreFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.onesignal.OneSignal;

public class SignIn extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    private BottomNavigationView nav;
    static String type;
    private FirebaseUser user;
    private DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        type = getIntent().getStringExtra("TYPE");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignIn.this);
        SharedPreferences.Editor editor = prefs.edit();
        user = FirebaseAuth.getInstance().getCurrentUser();
        editor.putString("Type", getIntent().getStringExtra("TYPE"));
        editor.putString("ID", user.getUid());
        editor.apply();
        nav = (BottomNavigationView) findViewById(R.id.bottom_nav);


        nav.setOnNavigationItemSelectedListener(this);


        displayFragment(new HomeFragment());
    }
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativelayout, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.appointment_menu:
                fragment = new AppointmentsFragment();

                break;

            case R.id.more_menu: {
                fragment = new MoreFragment();
            }
            break;
        }
        if (fragment != null) {
            displayFragment(fragment);
        }
        return false;
    }
    public void choose_speciality(View v) {
        Intent intent = new Intent(this, Categories.class);
        startActivity(intent);
    }
    public void choose_proName(View v) {
        Intent intent = new Intent(this, ChooseByProName.class);
        startActivity(intent);
    }
    public void signout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        startActivity(intent);
                        OneSignal.deleteTag("User_ID");
                    }
                });

    }
    public void agenda(View view) {
        Intent intent = new Intent(SignIn.this, Agenda.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

}