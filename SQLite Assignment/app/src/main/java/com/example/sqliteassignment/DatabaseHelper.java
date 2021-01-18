package com.example.sqliteassignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "EVENTS";

    public static final String _ID = "_id";
    public static final String EVENT_NAME = "EventName";
    public static final String EVENT_DATE = "EventDate";
    public static final String EVENT_LOCATION = "EventLocation";
    public static final String EVENT_DESCRIPTION = "EventDescription";
    // Database Information
    static final String DB_NAME = "Event.DB";

    // database version
    static final int DB_VERSION = 1;

    // Query for Creating table
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_NAME + " TEXT NOT NULL, " + EVENT_DATE + " TEXT, " + EVENT_LOCATION
            + " TEXT, " + EVENT_DESCRIPTION + " TEXT);";
    // If table is existed already, drop the current table to create another one
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    } // create a table in db

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_IF_EXISTS); // If table exists, drop table
        onCreate(sqLiteDatabase); // create a new table
    }
}
