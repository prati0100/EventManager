package com.eventmanager.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eventmanager.R;
import com.eventmanager.activities.ManagerActivity;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Speaker;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerSpeakerFragment extends Fragment implements View.OnClickListener {
    private Speaker mSpeaker;

    private TextInputLayout mSpeakerNameInputLayout, mSpeakerEventIdInputLayout,
            mSpeakerDetailsInputLayout;

    public ManagerSpeakerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_speaker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Get the database instance.
        AppDatabase database = AppDatabase.getInstance(getActivity());

        //Get the speaker of the event.
        GetSpeakerTask task = new GetSpeakerTask(database);
        task.execute();
        try {
            mSpeaker = task.get();
        }
        catch (InterruptedException | ExecutionException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.database_access_fail),
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(mSpeaker == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.database_access_fail),
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Now set the details of the speaker in the text fields.

        //Set the speaker name.
        mSpeakerNameInputLayout = view.findViewById(R.id.manager_speaker_name);
        mSpeakerNameInputLayout.getEditText().setText(mSpeaker.getName());

        //Set the event ID.
        mSpeakerEventIdInputLayout = view.findViewById(R.id.manager_speaker_event_id);
        mSpeakerEventIdInputLayout.getEditText().setText("" + mSpeaker.getEventID());

        //Set the description.
        mSpeakerDetailsInputLayout = view.findViewById(R.id.manager_speaker_details);
        mSpeakerDetailsInputLayout.getEditText().setText(mSpeaker.getDetails());

        //Set the OnClickListener for the button.
        Button b = view.findViewById(R.id.button_manager_speaker_edit);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //The onClick is only for the edit/save button.
        if(view.getId() != R.id.button_manager_speaker_edit) {
            return;
        }

        Button b = (Button) view;

        //If the user clicked on edit, get into edit mode. Enable all the text fields and
        //change the text of the button to save.
        if(b.getText().toString().equals(getResources().getString(R.string.edit))) {
            //Enable all the text fields.
            mSpeakerNameInputLayout.setEnabled(true);
            //Event ID can not be edited.
            mSpeakerDetailsInputLayout.setEnabled(true);

            b.setText(R.string.save);
        }

        //If the user clicked on save, update the data in the database and disable all the
        //text fields. Change the text of the button to edit.
        else if (b.getText().toString().equals(getResources().getString(R.string.save))) {
            //Disable all the text fields.
            mSpeakerNameInputLayout.setEnabled(false);
            //Event ID can not be edited.
            mSpeakerDetailsInputLayout.setEnabled(false);

            //Get the strings from the text fields.
            String newName = mSpeakerNameInputLayout.getEditText().getText().toString();
            String newDetails = mSpeakerDetailsInputLayout.getEditText().getText().toString();

            //Create a new speaker object with the new data.
            Speaker newSpeaker = new Speaker(mSpeaker.getId(), newName, newDetails,
                    mSpeaker.getEventID());

            //Execute the update on a new thread.
            UpdateSpeakerTask task = new UpdateSpeakerTask(AppDatabase
                    .getInstance(getActivity()), newSpeaker);
            task.execute();

            b.setText(R.string.edit);
        }
    }

    private static class GetSpeakerTask extends AsyncTask<Void, Void, Speaker> {
        private AppDatabase database;
        private EventHead eventHead;

        public GetSpeakerTask(AppDatabase database) {
            this.database = database;
        }

        protected Speaker doInBackground(Void... params) {
            List<EventHead> eventHeadList = database.eventDao().
                    getEventHeadFromId(ManagerActivity.getCurrentManagerId());

            //There must only be one manager in the list.
            if(eventHeadList.size() != 1) {
                return null;
            }

            eventHead = eventHeadList.get(0);

            List<Speaker> speakerList = database.eventDao()
                    .getSpeakerFromManagerId(eventHead.getId());

            //If there is no speaker, create a blank one.
            if(speakerList.size() == 0) {
                int id = database.eventDao().getMaxSpeakerId();
                id++; //Set the new speaker ID to previous max + 1

                Speaker speaker = new Speaker(id, "", "", eventHead.getEventID());
                database.eventDao().insertSpeakers(speaker);

                return speaker;
            } else {
                return speakerList.get(0);
            }
        }
    }

    private static class UpdateSpeakerTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase database;
        private Speaker newSpeaker;

        public UpdateSpeakerTask(AppDatabase database, Speaker newSpeaker) {
            this.database = database;
            this.newSpeaker = newSpeaker;
        }

        protected Void doInBackground(Void... params) {
            //Update the database entry.
            database.eventDao().updateSpeakers(newSpeaker);
            return null;
        }
    }
}
