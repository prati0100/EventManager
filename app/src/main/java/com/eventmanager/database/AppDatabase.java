package com.eventmanager.database;

import android.arch.persistence.room.Database;;
import android.arch.persistence.room.RoomDatabase;

import com.eventmanager.database.dao.EventDao;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Participant;
import com.eventmanager.database.entity.Participates;
import com.eventmanager.database.entity.Speaker;
import com.eventmanager.database.entity.Volunteer;

@Database(entities = {Event.class, EventHead.class, Speaker.class, Participant.class,
          Volunteer.class, Participates.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

}
