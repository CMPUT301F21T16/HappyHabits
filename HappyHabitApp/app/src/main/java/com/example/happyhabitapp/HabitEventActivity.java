package com.example.happyhabitapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the activity that displays all of the habit's events
 * and as an intermediary between the adding/editing of those events.
 */

//TODO: Create a view model, adapter, and listener for the habit events --DONE
//TODO: Render all view models and connect to the adapter --DONE
//TODO: Connect to firebase
//TODO: Fix issue of Drag event occurring on swipe (Why is it doing this, when habits adapter has no issue?)

public class HabitEventActivity extends AppCompatActivity implements HabitListener, HabitEventFragment.OnFragmentInteractionListener, FirestoreCallback{
    private FireBase fire = new FireBase();
    private ArrayList<HabitEvent> events = new ArrayList<>();
    private EventsAdapter recyclerAdapter;

    private RecyclerView recyclerView;
    private ImageView backIcon;
    private String addKey = "habit";
    private String editKey = "event";

    private Habit passedInHabit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fire.setApi(this);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            // swap to passedInHabit
            passedInHabit = (Habit) extras.get("habit");
        }

        // TO DO: et passed in Habit from the EditorView Fragment

        //---- For testing only (delete after) ----
//        int[] weekFreq = {1,1,1,1,1,1,1,1};
//
//        HabitEvent event1 = new HabitEvent(null, "Dinner", 0, "Turkey Dinner Yum");
//        HabitEvent event2 = new HabitEvent(null, "Lunch", 1, "Starved this time :(");
//        HabitEvent event3 = new HabitEvent(null, "Breakfast", 2, "8 Bowls of Rice Krispies");
//
//        events = new ArrayList<HabitEvent>();
//        events.add(event1); events.add(event2); events.add(event3);
//
//        passedInHabit = new Habit("gobble gobble", "turkey", null, weekFreq, true, events);

        //----- END TEST -----
        fire.getEventList(events, passedInHabit);
        setContentView(R.layout.habit_event_activity);
        setHabitName();
        setAdapter();
        setAddButton();
        setBackButton();
    }

    private void setHabitName(){
        TextView habitName = findViewById(R.id.habit_events_habit_name);
        habitName.setText(passedInHabit.getTitle());
    }

    private void setAdapter() {
        recyclerAdapter = new EventsAdapter(events, this, passedInHabit);
        recyclerView = findViewById(R.id.habit_event_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //Set data to be displayed linearly (instead of grid, etc...)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new EventAndHabitTouchHelper(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerAdapter.setTouchHelper(itemTouchHelper);                    //Add gesture support via callbacks
        itemTouchHelper.attachToRecyclerView(recyclerView);                 //Connect to the recyclerview

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setBackButton() {
        backIcon = findViewById(R.id.back_btn);
        backIcon.setOnClickListener(view -> {
            Intent goBackActivity = new Intent(HabitEventActivity.this, MergedDisplayActivity.class);
            startActivity(goBackActivity);
            }
        );
    }


    /**
     * Initializes Button to allow users to add HabitEvents
     * also determines activity to be done when the button is clicked
     */
    private void setAddButton() {
        FloatingActionButton addButton = findViewById(R.id.add_habit_btn);
        // set listener for button
        // when button is clicked, launch HabitEventFragment to facilitate adding
        // a new Habit Event
        addButton.setOnClickListener(view -> {
            DialogFragment newFragment = new HabitEventFragment();
            Bundle args = new Bundle();
            args.putSerializable(addKey, passedInHabit);
            newFragment.setArguments(args);

            newFragment.show(getSupportFragmentManager(),"ADD_EVENT");
        });
    }

    /**
     * Determines activity when an existing HabitEVent is clicked on
     * Launches a HabitEventFragment that allows users to view or
     * edit their HabitEvent
     * @param position
     *      int: index of the Event's position in the RecyclerView
     */
    @Override
    public void onHabitClick(int position) {
        HabitEvent selectedEvent = events.get(position); // get the Event from the list

        // launch the fragment
        DialogFragment newFragment = new HabitEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(editKey, selectedEvent); // pass in the event
        newFragment.setArguments(args);

        newFragment.show(getSupportFragmentManager(),"EDIT_HABIT");
    }

    /**
     * This adds a new HabitEvent to the Habit's list of Events
     * @param event
     *      HabitEvent: new Event to be added to the list
     */
    @Override
    public void addNewEvent(HabitEvent event) {
//        passedInHabit.getEvents().add(event);
        fire.setHabitEvent(event, passedInHabit);
//        setAdapter();
//        recyclerAdapter.notifyItemInserted(events.size() - 1);
        recyclerAdapter.notifyDataSetChanged();
    }

    /**
     * This methods edits an existing HabitEvent
     * @param newEvent
     *      HabitEvent: The event containing updated attributes that the user wants to change to
     * @param oldEvent
     *      HabitEvent: The original event that the user wants to edit
     */
    @Override
    public void editEvent(HabitEvent newEvent, HabitEvent oldEvent) {
        // get recycler views list of Events to display
        List<HabitEvent> eventList = recyclerAdapter.getHabitEventList();
        // get index position of event we wish to edit
        int pos = eventList.indexOf(oldEvent);

        // error check that the vent exists
        if ( pos != -1) {
            eventList.set(pos, newEvent); // replace old event with event with updated values
            recyclerAdapter.notifyDataSetChanged(); // update the adapter for proper display
            // update firebase
            fire.delEvent(passedInHabit, oldEvent);
            fire.setHabitEvent(newEvent, passedInHabit);
        }

    }


    /* ================================================================ Methods for FirestoreCallback ================================================================ */

    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {

    }

    @Override
    public void checkUser(boolean[] has) {

    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {
        setAdapter();
    }
}