package com.example.roomassignment;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = EventData.class, version = 1, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static String DATABASE_NAME = "events.db";
    private static EventDatabase eventDatabaseInstance;

    public static EventDatabase getInstance(Context context) {
        if (eventDatabaseInstance == null) {
            synchronized (LOCK) {
                eventDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), EventDatabase.class, EventDatabase.DATABASE_NAME).build(); // create a new database
            }
        }
        return eventDatabaseInstance;
    }
    public abstract EventDAO eventDAO();
}
