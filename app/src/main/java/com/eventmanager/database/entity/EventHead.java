package com.eventmanager.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "EventHead",
        indices = {@Index("eventID")},
        foreignKeys = @ForeignKey(entity = Event.class, parentColumns = "eventID", childColumns = "eventID"))
public class EventHead {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    public String password;

    public int eventID;

    public EventHead (int id, String name, String password, int eventID) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.eventID = eventID;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public int getEventID() {
        return eventID;
    }

    public static EventHead[] populateData() {
        return new EventHead[] {
                new EventHead(101, "EventHead1", "password1", 1001),
                new EventHead(102, "EventHead2", "password2", 1002)

        };
    }
}
