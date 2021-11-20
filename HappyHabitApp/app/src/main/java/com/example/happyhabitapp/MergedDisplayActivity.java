package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

//TODO: Add adapters. Make sure that the recycler adapter notifies the list view of any changes when they occur.
//TODO: Add button functionalities to toggle which view is displayed.
//TODO: Update the Username, profile pictures and habits according to firebase.
//TODO: Add the FAB to open add/edit fragment

public class MergedDisplayActivity extends AppCompatActivity implements HabitListener{

    private User currentUser;      //Change this to firebase??
    private HabitsAdapter recyclerAdapter;  //For the view of all habits (interactable)
    private DashboardAdapter listAdapter;   //For the view of today's habits (view only)
    private int buttonSelected;             //Indicates what state buttons are in.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_display);
    }

    /**
     * Sets up the adapter for the list view
     * and the recycler view (initially hidden)
     */
    private void setAdapters(){
        setRecyclerAdapter();
        setListAdapter();
    }

    /**
     * Sets an adapter that supports gestures on all habit entries
     * including swipe to delete, tap to edit/view, longpress+drag to re-order.
     * Sources from all of the user's habits.
     */
    private void setRecyclerAdapter(){
        recyclerAdapter = new HabitsAdapter(currentUser.getHabitList(), this);
        RecyclerView recyclerView = findViewById(R.id.habits_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //Set data to be displayed linearly (instead of grid, etc...)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new HabitTouchHelper(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerAdapter.setTouchHelper(itemTouchHelper);                    //Add gesture support via callbacks
        itemTouchHelper.attachToRecyclerView(recyclerView);                 //Connect to the recyclerview
    }


    /**
     * Sets an adapter that only supports static listing of habit entries. No interaction.
     * Sources from user's habits that coincide with today's date.
     */
    private void setListAdapter(){
        listAdapter = new DashboardAdapter(this, getTodaysHabits(currentUser.getHabitList()));
        ListView listView = findViewById(R.id.today_habit_list);
        listView.setAdapter(listAdapter);
    }

    /**
     * Gets all dates in the habit list whose dates match today's.
     * @return ArrayList<Habit> all the habits that occur today
     */
    private ArrayList<Habit> getTodaysHabits(ArrayList<Habit> habitList) {

    ArrayList<Habit> todaysHabits = new ArrayList<Habit>();     //The list to be returned
        Calendar today = Calendar.getInstance();                //Get the date today

        for(int i = 0; i < habitList.size(); i++) {
            Habit currentHabit = habitList.get(i);
            //Adds habit if it the corresponding weekday has a value of 1
            //DAY_OF_WEEK sets Sunday to be 1, Monday to be 2, etc...
            if(currentHabit.getWeek_freq()[today.get(Calendar.DAY_OF_WEEK) - 1] == 1) {
                todaysHabits.add(currentHabit);                 //Preserves order
            }
        }
        return todaysHabits;
    }


    /**
     * Helper method for the recycler adapter
     * @param position
     */
    @Override
    public void onHabitClick(int position) {

    }
}