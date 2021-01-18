package com.example.roomassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sshadkany.neo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    neo addBtn;
    RecyclerView recyclerView;
    TextView txtPlus;
    CustomAdapter adapter;
    EventDatabase eventDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter); // set adapter for recycler view

        eventDatabase = EventDatabase.getInstance(getApplicationContext());

        //  when user performs swipe or drag actions, the following codes will be executed
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // When user drag the item
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // When user swipe the item
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition(); // get the position of current item
                        List<EventData> eventData = adapter.getData(); // get list of EventData
                        eventDatabase.eventDAO().deleteEvent(eventData.get(position)); // delete the item from db
                        retrieveData(); // fetch data and show in recyclerView again
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
    }

    private void retrieveData() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<EventData> eventData = eventDatabase.eventDAO().loadEventData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(eventData);
                    }
                });
            }
        });
    }

    private void init() {
        addBtn = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        txtPlus = findViewById(R.id.plusIcon);

        // This setOnTouchListener is same as the one in SQLite assignment, so plz go there to see explanations
        addBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (addBtn.isShapeContainsPoint(event.getX(), event.getY())) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            addBtn.setStyle(neo.small_inner_shadow);
                            txtPlus.setTextScaleX(txtPlus.getTextScaleX() * 0.9f);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            addBtn.setStyle(neo.drop_shadow);
                            txtPlus.setTextScaleX(1);
                            startActivity(new Intent(getApplicationContext(), EventActivity.class));
                            return true;
                    }
                }
                return false;
            }
        });
    }
}