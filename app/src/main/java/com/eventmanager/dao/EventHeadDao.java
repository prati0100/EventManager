package com.eventmanager.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.eventmanager.entity.EventHead;

import java.util.List;

@Dao
public interface EventHeadDao {

    @Query("SELECT * FROM EventHead")
    List<EventHead> getAllHeads();

    @Insert
    void insertAll(EventHead... eventHeads);

    @Delete
    void delete(EventHead event);
}

