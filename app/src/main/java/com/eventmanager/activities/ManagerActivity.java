package com.eventmanager.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.eventmanager.R;
import com.eventmanager.fragments.ManagerEventsFragment;

public class ManagerActivity extends AppCompatActivity {
    private static int managerId;

    private FragmentManager mFragmentManager;

    private ManagerEventsFragment mManagerEventsFragment;
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
                    return true;
            }
            return false;
        }
    };

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

        //Initially, display the events fragment.
        currentFragment = mManagerEventsFragment;
        mFragmentManager.beginTransaction()
                .add(R.id.manager_fragment_container, mManagerEventsFragment).commit();

        BottomNavigationView navigation = findViewById(R.id.manager_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static int getCurrentManagerId() {
        return managerId;
    }
}
