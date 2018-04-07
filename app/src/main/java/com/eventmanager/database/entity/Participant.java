package com.eventmanager.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity (tableName = "participant")
public class Participant {

    @PrimaryKey
    public int id;

    public String name;

    public String institute;

    public Participant (int id, String name, String institute){
        this.id = id;
        this.name = name;
        this.institute = institute;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstitute() {
        return institute;
    }

    public static Participant[] populateData() {
        return new Participant[]{
                new Participant(1101, "Participant1", "Ins1"),
                new Participant(1102, "Participant2", "Ins2")

        };
    }
}
