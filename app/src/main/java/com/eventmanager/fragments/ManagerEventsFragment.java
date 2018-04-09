package com.eventmanager.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eventmanager.R;
import com.eventmanager.activities.EventCreateActivity;
import com.eventmanager.activities.ManagerActivity;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Speaker;
import com.eventmanager.database.entity.Volunteer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.eventmanager.activities.ManagerActivity.getCurrentManagerId;

public class ManagerEventsFragment extends Fragment implements View.OnClickListener {
    private Event mEvent;

    private TextInputLayout mEventNameTextInput, mEventDateTextInput, mEventTimeTextInput;
    private TextInputLayout mEventLocationTextInput;

    public ManagerEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Get the database instance.
        AppDatabase database = AppDatabase.getInstance(getActivity());

        //Get the event the manager belongs to.
        GetManagerEventTask task = new GetManagerEventTask(database);
        task.execute();
        try {
            mEvent = task.get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.database_access_fail),
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(mEvent == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.database_access_fail),
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Now set the details of that event in the text fields.

        //Set the name
        mEventNameTextInput = view.findViewById(R.id.manager_events_name);
        mEventNameTextInput.getEditText().setText(mEvent.getEventName());

        //Set the date.
        mEventDateTextInput = view.findViewById(R.id.manager_events_date);
        mEventDateTextInput.getEditText().setText(mEvent.date);

        //Set the time.
        mEventTimeTextInput = view.findViewById(R.id.manager_events_time);
        mEventTimeTextInput.getEditText().setText(mEvent.time);

        //Set the location.
        mEventLocationTextInput = view.findViewById(R.id.manager_events_location);
        mEventLocationTextInput.getEditText().setText(mEvent.getRoom());

        //Set the onClickListener for the button.
        Button b = view.findViewById(R.id.button_manager_events_edit);
        b.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manager_events_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Same code for both the add event options.
            case R.id.manager_events_menu_add_event:
                Intent i = new Intent(getActivity(), EventCreateActivity.class);
                startActivity(i);
                return true;

            case R.id.manager_events_menu_delete_event:
                deleteCurrentEvent();
                return true;
        }
        return true;
    }

    private void deleteCurrentEvent() {
        //Show an alert to confirm user action.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert);
        builder.setMessage(R.string.delete_event_prompt);

        String positiveText = getString(android.R.string.ok);

        AppDatabase database = AppDatabase.getInstance(getActivity());

        final EventDeleteTask task = new EventDeleteTask(database, getActivity());
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.execute();

                dialog.dismiss();
            }
        });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing.
            }
        });

        //Display the dialog.
        builder.create().show();
    }

    @Override
    public void onClick(View view) {
        //The onClick in only for the edit/save button.
        if(view.getId() != R.id.button_manager_events_edit) {
            return;
        }

        Button b = (Button) view;

        //If the user clicked on edit, get into edit mode. Enable all the text fields and
        //change the text of the button to save.
        if(b.getText().toString().equals(getResources().getString(R.string.edit))) {
            //Enable all the text fields.
            mEventNameTextInput.setEnabled(true);
            mEventDateTextInput.setEnabled(true);
            mEventTimeTextInput.setEnabled(true);
            mEventLocationTextInput.setEnabled(true);

            b.setText(R.string.save);
        }

        //If the user clicked on save, update the data in the database and disable all the
        //text fields. Change the text of the button to edit.
        else if (b.getText().toString().equals(getResources().getString(R.string.save))) {
            //Disable all the text fields.
            mEventNameTextInput.setEnabled(false);
            mEventDateTextInput.setEnabled(false);
            mEventTimeTextInput.setEnabled(false);
            mEventLocationTextInput.setEnabled(false);

            //Get the strings from the text fields.
            String newName = mEventNameTextInput.getEditText().getText().toString();
            String newDate = mEventDateTextInput.getEditText().getText().toString();
            String newTime = mEventTimeTextInput.getEditText().getText().toString();
            String newLocation = mEventLocationTextInput.getEditText().getText().toString();

            //Create a new Event object with the new data.
            Event newEvent = new Event(mEvent.getEventID(), newName, newLocation, newTime, newDate);

            //Execute the update on a new thread.
            UpdateManagerEventTask task = new UpdateManagerEventTask(newEvent,
                    AppDatabase.getInstance(getActivity()));
            task.execute();

            b.setText(R.string.edit);
        }
    }

    static class GetManagerEventTask extends AsyncTask<Void, Void, Event> {
        private AppDatabase database;

        public GetManagerEventTask(AppDatabase database) {
            this.database = database;
        }

        protected Event doInBackground(Void... params) {
            List<EventHead> managerList = database.eventDao().
                    getEventHeadFromId(getCurrentManagerId());

            //There must only be one manager.
            if(managerList.size() != 1) {
                return null;
            }

            List<Event> list = database.eventDao().getEventById(managerList.get(0).getEventID());

            //There must be only one event.
            if(list.size() != 1) {
                return null;
            }

            return list.get(0);
        }
    }

    static class UpdateManagerEventTask extends AsyncTask<Void, Void, Void> {
        private Event mEvent;
        private AppDatabase database;

        public UpdateManagerEventTask(Event mEvent, AppDatabase database) {
            this.mEvent = mEvent;
            this.database = database;
        }

        protected Void doInBackground(Void... params) {
            //Update the database entry.
            database.eventDao().updateEvents(mEvent);
            return null;
        }
    }

    private static class EventDeleteTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase database;
        private Context context;

        public EventDeleteTask(AppDatabase database, Context context) {
            this.database = database;
            this.context = context;
        }

        protected Void doInBackground(Void... params) {
            //Get the current manager and corresponding event.
            List<EventHead> eventHeadList = database.eventDao()
                    .getEventHeadFromId(getCurrentManagerId());

            EventHead head = eventHeadList.get(0);

            List<Event> eventList = database.eventDao().getEventById(head.getEventID());

            Event event = eventList.get(0);

            //Get the speaker for this event.
            List<Speaker> speakerList = database.eventDao().getSpeakerFromManagerId(head.getId());

            Speaker speaker = speakerList.get(0);

            //Get all the volunteers for this event.
            List<Volunteer> volunteerList = database.eventDao().getEventVolunteers(event.getEventID());

            //Delete the volunteers
            database.eventDao().deleteVolunteers(volunteerList);

            //Delete the event head.
            database.eventDao().deleteEventHead(head);

            //Delete the speaker.
            database.eventDao().deleteSpeaker(speaker);

            //Delete the event.
            database.eventDao().deleteEvent(event);

            ManagerActivity.logOutManager();

            return null;
        }

        protected void onPostExecute(Void v) {
            ((Activity) context).finish();
        }
    }
}
