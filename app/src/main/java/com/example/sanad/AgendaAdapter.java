package com.example.sanad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class AgendaAdapter extends ArrayAdapter<Appointments> {
    Appointments currentApgendaItem;

    public AgendaAdapter(@NonNull Context context, ArrayList<Appointments> agenda) {
        super(context, 0,agenda);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.agenda_item, parent, false);


        }
        currentApgendaItem = getItem(position);
        TextView patname = (TextView) listItemView.findViewById(R.id.agenda_name);
        if (currentApgendaItem != null) {
            patname.setText(currentApgendaItem.getPatientName());
        }
        TextView pat_loc = (TextView) listItemView.findViewById(R.id.agendaLoc);
        if (currentApgendaItem != null) {
            patname.setText(currentApgendaItem.getPate_location());
        }
        TextView date = (TextView) listItemView.findViewById(R.id.agendaDate);
        if (currentApgendaItem != null) {
            patname.setText(currentApgendaItem.getDate());
        }
        TextView time = (TextView) listItemView.findViewById(R.id.agendaTime);
        if (currentApgendaItem != null) {
            patname.setText(currentApgendaItem.getTime());
        }
        return listItemView;
    }
}