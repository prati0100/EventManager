package com.eventmanager.fragments;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.eventmanager.R;
import com.eventmanager.entity.Event;

import java.util.ArrayList;
import java.util.List;

public class GuestEventsAdapter extends RecyclerView.Adapter<GuestEventsAdapter.ViewHolder> {
    private List<Event> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView guestEventRowName, guestEventRowTime, guestEventRowDate, guestEventRowLocation;
        public ImageView guestEventRowCircle;
        public ViewHolder(View view) {
            super(view);
            guestEventRowName = view.findViewById(R.id.guest_events_row_name);
            guestEventRowTime = view.findViewById(R.id.guest_events_row_time);
            guestEventRowDate = view.findViewById(R.id.guest_events_row_date);
            guestEventRowLocation = view.findViewById(R.id.guest_events_row_location);
            guestEventRowCircle = view.findViewById(R.id.guest_events_row_circle);
        }
    }

    //The dataset should be a list of Events. Right now the database is not implemented so
    //using strings as dummy data.
    //TODO: Get the dataset from the database.
    public GuestEventsAdapter() {
        mDataset = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GuestEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_events_row, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TODO: Update the view holder with appropriate data.
        // - get element from the dataset at this position
        // - replace the contents of the view with that element

        //To generate the colors for the row circle.
        ColorGenerator generator = ColorGenerator.MATERIAL;

        //Set the first letter of the event name in the circle.
        String letter = String.valueOf(mDataset.get(position).getEventName().charAt(0));

        //Create a TextDrawable for the image background.
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
        holder.guestEventRowCircle.setImageDrawable(drawable);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}