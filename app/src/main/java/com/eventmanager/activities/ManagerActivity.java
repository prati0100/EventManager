package com.eventmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.eventmanager.R;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Speaker;
import com.eventmanager.database.entity.Volunteer;
import com.eventmanager.fragments.ManagerEventsFragment;
import com.eventmanager.fragments.ManagerSpeakerFragment;
import com.eventmanager.fragments.ManagerVolunteersFragment;

import java.util.List;

public class ManagerActivity extends AppCompatActivity {
    private static int managerId;

    private FragmentManager mFragmentManager;

    private ManagerEventsFragment mManagerEventsFragment;
    private ManagerSpeakerFragment mManagerSpeakerFragment;
    private ManagerVolunteersFragment mManagerVolunteersFragment;

    private Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.manager_navigation_events:
                    if(currentFragment == mManagerEventsFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(R.string.title_events);

                    mFragmentManager.beginTransaction()
                            .replace(R.id.manager_fragment_container, mManagerEventsFragment)
                            .commit();
                    currentFragment = mManagerEventsFragment;
                    return true;

                case R.id.manager_navigation_speakers:
                    if(currentFragment == mManagerSpeakerFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(R.string.title_events);

                    mFragmentManager.beginTransaction()
                            .replace(R.id.manager_fragment_container, mManagerSpeakerFragment)
                            .commit();
                    currentFragment = mManagerSpeakerFragment;
                    return true;

                case R.id.manager_navigation_volunteers:
                    if(currentFragment == mManagerVolunteersFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(R.string.volunteers);

                    mFragmentManager.beginTransaction()
                            .replace(R.id.manager_fragment_container, mManagerVolunteersFragment)
                            .commit();
                    currentFragment = mManagerVolunteersFragment;
                    return true;
            }
            return false;
        }
    };

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Same code for both the add event options.
            case R.id.manager_speaker_menu_add_event:
            case R.id.manager_events_menu_add_event:
                Intent i = new Intent(this, EventCreateActivity.class);
                startActivity(i);
                return true;

            case R.id.manager_events_menu_delete_event:
                deleteCurrentEvent();
                return true;

            case R.id.manager_volunteers_menu_add:
                mManagerVolunteersFragment.addVolunteer();
                return true;
        }
        return true;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = findViewById(R.id.manager_toolbar);
        toolbar.setTitle(R.string.title_events);
        setSupportActionBar(toolbar);

        //Get the manager id from the intent;
        String id = getIntent().getExtras().getString(ManagerLoginActivity.EXTRA_MANAGER_ID);
        managerId = Integer.parseInt(id);

        mFragmentManager = getFragmentManager();

        mManagerEventsFragment = new ManagerEventsFragment();
        mManagerSpeakerFragment = new ManagerSpeakerFragment();
        mManagerVolunteersFragment = new ManagerVolunteersFragment();

        //Initially, display the events fragment.
        currentFragment = mManagerEventsFragment;
        mFragmentManager.beginTransaction()
                .add(R.id.manager_fragment_container, mManagerEventsFragment).commit();

        BottomNavigationView navigation = findViewById(R.id.manager_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(currentFragment == mManagerEventsFragment) {
            inflater.inflate(R.menu.manager_events_menu, menu);
        } else if(currentFragment == mManagerSpeakerFragment) {
            inflater.inflate(R.menu.manager_speaker_menu, menu);
        } else if(currentFragment == mManagerVolunteersFragment) {
            inflater.inflate(R.menu.manager_volunteers_menu, menu);
        }

        return true;
    }*/

    public static int getCurrentManagerId() {
        return managerId;
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

            //Set the current event head id to 0.
            managerId = 0;

            return null;
        }

        protected void onPostExecute(Void v) {
            ((Activity) context).finish();
        }
    }

    public static void logOutManager() {
        managerId = 0;
    }
}
