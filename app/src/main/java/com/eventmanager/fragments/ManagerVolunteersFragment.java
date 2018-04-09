package com.eventmanager.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.eventmanager.R;
import com.eventmanager.activities.ManagerActivity;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Volunteer;

import java.util.List;

public class ManagerVolunteersFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AppDatabase mDatabase;

    private Context context;

    public ManagerVolunteersFragment() {
        // Required empty public constructor
    }

    public RecyclerView.Adapter getManagerVolunteersAdapter() {
        return mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_volunteers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        //Get the reference to the database object
        mDatabase = AppDatabase.getInstance(getActivity());

        mRecyclerView = view.findViewById(R.id.manager_volunteers_recycler);

        //Use a linear layout manager for the recycler.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Improves performance when the content layout size does not change for different items.
        mRecyclerView.setHasFixedSize(true);

        //Get the list of all volunteers and their corresponding events
        GetVolunteersTask task = new GetVolunteersTask(mDatabase);
        task.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manager_volunteers_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manager_volunteers_menu_add:
                addVolunteer();
        }
        return true;
    }

    public void addVolunteer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_volunteer);

        final View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_volunteer_create, null);

        builder.setView(view);

        builder.setPositiveButton(R.string.create_volunteer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputLayout layout = view.findViewById(R.id.manager_create_volunteer_name);
                String name = layout.getEditText().getText().toString();

                AppDatabase database = AppDatabase.getInstance(getActivity());

                VolunteerCreateTask task = new VolunteerCreateTask(database, name, (ManagerVolunteersAdapter) mAdapter);
                task.execute();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private class GetVolunteersTask extends AsyncTask<Void, Void, List<Volunteer>> {
        private AppDatabase database;
        public GetVolunteersTask(AppDatabase database) {
            this.database = database;
        }

        protected List<Volunteer> doInBackground(Void... params) {
            //Get the current event head object. We need it to know the event ID for which we need
            //to get the volunteer details for
            List<EventHead> eventHeadList = database.eventDao()
                    .getEventHeadFromId(ManagerActivity.getCurrentManagerId());

            EventHead eventHead = eventHeadList.get(0);

            //Get all the volunteers for this event
            return database.eventDao().getEventVolunteers(eventHead.getEventID());
        }

        protected void onPostExecute(List<Volunteer> volunteers) {
            mAdapter = new ManagerVolunteersAdapter(volunteers, getActivity(), mRecyclerView);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private static class VolunteerCreateTask extends AsyncTask<Void, Void, Void> {
        private AppDatabase database;
        private String name;
        private ManagerVolunteersAdapter adapter;

        public VolunteerCreateTask(AppDatabase database, String name, ManagerVolunteersAdapter adapter) {
            this.database = database;
            this.name = name;
            this.adapter = adapter;
        }

        protected Void doInBackground(Void... params) {
            //Get the event
            EventHead head = database.eventDao().getEventHeadFromId(ManagerActivity.getCurrentManagerId()).get(0);
            Event event = database.eventDao().getEventById(head.getEventID()).get(0);

            //Get the maximum volunteer ID.
            int id = database.eventDao().getMaxVolunteerId();
            id++;

            Volunteer volunteer = new Volunteer(id, name, event.getEventID());
            adapter.getDataset().add(volunteer);

            database.eventDao().insertVolunteers(volunteer);
            return null;
        }

        protected void onPostExecute(Void v) {
            adapter.notifyDataSetChanged();
        }
    }
}
