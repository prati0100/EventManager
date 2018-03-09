package com.eventmanager.database;

import android.arch.persistence.room.Database;;
import android.arch.persistence.room.RoomDatabase;

import com.eventmanager.dao.EventDao;
import com.eventmanager.dao.EventHeadDao;
import com.eventmanager.entity.Event;
import com.eventmanager.entity.EventHead;

@Database(entities = {Event.class, EventHead.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventDao eventDao();
    public abstract EventHeadDao eventHeadDao();

}
