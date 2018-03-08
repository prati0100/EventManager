package com.eventmanager.fragments;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eventmanager.R;

public class GuestEventsAdapter extends RecyclerView.Adapter<GuestEventsAdapter.ViewHolder> {
    //TODO: Change the data type of mDataset to array/list of Events.
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView guestEventRowName, guestEventRowTime, guestEventRowDate, guestEventRowLocation;
        public ViewHolder(View view) {
            super(view);
            guestEventRowName = view.findViewById(R.id.guest_events_row_name);
            guestEventRowTime = view.findViewById(R.id.guest_events_row_time);
            guestEventRowDate = view.findViewById(R.id.guest_events_row_date);
            guestEventRowLocation = view.findViewById(R.id.guest_events_row_location);
        }
    }

    //The dataset should be a list of Events. Right now the database is not implemented so
    //using strings as dummy data.
    //TODO: Get the dataset from the database.
    public GuestEventsAdapter() {
        mDataset = new String[5];
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GuestEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_events_row, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //TODO: Implement this once the Events class is created.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //Do nothing
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}