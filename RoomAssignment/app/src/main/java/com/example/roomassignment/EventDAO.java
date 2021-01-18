package com.example.roomassignment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// This is Data Access Objects are the main classes where you define your database interactions
@Dao
public interface EventDAO {
    @Query("SELECT * FROM Events ORDER BY ID")
    List<EventData> loadEventData();

    @Insert()
    void insertNewEvent(EventData eventData);

    @Update()
    void updateEvent(EventData eventData);

    @Delete()
    void deleteEvent(EventData eventData);

}
