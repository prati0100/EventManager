package com.eventmanager.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;



@Entity(tableName = "Event")
public class Event {

    @PrimaryKey
    public int eventID;

    public String eventName;

    public String room;

    public String time; //takes time in format "HHMM" (24-hour)

    public String date; //Takes the date in format "DD/MM/YYYY"

    public Event (int eventID, String eventName, String room, String time, String date) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.room = room;
        this.time = time;
        this.date = date;
    }

    public void setEventID(int event_id) {
        this.eventID = event_id;
    }

    public void setEventName(String event_name) {
        this.eventName= event_name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public int getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventTime() {
        return time;
    }

    public String getEventDate() {
        return date;
    }


    public static Event[] populateData() {
        return new Event[] {
                new Event(1001, "Demo_Event1", "AB1-303", "1300", "24/03/2018"),
                new Event(1002, "Demo_Event2", "AB5-306", "0900", "24/03/2018")
        };
    }
}
