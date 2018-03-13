package com.eventmanager.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

    private AppDatabase mDatabase;

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

        //Initialize the database object.
        mDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "EventX").build();

        //Pass reference to the database object to each fragment. We use only one instance of the
        //database object because it is expensive to create and we generally don't need multiple
        //instances.
        //It is often dangerous to pass an object this way to a fragment if there is a possibility
        //for the fragment to outlive the activity. It does not happen in our case though, so
        //no harm.
        mGuestEventsFragment.setDatabase(mDatabase);
        mGuestHomeFragment.setDatabase(mDatabase);
        mGuestScheduleFragment.setDatabase(mDatabase);
        mGuestSpeakersFragment.setDatabase(mDatabase);

        //Add the home fragment. This is the fragment that shows up when the activity is first
        //started.
        currentFragment = mGuestHomeFragment;
        mFragmentManager.beginTransaction().add(R.id.guest_fragment_container, mGuestHomeFragment)
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.guest_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }
}
