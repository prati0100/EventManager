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

    public Volunteer(int id, String name, int eventID) {
        this.id = id;
        this.name = name;
        this.eventID = eventID;
    }

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

    public static Volunteer[] populateData() {
        return new Volunteer[] {
                new Volunteer(1, "Jack Paulson", 1001),
                new Volunteer(2, "Martha Hunt", 1001),
                new Volunteer(3, "Craig Olsen", 1002),
                new Volunteer(4, "John Reaver", 1002),
                new Volunteer(5, "Alex Fergusen", 1002),
                new Volunteer(6, "Jacob Preston", 1003)
        };
    }
}
