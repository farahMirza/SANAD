package com.sanad.farah.sanad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Providers extends AppCompatActivity {
    private ListView list;
    private ArrayList<ProviderItem> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);
        list = (ListView) findViewById(R.id.listview);
        mArrayList = new ArrayList<>();
//        mArrayList.add(new ProviderItem(R.drawable.doctor2, "General Checking"));
//        mArrayList.add(new ProviderItem(R.drawable.nurse2, "Nurse"));
        mArrayList.add(new ProviderItem(R.drawable.ribbon, "PALLIATIVE CARE"));
        mArrayList.add(new ProviderItem(R.drawable.psychologist, "PSYCHOTHERAPIST"));
        mArrayList.add(new ProviderItem(R.drawable.disabled, "MULTIPLE SCLEROSIS CARE"));
        mArrayList.add(new ProviderItem(R.drawable.abc_block, "AUTISM CARE"));
        mArrayList.add(new ProviderItem(R.drawable.ear, "SPEECH THERAPY"));
        mArrayList.add(new ProviderItem(R.drawable.autism, "HEARING THERAPY"));


        ProvidersAdapter adapter = new ProvidersAdapter(this, mArrayList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProviderItem item = mArrayList.get(position);
                Intent intent = new Intent(getApplicationContext(), ProviderPlace.class);
                intent.putExtra("ProviderType", item.getProvider_name());
                startActivity(intent);

            }
        });
    }
}
