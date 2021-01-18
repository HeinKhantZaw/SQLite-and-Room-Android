package com.example.sqliteassignment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sshadkany.neo;


public class MainActivity extends AppCompatActivity {
    neo myBtn;
    DBManager dbManager;
    ListView listView;
    SimpleCursorAdapter adapter;
    TextView plusTxt;
    String[] from = {DatabaseHelper._ID, DatabaseHelper.EVENT_NAME, DatabaseHelper.EVENT_DATE, DatabaseHelper.EVENT_LOCATION, DatabaseHelper.EVENT_DESCRIPTION};
    int[] to = {R.id.txtEventID, R.id.txtEventTitle, R.id.txtEventDate, R.id.txtEventLocation, R.id.txtEventDesc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); // codes for findViewByID and setOnClickListeners
        openDB();

        Cursor cursor = dbManager.fetch(); // get the result set of a query and put it to cursor
        adapter = new SimpleCursorAdapter(this, R.layout.event_item_layout, cursor, from, to, 0); // to get Data from Database to ListView
        adapter.notifyDataSetChanged(); // Notifies that data has been changed and makes adapter to refresh the data itself.

        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty)); //shows when list view is empty
        listView.setEmptyView(findViewById(R.id.assignmentTitle)); //shows when list view is empty
    }

    private void openDB() {
        dbManager = new DBManager(this);
        dbManager.open();
    }

    private void init() {
        myBtn = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);
        plusTxt = findViewById(R.id.plusIcon);
        listViewSetOnClick();
        btnSetOnClick();
    }

    private void btnSetOnClick() {
        myBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (myBtn.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // btn is pressed
                            myBtn.setStyle(neo.small_inner_shadow); // change shadow type when btn is pressed
                            plusTxt.setTextScaleX(plusTxt.getTextScaleX() * 0.9f); // change text scale to become a little bit smaller when user press the btn
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // btn is released
                            myBtn.setStyle(neo.drop_shadow); // change shadow type when btn is released
                            plusTxt.setTextScaleX(1); // set the text scale to normal when the btn is released
                            startActivity(new Intent(getApplicationContext(), AddEventActivity.class)); // start new activity
                            return true;
                    }
                }
                return false;
            }
        });
    }

    private void listViewSetOnClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the id of TextViews
                TextView tvID, tvName, tvDate, tvLocation, tvDesc;
                tvID = view.findViewById(R.id.txtEventID);
                tvName = view.findViewById(R.id.txtEventTitle);
                tvDate = view.findViewById(R.id.txtEventDate);
                tvLocation = view.findViewById(R.id.txtEventLocation);
                tvDesc = view.findViewById(R.id.txtEventDesc);

                // get the string values
                String strID, strName, strDate, strLocation, strDesc;
                strID = tvID.getText().toString();
                strName = tvName.getText().toString();
                strDate = tvDate.getText().toString();
                strLocation = tvLocation.getText().toString();
                strDesc = tvDesc.getText().toString();

                Intent modifyIntent = new Intent(getApplicationContext(), ModifyEventActivity.class);

                // put the data into intent which will be passed to ModifyEventActivity
                modifyIntent.putExtra(DatabaseHelper._ID, strID);
                modifyIntent.putExtra(DatabaseHelper.EVENT_NAME, strName);
                modifyIntent.putExtra(DatabaseHelper.EVENT_DATE, strDate);
                modifyIntent.putExtra(DatabaseHelper.EVENT_LOCATION, strLocation);
                modifyIntent.putExtra(DatabaseHelper.EVENT_DESCRIPTION, strDesc);

                startActivity(modifyIntent); //start new activity
            }
        });
    }
}