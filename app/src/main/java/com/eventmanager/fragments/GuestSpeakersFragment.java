package com.eventmanager.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventmanager.R;
import com.eventmanager.activities.GuestActivity;


/**
 * A simple {@link Fragment} subclass. This fragment shows the speakers attending the events in
 * {@link GuestActivity}.
 */
public class GuestSpeakersFragment extends Fragment {


    public GuestSpeakersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_speakers, container, false);
    }

}
