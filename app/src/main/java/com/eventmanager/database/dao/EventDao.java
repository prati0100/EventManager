package com.eventmanager.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Participant;
import com.eventmanager.database.entity.Speaker;
import com.eventmanager.database.entity.Volunteer;

import java.util.List;

/**
 * Created by kunal on 09-03-2018.
 */
@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    //    TODO schedule query

    @Insert
    void insertEvents(Event... events);

    @Delete
    void deleteEvent(Event event);



    @Query("SELECT * FROM EventHead")
    List<EventHead> getAllHeads();

    @Insert
    void insertEvents(EventHead... eventHeads);

    @Delete
    void deleteEvent(EventHead event);



    @Query("SELECT * FROM Speaker")
    List<Speaker> getAllSpeakers();

    @Insert
    void insertSpeakers(Speaker... speakers);

    @Delete
    void deleteSpeaker(Speaker speaker);



    @Query("SELECT * FROM participant")
    List<Participant> getAllParticipants();

    @Insert
    void insertParticipants(Participant... participants);

    @Delete
    void deleteParticipant(Participant participant);



    @Query("SELECT * FROM volunteer")
    List<Volunteer> getAllVolunteers();

    @Insert
    void insertVolunteers(Volunteer... volunteers);

    @Delete
    void deleteVolunteer(Volunteer volunteer);












}
