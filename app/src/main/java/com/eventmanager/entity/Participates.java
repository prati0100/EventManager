package com.eventmanager.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;


@Entity(tableName = "participates", foreignKeys =
        {@ForeignKey(entity = Event.class, parentColumns = "eventID", childColumns = "eventID"),
        @ForeignKey(entity = Participant.class, parentColumns = "id", childColumns = "partID")})
public class Participates {

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
}
