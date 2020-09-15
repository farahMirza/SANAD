package com.example.sanad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Categories extends AppCompatActivity implements View.OnClickListener {
    Button generalCheck;
    Button nursing ;
    Button therapy ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        generalCheck=(Button)findViewById(R.id.gen_check);
        nursing=(Button)findViewById(R.id.nurse_services);
        therapy=(Button)findViewById(R.id.phys_thrapy);
nursing.setOnClickListener(this);










    }
    public void GeneralCheck(View v){
        Intent intent = new Intent(getApplicationContext(), ProviderPlace.class);
        intent.putExtra("ProviderType", generalCheck.getText());
        startActivity(intent);
    }

    public void physc(View v){
        Intent intent = new Intent(getApplicationContext(), ProviderPlace.class);
        intent.putExtra("ProviderType", therapy.getText());
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), Providers.class);
        intent.putExtra("ProviderType", nursing.getText());
        startActivity(intent);
    }
}
