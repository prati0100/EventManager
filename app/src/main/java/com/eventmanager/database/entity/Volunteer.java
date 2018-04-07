package com.eventmanager.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity (tableName = "volunteer",
        indices = {@Index("eventID")},
        foreignKeys =
         @ForeignKey(entity = Event.class, parentColumns = "eventID", childColumns = "eventID"))
public class Volunteer {

    @PrimaryKey
    public int id;

    public String name;

    public int eventID;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getEventID() {
        return eventID;
    }
}
