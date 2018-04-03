package com.eventmanager.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.eventmanager.database.dao.EventDao;
import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Participant;
import com.eventmanager.database.entity.Participates;
import com.eventmanager.database.entity.Speaker;
import com.eventmanager.database.entity.Volunteer;

import java.util.concurrent.Executors;

@Database(entities = {Event.class, EventHead.class, Speaker.class, Participant.class,
          Volunteer.class, Participates.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract EventDao eventDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

//  pre-populates database on first run.
    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "my-database")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).eventDao().insertEvents(Event.populateData());
                                getInstance(context).eventDao().insertEventHeads(EventHead.populateData());
                                getInstance(context).eventDao().insertParticipants(Participant.populateData());
                            }
                        });
                    }
                })
                .build();
    }

}
