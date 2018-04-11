package com.eventmanager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.eventmanager.R;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Speaker;

import org.mindrot.jbcrypt.BCrypt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        Toolbar toolbar = findViewById(R.id.event_create_toolbar);
        toolbar.setTitle(R.string.create_event);
        setSupportActionBar(toolbar);
    }

    public void onClickCreate(View view) {
        //Get the text from the text fields.
        TextInputLayout textInputLayout = findViewById(R.id.manager_create_event_name);
        String name = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.manager_create_event_date);
        String date = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.manager_create_event_time);
        String time = textInputLayout.getEditText().getText().toString();

        textInputLayout = findViewById(R.id.manager_create_event_location);
        String location = textInputLayout.getEditText().getText().toString();

        if(name.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, R.string.create_event_error, Toast.LENGTH_LONG).show();
            return;
        }

        //Parse the date and time to make sure they are valid.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        //The parse() method does not (by default) throw an Exception if the date is correctly
        // formatted but invalid on the calendar (e.g. 29/2/2001), instead it alters the date to be
        // a valid one (in the previous example, to 1/3/2001).
        dateFormat.setLenient(false);
        try {
            Date dDate = dateFormat.parse(date);
        } catch (ParseException pe) {
            Toast.makeText(this, R.string.event_date_format_error, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
        timeFormat.setLenient(false);
        try {
            Date dTime = timeFormat.parse(time);
        } catch (ParseException pe) {
            Toast.makeText(this, R.string.event_time_format_error, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //Set the event ID to 0 for now. We will later query the database to pick
        //An appropriate event ID.
        Event event = new Event(0, name, location, time, date);

        CreateEventTask task = new CreateEventTask(event, AppDatabase.getInstance(this), this);
        task.execute();
    }

    private static class CreateEventTask extends AsyncTask<Void, Void, EventHead> {
        private Event event;
        private AppDatabase database;
        private String password;
        private Context context;

        public CreateEventTask(Event event, AppDatabase database, Context context) {
            this.event = event;
            this.database = database;
            this.context = context;
        }

        @Override
        protected EventHead doInBackground(Void... params) {
            //Get the maximum event ID
            int newEventId = database.eventDao().getMaxEventId();
            newEventId++; //Set the new event ID to previous max + 1
            event.setEventID(newEventId);

            //Get the name of the current event head.
            List<EventHead> list = database.eventDao()
                    .getEventHeadFromId(ManagerActivity.getCurrentManagerId());

            String eventHeadName = list.get(0).getName();

            //Get the max event head id.
            int newEventHeadId = database.eventDao().getMaxEventHeadId();
            newEventHeadId++; //The new id is previous max + 1

            //Create a password for this event head. The password will be hunterX where x is the
            //number of event heads that will be after this insertion. So if there are 4 event heads
            //in the database, the password will be hunter5.
            int eventHeadCount = database.eventDao().getEventHeadCount();
            eventHeadCount++;
            password = "hunter" + eventHeadCount;

            //Hash the string.
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

            //Create a new event head to correspond to this event
            EventHead head = new EventHead(newEventHeadId, eventHeadName, hashed, newEventId);

            //Insert the new event in the database.
            database.eventDao().insertEvents(event);

            //Insert the new head in the database
            database.eventDao().insertEventHeads(head);

            //Create a dummy speaker.
            int id = database.eventDao().getMaxSpeakerId();
            id++; //Set the new speaker ID to previous max + 1

            Speaker speaker = new Speaker(id, "", "", head.getEventID());
            database.eventDao().insertSpeakers(speaker);

            return head;
        }

        protected void onPostExecute(EventHead eventHead) {
            //Create an alert to show the new username and password.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.event_created));
            builder.setMessage("New event created. The new event's ID is: " + eventHead.getEventID()
                    + "\n\nThe credentials of the new event head are:\n"
                    + "\nID: " + eventHead.getId()
                    + "\nPassword: " + password);

            String positiveText = context.getString(android.R.string.ok);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Activity a = (Activity) context;
                            a.finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();
        }
    }
}
