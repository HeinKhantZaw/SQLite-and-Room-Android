package com.example.sqliteassignment;

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

public class AddEventActivity extends Activity {
    neo btnAdd;
    EditText edtName, edtLocation, edtDesc;
    TextView txtAdd;
    DatePickerTimeline date;
    int dayValue = Calendar.getInstance().get(Calendar.DATE);
    int monthValue = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int yearValue = Calendar.getInstance().get(Calendar.YEAR);
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        init(); // findViewById and setOnClick
        openDB();
    }

    private void openDB() {
        dbManager = new DBManager(this);
        dbManager.open();
    }

    private void init() {
        btnAdd = findViewById(R.id.btnAdd);
        edtName = findViewById(R.id.edtEventName);
        date = findViewById(R.id.datePickerTimeline);
        date.setInitialDate(yearValue, monthValue - 1, dayValue - 2); // set initial date of date picker
        Calendar currentDate = Calendar.getInstance(); // get calender instance
        date.setActiveDate(currentDate);  // set default selected date for date picker
        date.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override

            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                // If user selects 18 January 2021, the values passed into this method are
                // year = 2021, month = 0 (Jan = 0, Feb = 1, ...) , day = 18, dayOfWeek = 2 (Sunday = 1, Monday 2, .....)
                dayValue = day;
                monthValue = month + 1;  // So, I have to plus one in order to get correct month number
                yearValue = year;
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

            }
        });
        edtLocation = findViewById(R.id.edtEventLocation);
        edtDesc = findViewById(R.id.edtEventDesc);
        txtAdd = findViewById(R.id.txtAdd);

        // Same as in MainActivity
        btnAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btnAdd.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            btnAdd.setStyle(neo.small_inner_shadow);
                            txtAdd.setTextScaleX(txtAdd.getTextScaleX() * 0.9f);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            btnAdd.setStyle(neo.drop_shadow);
                            txtAdd.setTextScaleX(1);

                            // get user input values
                            final String name = edtName.getText().toString();
                            final String date = dayValue + "." + monthValue + "." + yearValue;
                            final String location = edtLocation.getText().toString();
                            final String desc = edtDesc.getText().toString();

                            // insert into db
                            dbManager.insert(name, date, location, desc);

                            // start new activity
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            // FLAG_ACTIVITY_CLEAR_TOP means all of the other activities on top of it will be closed and this Intent will be delivered to the top.
                            return true;
                    }
                }
                return false;
            }
        });
    }
}