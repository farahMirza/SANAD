package com.example.sanad;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;

public class CareProviderAdapter extends ArrayAdapter<Users> {
    private float rate;
    private RatingBar bar;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView iconView;

    public CareProviderAdapter(Activity context, ArrayList<Users> mUser) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for more than view, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, mUser);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.care_provider_lv_post, parent, false);
        }

//putting images of care providers to the list
        Users currentCareProviderItem = getItem(position);
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sanad-7f6ef.appspot.com/images").child(currentCareProviderItem.getID());
        iconView = (ImageView) listItemView.findViewById(R.id.user_image);
        iconView.setImageResource(R.drawable.doctor);

        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    iconView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
            iconView.setImageResource(R.drawable.doctor);
        }


        TextView providerTextView = (TextView) listItemView.findViewById(R.id.provider_name);
        providerTextView.setText(currentCareProviderItem.getNAME());


        TextView discription = (TextView) listItemView.findViewById(R.id.description);
        discription.setText(currentCareProviderItem.getDescription());
        bar = (RatingBar) listItemView.findViewById(R.id.rateBar);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Rating").child(currentCareProviderItem.getID());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //putting rating of care providers to list
                RatePro r = dataSnapshot.getValue(RatePro.class);
                if (r != null) {
                    bar.setRating(r.getRating());
                } else {
                    bar.setRating(1);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });



        return listItemView;
    }

}
