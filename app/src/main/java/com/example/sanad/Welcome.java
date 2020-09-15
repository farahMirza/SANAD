package com.example.sanad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome extends AppCompatActivity {
    private Button patient, provider;
    private ImageView pa, pro;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        patient = (Button) findViewById(R.id.patient);
        pa = (ImageView) findViewById(R.id.pa);
        pro = (ImageView) findViewById(R.id.pro);
        provider = (Button) findViewById(R.id.provider);
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, set_information.class);
                intent.putExtra("TYPE", "patient");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Welcome.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TYPE", "patient");
                editor.commit();
                type = "patient";
                startActivity(intent);
                finish();
            }
        });
        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, set_information.class);
                intent.putExtra("TYPE", "doctor");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Welcome.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TYPE", "doctor");
                editor.commit();
                type = "doctor";
                startActivity(intent);
                finish();

            }
        });
        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, set_information.class);
                intent.putExtra("TYPE", "patient");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Welcome.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TYPE", "patient");
                editor.apply();
                type = "patient";
                startActivity(intent);
                finish();
            }
        });
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, set_information.class);
                intent.putExtra("TYPE", "doctor");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Welcome.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TYPE", "doctor");
                editor.apply();
                type = "doctor";
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      finish();
    }
}

