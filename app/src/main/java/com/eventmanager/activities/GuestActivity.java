package com.eventmanager.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.eventmanager.database.AppDatabase;
import com.eventmanager.fragments.GuestEventsFragment;
import com.eventmanager.fragments.GuestHomeFragment;
import com.eventmanager.fragments.GuestScheduleFragment;
import com.eventmanager.fragments.GuestSpeakersFragment;
import com.eventmanager.R;

public class GuestActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private GuestEventsFragment mGuestEventsFragment;
    private GuestHomeFragment mGuestHomeFragment;
    private GuestScheduleFragment mGuestScheduleFragment;
    private GuestSpeakersFragment mGuestSpeakersFragment;

    private Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //If the current fragment is the same, do nothing
                    if(currentFragment == mGuestHomeFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(getResources()
                            .getString(R.string.title_activity_guest));

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestHomeFragment).commit();
                    currentFragment = mGuestHomeFragment;
                    return true;

                case R.id.navigation_schedule:
                    if(currentFragment == mGuestScheduleFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(getResources()
                            .getString(R.string.title_schedule));

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestScheduleFragment).commit();
                    currentFragment = mGuestScheduleFragment;
                    return true;

                case R.id.navigation_events:
                    if(currentFragment == mGuestEventsFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(getResources().getString(R.string.title_events));

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestEventsFragment).commit();
                    currentFragment = mGuestEventsFragment;
                    return true;

                case R.id.navigation_speakers:
                    if(currentFragment == mGuestSpeakersFragment) {
                        return true;
                    }

                    getSupportActionBar().setTitle(getResources()
                            .getString(R.string.title_speakers));

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestSpeakersFragment).commit();
                    currentFragment = mGuestSpeakersFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        //Initialize the fragment manager.
        mFragmentManager = getFragmentManager();

        //Initialize the fragments.
        mGuestEventsFragment = new GuestEventsFragment();
        mGuestHomeFragment = new GuestHomeFragment();
        mGuestScheduleFragment = new GuestScheduleFragment();
        mGuestSpeakersFragment = new GuestSpeakersFragment();

        //Add the home fragment. This is the fragment that shows up when the activity is first
        //started.
        currentFragment = mGuestHomeFragment;
        mFragmentManager.beginTransaction().add(R.id.guest_fragment_container, mGuestHomeFragment)
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //The method the GuestHomeFragment calls when one of its buttons is clicked.
    //Although fragments are supposed to be modular, and this breaks that modularity, the fragments
    //in question are not modular anyway, so who cares.
    public void onClickGuestHomeButton(View v) {
        BottomNavigationView navigation = findViewById(R.id.guest_navigation);
        switch (v.getId()) {
            case R.id.button_home_events:
                getSupportActionBar().setTitle(getResources().getString(R.string.title_events));

                mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                        mGuestEventsFragment).commit();
                currentFragment = mGuestEventsFragment;
                navigation.setSelectedItemId(R.id.navigation_events);
                break;
            case R.id.button_home_schedule:
                getSupportActionBar().setTitle(getResources()
                        .getString(R.string.title_schedule));

                mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                        mGuestScheduleFragment).commit();
                currentFragment = mGuestScheduleFragment;
                navigation.setSelectedItemId(R.id.navigation_schedule);
                break;
            case R.id.button_home_speakers:
                getSupportActionBar().setTitle(getResources()
                        .getString(R.string.title_speakers));

                mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                        mGuestSpeakersFragment).commit();
                currentFragment = mGuestSpeakersFragment;
                navigation.setSelectedItemId(R.id.navigation_speakers);
                break;
        }
    }
}
