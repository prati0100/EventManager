package com.eventmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

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

        //If the login fails, show an error message and
        if(!result) {
            Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            return;
        }

        //Login successful. Do other stuff here...


        // Launch ManagerActivity
        //Intent i = new Intent(this, ManagerActivity.class);
        //startActivity(i);
    }

    private boolean doLogin(String managerId, String password) {
        //TODO: Implement this once the databases are set up

        //Do the credential check on a different thread.
        try {
            LoginTask task = new LoginTask();
            task.execute(managerId, password);
            return task.get();
        } catch(CancellationException | InterruptedException | ExecutionException e) {
            return false;
        }

    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... params) {
            String managerId, password, hashed;

            //Only the ID and password should be passed. If more or less parameters are passed,
            //return an error
            if(params.length != 2) {
                return false;
            }

            managerId = params[0];
            password = params[1];

            //TODO: Get the hashed string from the database
            //The hash for string "abc". Replace this value with the one retrieved from the database
            hashed = "$2a$10$/H6mqwKWrtURpo0hZNmcw.6y1xi7Q3Fnnxwd0yE2l/ZKX5mhtV5zO";

            //Check if the password is correct
            return BCrypt.checkpw(password, hashed);
        }

    }

}
