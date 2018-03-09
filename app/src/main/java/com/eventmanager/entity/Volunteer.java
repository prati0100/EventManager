package com.eventmanager.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by kunal on 09-03-2018.
 */
@Entity (tableName = "volunteer", foreignKeys =
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
