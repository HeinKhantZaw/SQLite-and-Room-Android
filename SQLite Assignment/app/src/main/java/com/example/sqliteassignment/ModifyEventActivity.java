package com.example.sqliteassignment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.Calendar;

public class ModifyEventActivity extends Activity {
    EditText edtTitle, edtLocation, edtDesc;
    DatePickerTimeline edtDate;
    neo btnUpdate, btnDelete;
    TextView txtUpdate, txtDelete;
    long _id;
    DBManager dbManager;
    int updatedDay, updatedMonth, updatedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        init();
        openDB();

        Intent intent = getIntent();
        _id = Long.parseLong(intent.getStringExtra(DatabaseHelper._ID));
        edtTitle.setText(intent.getStringExtra(DatabaseHelper.EVENT_NAME));
        String date = intent.getStringExtra(DatabaseHelper.EVENT_DATE);

        String[] dateVal = date.split("\\."); //get calendar date value and split into array ( Eg. "1.18.2021" -> {"1","18","2021"} )
        updatedYear = Integer.parseInt(dateVal[2]); // get the year value which is in the last index of the array
        updatedMonth = Integer.parseInt(dateVal[1]); // get the month value which is in the middle index of the array
        updatedDay = Integer.parseInt(dateVal[0]); // get the year value which is in the first index of the array

        edtDate.setInitialDate(2021, 0, 1); // set initial date for date picker
        Calendar updatedCalendar = Calendar.getInstance(); // get calender instance
        edtDate.setActiveDate(updatedCalendar); // set default selected date for date picker
        edtDate.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                // If user selects 18 January 2021, the values passed into this method are
                // year = 2021, month = 0 (Jan = 0, Feb = 1, ...) , day = 18, dayOfWeek = 2 (Sunday = 1, Monday 2, .....)
                updatedDay = day;
                updatedMonth = month + 1; // So, I have to plus one in order to get correct month number
                updatedYear = year;
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

            }
        });
        edtLocation.setText(intent.getStringExtra(DatabaseHelper.EVENT_LOCATION));
        edtDesc.setText(intent.getStringExtra(DatabaseHelper.EVENT_DESCRIPTION));
        setOnTouch();
    }

    private void openDB() {
        dbManager = new DBManager(this);
        dbManager.open();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouch() {
        // same as in MainActivity
        btnUpdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btnUpdate.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // btn is pressed
                            btnUpdate.setStyle(neo.small_inner_shadow);
                            txtUpdate.setTextScaleX(txtUpdate.getTextScaleX() * 0.9f);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // btn is released
                            btnUpdate.setStyle(neo.drop_shadow);
                            txtUpdate.setTextScaleX(1);

                            // get user input values
                            String title = edtTitle.getText().toString();
                            String date = updatedDay + "." + updatedMonth + "." + updatedYear;
                            String location = edtLocation.getText().toString();
                            String desc = edtDesc.getText().toString();

                            // update a row in db
                            dbManager.update(_id, title, date, location, desc);

                            returnHome();
                            return true;
                    }
                }
                return false;
            }
        });
        btnDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btnDelete.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            btnDelete.setStyle(neo.small_inner_shadow);
                            txtDelete.setTextScaleX(txtDelete.getTextScaleX() * 0.9f);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // RELEASED
                            btnDelete.setStyle(neo.drop_shadow);
                            txtDelete.setTextScaleX(1);

                            dbManager.delete(_id); // delete a row in db
                            returnHome();

                            return true;
                    }
                }
                return false;
            }
        });
    }

    private void init() {
        edtTitle = findViewById(R.id.edtEventName);
        edtDate = findViewById(R.id.datePickerTimeline);
        edtLocation = findViewById(R.id.edtEventLocation);
        edtDesc = findViewById(R.id.edtEventDesc);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        txtUpdate = findViewById(R.id.txtUpdate);
        txtDelete = findViewById(R.id.txtDelete);
    }

    public void returnHome() {
        // all of the other activities on top of it will be closed and this Intent will be delivered to the top.
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent); // start new activity
    }
}