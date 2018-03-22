package com.eventmanager.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;



@Entity (tableName = "speaker",
        indices = {@Index("eventID")},
        foreignKeys = @ForeignKey(entity = Event.class, parentColumns = "eventID", childColumns = "eventID"))
public class Speaker {

    @PrimaryKey
    public int id;

    public String name;

    public  String details;

    public int eventID;

    public Speaker (int id, String name, String details, int eventID) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.eventID = eventID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getDetails() {
        return details;
    }

    public int getEventID() {
        return eventID;
    }

    public static Speaker[] populateData() {
        return new Speaker[]{
                new Speaker(1101, "Speaker1", "Blah Blah blah blah blahhhhhhhh", 1001),
                new Speaker(1102, "Speaker2", "Blah Blah blah blah blahhhhhhhh", 1002)
        };
    }
}
