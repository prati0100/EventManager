package com.eventmanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.eventmanager.R;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.EventHead;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class ManagerLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.manager_login_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClickLogin(View v) {
        String managerId, password;
        boolean result;

        //Get the manager ID and password
        AppCompatEditText idEditText = findViewById(R.id.edittext_manager_id);
        AppCompatEditText passEditText = findViewById(R.id.edittext_password);
        managerId = idEditText.getText().toString();
        password = passEditText.getText().toString();

        result = doLogin(managerId, password);

        //Show a toast and return if the login failed.
        if(!result) {
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Launch ManagerActivity
        Intent i = new Intent(this, ManagerActivity.class);
        startActivity(i);
    }

    private boolean doLogin(String managerId, String password) {
        //Do the credential check on a different thread.
        try {
            LoginTask task = new LoginTask(managerId, password, AppDatabase.getInstance(this));
            task.execute();
            return task.get();
        } catch(CancellationException | InterruptedException | ExecutionException e) {
            return false;
        }

    }

    private static class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private AppDatabase database;
        private String managerId, password;

        public LoginTask(String managerId, String password, AppDatabase database) {
            this.managerId = managerId;
            this.password = password;
            this.database = database;
        }

        protected Boolean doInBackground(Void... params) {
            int id = Integer.parseInt(managerId);
            String hashed;

            //Get event head object from the database.
            List<EventHead> list = database.eventDao().getEventHeadFromId(id);
            //If the id does not exist in the database, login fails.
            if(list.size() == 0) {
                return false;
            }
            hashed = list.get(0).getPassword();

            //Check if the password is correct
            return BCrypt.checkpw(password, hashed);
        }
    }

}
