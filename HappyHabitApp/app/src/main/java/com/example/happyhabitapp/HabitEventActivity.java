package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * This class represents the activity that displays all of the habit's events
 * and as an intermediary between the adding/editing of those events.
 */

//TODO: Create a view model, adapter, and listener for the habit events
//TODO: Render all view models and connect to the adapter
public class HabitEventActivity extends AppCompatActivity implements HabitListener{

    private ArrayList<HabitEvent> events;
    private EventsAdapter recyclerAdapter;

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_event_activity);
        setAdapter();
    }

    private void setAdapter() {
        recyclerAdapter = new EventsAdapter(events, this);
        recyclerView = findViewById(R.id.habit_event_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //Set data to be displayed linearly (instead of grid, etc...)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new EventAndHabitTouchHelper(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerAdapter.setTouchHelper(itemTouchHelper);                    //Add gesture support via callbacks
        itemTouchHelper.attachToRecyclerView(recyclerView);                 //Connect to the recyclerview

        recyclerView.setAdapter(recyclerAdapter);

    }







    @Override
    public void onHabitClick(int position) {

    }
}