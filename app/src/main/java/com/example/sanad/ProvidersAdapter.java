package com.example.sanad;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProvidersAdapter extends ArrayAdapter<ProviderItem> {

    public ProvidersAdapter(Activity context, ArrayList<ProviderItem> ProviderItem) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for more than view, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, ProviderItem);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view_item, parent, false);
        }

        // Get the {@link doctor item} object located at this position in the list
        ProviderItem currentProviderItem = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView providerTextView = (TextView) listItemView.findViewById(R.id.provider_txtView_id);
        // Get the version number from the current doctor item object and
        // set this text on the number TextView
        providerTextView.setText(currentProviderItem.getProvider_name());

        // Find the ImageView in the list_view_item.xml layout with the ID list_item_icon
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.provider_img_id);
        // Get the image resource ID from the current doctor item object and
        // set the image to iconView
        iconView.setImageResource(currentProviderItem.getImg_id());

        // Return the whole list item layout (containing 1 TextView and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
