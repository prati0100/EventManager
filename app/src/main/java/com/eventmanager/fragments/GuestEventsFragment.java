package com.eventmanager.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventmanager.R;
import com.eventmanager.activities.GuestActivity;
import com.eventmanager.database.AppDatabase;


/**
 * A simple {@link Fragment} subclass. This fragment shows the events that are available in
 * {@link GuestActivity}.
 */
public class GuestEventsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AppDatabase mDatabase;

    public GuestEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Initialize the RecyclerView.
        mRecyclerView = view.findViewById(R.id.guest_events_recycler);

        //Use a linear layout manager for the recycler.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Improves performance when the content layout size does not change for different items.
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new GuestEventsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setDatabase(AppDatabase database) {
        mDatabase = database;
    }
}
