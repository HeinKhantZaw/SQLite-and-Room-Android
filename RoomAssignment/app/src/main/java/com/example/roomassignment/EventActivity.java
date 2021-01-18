package com.example.roomassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.Calendar;

public class EventActivity extends Activity {
    EditText edtEventName, edtEventDesc, edtEventLocation;
    DatePickerTimeline eventDate;
    neo btnNeo;
    TextView btnText, titleEventDialog;
    Intent intent;
    int dayValue = Calendar.getInstance().get(Calendar.DATE), monthValue = Calendar.getInstance().get(Calendar.MONTH) + 1, yearValue = Calendar.getInstance().get(Calendar.YEAR);

    private EventDatabase eventDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        init();

        // If there's data in intent, we don't need to insert a new row, we just need to update it
        // ( The scenario when user click the one of the items in recyclerView )

        if (intent != null && intent.hasExtra("id")) {
            btnText.setText("Update Event"); // change the btn text "Save Event" to "Update Event"
            titleEventDialog.setText("UPDATE EVENT"); // Change title from "ADD EVENT to UPDATE EVENT"
            titleEventDialog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);

            // get value from intent and set text in editText (name, desc,location,date)
            edtEventName.setText(intent.getStringExtra("name"));
            edtEventDesc.setText(intent.getStringExtra("description"));
            edtEventLocation.setText(intent.getStringExtra("location"));
            String date = intent.getStringExtra("date");

            String[] dateVal = date.split("\\.");
            yearValue = Integer.parseInt(dateVal[2]);
            monthValue = Integer.parseInt(dateVal[1]);
            dayValue = Integer.parseInt(dateVal[0]);
        }
    }

    private void init() {
        intent = getIntent();
        eventDatabase = EventDatabase.getInstance(this);

        btnNeo = findViewById(R.id.btnModify);
        btnText = findViewById(R.id.btnTxt);
        titleEventDialog = findViewById(R.id.titleEventDialog);
        edtEventName = findViewById(R.id.edtEventName);
        edtEventDesc = findViewById(R.id.edtEventDesc);
        edtEventLocation = findViewById(R.id.edtEventLocation);
        eventDate = findViewById(R.id.datePickerTimeline);

        eventDate.setInitialDate(yearValue, monthValue - 1, dayValue - 2);
        Calendar currentDate = Calendar.getInstance();
        eventDate.setActiveDate(currentDate);
        eventDate.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                dayValue = day;
                monthValue = month + 1;
                yearValue = year;
            }

            @Override
            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {

            }
        });
        btnNeo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btnNeo.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            btnNeo.setStyle(neo.small_inner_shadow);
                            btnText.setTextScaleX(btnText.getTextScaleX() * 0.9f);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            btnNeo.setStyle(neo.drop_shadow);
                            btnText.setTextScaleX(1);
                            onSaveButtonClicked();
                            return true;
                    }
                }
                return false;
            }
        });
    }

    // This method will be called when user clicked save btn
    private void onSaveButtonClicked() {
        final String date = dayValue + "." + monthValue + "." + yearValue;
        final String name = edtEventName.getText().toString();
        final String location = edtEventLocation.getText().toString();
        final String desc = edtEventDesc.getText().toString();
        final EventData eventData = new EventData(name,
                date, location, desc);

        // create a new thread to insert or update db
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // the condition when user click the one of the items in recyclerView (Update existing row)
                if (intent != null && intent.hasExtra("id")) {
                    eventData.setId(intent.getIntExtra("id", 0));
                    eventDatabase.eventDAO().updateEvent(eventData);
                }
                // the condition when user click plus button from MainActivity.java (Insert a new row)
                else {
                    eventDatabase.eventDAO().insertNewEvent(eventData);

                }
                finish(); // destroy activity (when u press back key after clicking "save btn", this activity won't be called again)
            }
        });
    }
}