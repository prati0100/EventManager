package com.eventmanager.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "participates", foreignKeys =
        {@ForeignKey(entity = Event.class, parentColumns = "eventID", childColumns = "eventID"),
        @ForeignKey(entity = Participant.class, parentColumns = "id", childColumns = "partID")})
public class Participates {

    @PrimaryKey(autoGenerate = true)
    public int participates_pk;

    public int partID;

    public int eventID;

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getPartID() {
        return partID;
    }

    public int getEventID() {
        return eventID;
    }

    public int getParticipates_pk() {
        return participates_pk;
    }
}
