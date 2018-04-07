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
                new Speaker(1101, "Albert Einstein",
                        "Albert Einstein is a " +
                              "theoretical physicist who developed the theory of relativity, " +
                              "one of the two pillars of modern physics " +
                              "(alongside quantum mechanics).His work is also known for its " +
                              "influence on the philosophy of science.",
                        1001),

                new Speaker(1102, "Niels Bohr",
                        "Niels Henrik David Bohr is a Danish physicist who made " +
                              "foundational contributions to understanding atomic structure " +
                              "and quantum theory, for which he received the Nobel Prize in " +
                              "Physics in 1922.",
                        1002),

                new Speaker(1103, "Michael Faraday",
                        "Michael Faraday is an English scientist who contributed to the " +
                              "study of electromagnetism and electrochemistry. His main " +
                              "discoveries include the principles underlying electromagnetic " +
                              "induction, diamagnetism and electrolysis.",
                        1003)
        };
    }
}
