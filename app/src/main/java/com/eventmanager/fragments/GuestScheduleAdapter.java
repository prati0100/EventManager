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
import com.eventmanager.database.entity.Event;

import java.util.List;

public class GuestScheduleAdapter extends RecyclerView.Adapter<GuestScheduleAdapter.ViewHolder> {
    private List<Event> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView guestScheduleRowName, guestScheduleRowTime, guestScheduleRowDate, guestScheduleRowLocation;
        public ImageView guestScheduleRowCircle;
        public ViewHolder(View view) {
            super(view);
            guestScheduleRowName = view.findViewById(R.id.guest_schedule_row_name);
            guestScheduleRowTime = view.findViewById(R.id.guest_schedule_row_time);
            guestScheduleRowDate = view.findViewById(R.id.guest_schedule_row_date);
            guestScheduleRowLocation = view.findViewById(R.id.guest_schedule_row_location);
            guestScheduleRowCircle = view.findViewById(R.id.guest_schedule_row_circle);
        }
    }

    //The dataset should be the list of Events.
    public GuestScheduleAdapter(List<Event> list) {
        mDataset = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GuestScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_schedule_row, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from the dataset at this position
        // - replace the contents of the view with that element

        Event event = mDataset.get(position);

        holder.guestScheduleRowName.setText(event.getEventName());
        holder.guestScheduleRowTime.setText(event.getEventTime());
        holder.guestScheduleRowDate.setText(event.getEventDate());
        holder.guestScheduleRowLocation.setText(event.getRoom());

        //To generate the colors for the row circle.
        ColorGenerator generator = ColorGenerator.MATERIAL;

        //Set the first letter of the event name in the circle.
        String letter = String.valueOf(mDataset.get(position).getEventName().charAt(0));

        //Create a TextDrawable for the image background.
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
        holder.guestScheduleRowCircle.setImageDrawable(drawable);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}