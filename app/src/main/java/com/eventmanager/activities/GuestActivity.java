package com.eventmanager.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestHomeFragment).commit();
                    currentFragment = mGuestHomeFragment;
                    return true;

                case R.id.navigation_schedule:
                    if(currentFragment == mGuestScheduleFragment) {
                        return true;
                    }

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestScheduleFragment).commit();
                    currentFragment = mGuestScheduleFragment;
                    return true;

                case R.id.navigation_events:
                    if(currentFragment == mGuestEventsFragment) {
                        return true;
                    }

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestEventsFragment).commit();
                    currentFragment = mGuestEventsFragment;
                    return true;

                case R.id.navigation_speakers:
                    if(currentFragment == mGuestSpeakersFragment) {
                        return true;
                    }

                    mFragmentManager.beginTransaction().replace(R.id.guest_fragment_container,
                            mGuestScheduleFragment).commit();
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

}
