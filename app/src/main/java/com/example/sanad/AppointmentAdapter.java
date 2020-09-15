package com.example.sanad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import androidx.annotation.NonNull;
public class AppointmentAdapter extends ArrayAdapter<Appointments> {
    Appointments currentAppointmentItem;
    Button cancel;

    public AppointmentAdapter(Context context, ArrayList<Appointments> mAppoint) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for more than a view, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, mAppoint);
    }

    private void sendNotification() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String send_email;
                    String msg = user.getDisplayName() + " have been canceld his appointment";
                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    if (MainActivity.LoggedIn_User_Email != null)
                        if (MainActivity.LoggedIn_User_Email.equals(currentAppointmentItem.getPatientID())) {
                            send_email = currentAppointmentItem.getProviderID();

                        } else {
                            send_email = currentAppointmentItem.getPatientID();
                        }
                    else send_email = currentAppointmentItem.getProviderID();

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic OGUzYWYzMTItZjI1NC00ODk2LTllMzEtOTMyODZlMmEzYTg1");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"02046bd2-1b21-466d-8a9b-1dd62a62702b\","
                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + msg + "\"},"

                                + "\"headings\": {\"en\": \"New Request\", \"es\": \"Spanish Subtitle\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.appointment_post_lv, parent, false);


        }


        currentAppointmentItem = getItem(position);

        TextView providerName = (TextView) listItemView.findViewById(R.id.AproviderNameView);
        if (currentAppointmentItem != null) {
            providerName.setText(currentAppointmentItem.getProvName());
        }


        TextView specialization = (TextView) listItemView.findViewById(R.id.AproviderSpecView);
        if (currentAppointmentItem != null) {
            specialization.setText(currentAppointmentItem.getSpecial());
        }

        TextView provLocation = (TextView) listItemView.findViewById(R.id.AProviderLocationView);
        if (currentAppointmentItem != null) {
            provLocation.setText(currentAppointmentItem.getProv_location());
        }

        TextView pateLocation = (TextView) listItemView.findViewById(R.id.APatientLocationView);
        if (currentAppointmentItem != null) {
            pateLocation.setText(currentAppointmentItem.getPate_location());
        }
        ImageButton phone = (ImageButton) listItemView.findViewById(R.id.phone);
        phone.setImageResource(R.drawable.phone);
        TextView provNumber = (TextView) listItemView.findViewById(R.id.APhoneNumberView);
        if (currentAppointmentItem != null) {
            provNumber.setText(currentAppointmentItem.getProv_number());
        }

        TextView date = (TextView) listItemView.findViewById(R.id.ACDView);
        date.setText(currentAppointmentItem.getDate());
        TextView pName = (TextView) listItemView.findViewById(R.id.APatientName);
        pName.setText(currentAppointmentItem.getPatientName());

        TextView time = (TextView) listItemView.findViewById(R.id.ACTView);
        if (currentAppointmentItem != null) {
            time.setText(currentAppointmentItem.getTime());
        }

        cancel = (Button) listItemView.findViewById(R.id.ACBtn);
// showing and hiding cancel button for appointments
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users u = dataSnapshot.getValue(Users.class);
                if (u.getTYPE().equals("doctor")) {

                    cancel.setVisibility(View.GONE);


                } else
                    cancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//canceling appointments
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to remove appointment from DB
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Are you sure?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
                                String[] timeParts, dateParts;
                                String year, month, day;
                                dateParts = currentAppointmentItem.getDate().split("/");
                                timeParts = currentAppointmentItem.getTime().split(":");
                                day = dateParts[2];
                                year = dateParts[0];
                                month = dateParts[1];
                                String hour = timeParts[0];
                                String minute = timeParts[1];
                                ref.child((currentAppointmentItem.getProviderID() + currentAppointmentItem.getPatientID() + year + month + day + hour + minute)).setValue(null);
                                Toast.makeText(getContext(), "Appointment have been canceled", Toast.LENGTH_SHORT).show();
                                sendNotification();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = dialog.create();
                alert.setTitle("Cancel Appointment");
                alert.show();


            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", currentAppointmentItem.getProv_number(), null));
                getContext().startActivity(intent);
            }
        });

        return listItemView;
    }

}
