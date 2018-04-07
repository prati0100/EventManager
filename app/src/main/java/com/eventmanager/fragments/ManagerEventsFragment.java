package com.eventmanager.fragments;


import android.arch.persistence.room.Update;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eventmanager.R;
import com.eventmanager.activities.ManagerActivity;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
                    getEventHeadFromId(ManagerActivity.getCurrentManagerId());

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
}
