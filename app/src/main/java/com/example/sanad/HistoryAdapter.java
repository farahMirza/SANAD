package com.example.sanad;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<History> {

    public HistoryAdapter(Activity context, ArrayList<History> mHistory) {

        super(context, 0, mHistory);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.history_post, parent, false);
        }


        History currentHistoryItem = getItem(position);

        TextView providerName = (TextView) listItemView.findViewById(R.id.hName);
        providerName.setText(currentHistoryItem.getProName());
        TextView patName = (TextView) listItemView.findViewById(R.id.hPName);
        patName.setText(currentHistoryItem.getPatName());
        TextView patLocation = (TextView) listItemView.findViewById(R.id.hPLocation);
        patLocation.setText(currentHistoryItem.getPatLocation());
        TextView date = (TextView) listItemView.findViewById(R.id.hDate);
        date.setText(currentHistoryItem.getDate());
        TextView time = (TextView) listItemView.findViewById(R.id.hTime);
        time.setText(currentHistoryItem.getTime());


        return listItemView;
    }
}