package com.eventmanager.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.eventmanager.entity.Event;

import java.util.List;

/**
 * Created by kunal on 09-03-2018.
 */
@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

}
