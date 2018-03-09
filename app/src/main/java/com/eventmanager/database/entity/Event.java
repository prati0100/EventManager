package com.eventmanager.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;



@Entity(tableName = "Event")
public class Event {

    @PrimaryKey
    public int eventID;

    @ColumnInfo(name = "event_name")
    public String eventName;

    public String room;

    // TODO  time

    public void setEventID(int event_id) {
        this.eventID = event_id;
    }

    public void setEventName(String event_name) {
        this.eventName= event_name;
    }

    public void setRoom(String room) {
        this.room = room;
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
}
