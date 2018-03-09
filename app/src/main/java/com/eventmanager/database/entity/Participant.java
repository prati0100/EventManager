package com.eventmanager.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by kunal on 09-03-2018.
 */

@Entity (tableName = "participant")
public class Participant {

    @PrimaryKey
    public int id;

    public String name;

    public String institute;

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
}
