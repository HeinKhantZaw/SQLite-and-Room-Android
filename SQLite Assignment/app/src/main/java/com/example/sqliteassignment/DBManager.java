package com.example.sqliteassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DatabaseHelper databaseHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        this.context = context;
    }

    public void open() {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase(); //Create and/or open a database that will be used for reading and writing.
    }

    public void close() {
        databaseHelper.close();
    }

    public void insert(String eventName, String eventDate, String eventLocation, String eventDesc) {
        // This method inserts data into database

        ContentValues values = new ContentValues(); // A key/value store that inserts data into a row of a table
        // key is the column name in the database, and value is the value to store in that column.
        values.put(DatabaseHelper.EVENT_NAME, eventName); // In here key is "EventName" and value is the parameter called eventName which will be passed into this method.
        values.put(DatabaseHelper.EVENT_DATE, eventDate);
        values.put(DatabaseHelper.EVENT_LOCATION, eventLocation);
        values.put(DatabaseHelper.EVENT_DESCRIPTION, eventDesc);
        database.insert(DatabaseHelper.TABLE_NAME, null, values); // insert into database
    }

    public Cursor fetch() {
        // this method gets the data from database

        // array of table columns is created
        String[] columns = {DatabaseHelper._ID, DatabaseHelper.EVENT_NAME, DatabaseHelper.EVENT_DATE, DatabaseHelper.EVENT_LOCATION, DatabaseHelper.EVENT_DESCRIPTION};

        // get the result set of a query and put it to cursor
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) { // if (cursor != null) -> (cursor has data, it is not empty)
            cursor.moveToFirst(); // moves the cursor to the first result
        }
        return cursor;
    }

    public void update(long _id, String eventName, String eventDate, String eventLocation, String eventDesc) {
        // this method updates the data

        ContentValues values = new ContentValues(); // creat a new contentValues object again to put data
        values.put(DatabaseHelper.EVENT_NAME, eventName);
        values.put(DatabaseHelper.EVENT_DATE, eventDate);
        values.put(DatabaseHelper.EVENT_LOCATION, eventLocation);
        values.put(DatabaseHelper.EVENT_DESCRIPTION, eventDesc);
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper._ID + " = " + _id, null);
        // this query will update the row that matches the id param passed into this method
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + " = " + _id, null);
        // this query will delete the row that matches the id param passed into this method
    }
}
