package com.eventmanager.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.eventmanager.database.entity.Event;
import com.eventmanager.database.entity.EventHead;
import com.eventmanager.database.entity.Participant;
import com.eventmanager.database.entity.Speaker;
import com.eventmanager.database.entity.Volunteer;

import java.util.List;

/**
 * Created by kunal on 09-03-2018.
 *
 * This interface is used by the Room library to build a database object. The library uses the
 * queries specified here to build an implementation for the methods.
 */
@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    @Query("SELECT * from event order by time")
    List<Event> getEventSchedule();

    @Query("SELECT * FROM Event WHERE eventID = :eventId")
    List<Event> getEventById(int eventId);

    @Query("SELECT MAX(eventID) FROM event")
    int getMaxEventId();

    @Insert
    void insertEvents(Event... events);

    @Delete
    void deleteEvent(Event event);

    @Update
    void updateEvents(Event... events);



    @Query("SELECT * FROM EventHead")
    List<EventHead> getAllHeads();

    @Query("SELECT * FROM EventHead WHERE id = :id")
    List<EventHead> getEventHeadFromId(int id);

    @Query("SELECT MAX(id) FROM EventHead")
    int getMaxEventHeadId();

    @Query("SELECT COUNT(id) FROM EventHead")
    int getEventHeadCount();

    @Insert
    void insertEventHeads(EventHead... eventHeads);

    @Delete
    void deleteEventHead(EventHead event);



    @Query("SELECT * FROM Speaker")
    List<Speaker> getAllSpeakers();

    @Query("SELECT * FROM event WHERE eventID = :eventId")
    List<Event> getSpeakerEvent(int eventId);

    @Query("SELECT * FROM speaker WHERE eventID = (SELECT eventId FROM eventHead WHERE id = :id)")
    List<Speaker> getSpeakerFromManagerId(int id);

    @Insert
    void insertSpeakers(Speaker... speakers);

    @Update
    void updateSpeakers(Speaker... speakers);

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

    @Query("SELECT * FROM volunteer WHERE eventID = :eventId")
    List<Volunteer> getEventVolunteers(int eventId);

    @Insert
    void insertVolunteers(Volunteer... volunteers);

    @Delete
    void deleteVolunteers(List<Volunteer> volunteers);
}
