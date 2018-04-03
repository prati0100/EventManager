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
import com.eventmanager.database.entity.Speaker;

import java.util.List;

public class GuestSpeakersAdapter extends RecyclerView.Adapter<GuestSpeakersAdapter.ViewHolder> {
    //The indices of the speaker list and the event list should correspond. This means that for
    //a speaker at index i in the speakers list, his event name should be at index i in the events
    //list. This is rather hacky and will be improved in upcoming iterations.
    private List<Speaker> mSpeakersList;
    private List<Event> mEventsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView guestSpeakersName, guestSpeakersEvent;
        public ImageView guestSpeakersRowCircle;
        public ViewHolder(View view) {
            super(view);
            guestSpeakersName = view.findViewById(R.id.guest_speakers_row_name);
            guestSpeakersEvent = view.findViewById(R.id.guest_speakers_row_event);
            guestSpeakersRowCircle = view.findViewById(R.id.guest_speakers_row_circle);
        }
    }

    public GuestSpeakersAdapter(List<Speaker> speakerList, List<Event> eventList) {
        mSpeakersList = speakerList;
        mEventsList = eventList;
    }

    //Create new views (invoked by the layout manager).
    @Override
    public GuestSpeakersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_speakers_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from the dataset at this position
        // - replace the contents of the view with that element

        Speaker speaker = mSpeakersList.get(position);
        Event event = mEventsList.get(position);


        holder.guestSpeakersName.setText(speaker.getName());
        holder.guestSpeakersEvent.setText(event.getEventName());

        //To generate the colors for the row circle.
        ColorGenerator generator = ColorGenerator.MATERIAL;

        //Set the first letter of the speaker's name in the circle.
        String letter = String.valueOf(mSpeakersList.get(position).getName().charAt(0));

        //Create a TextDrawable for the image background.
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
        holder.guestSpeakersRowCircle.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mSpeakersList.size();
    }
}
