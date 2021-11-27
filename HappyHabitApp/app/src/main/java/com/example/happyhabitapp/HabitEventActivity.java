package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * This class represents the activity that displays all of the habit's events
 * and as an intermediary between the adding/editing of those events.
 */

//TODO: Create a view model, adapter, and listener for the habit events --DONE
//TODO: Render all view models and connect to the adapter --DONE
//TODO: Connect to firebase
//TODO: Fix issue of Drag event occurring on swipe (Why is it doing this, when habits adapter has no issue?)

public class HabitEventActivity extends AppCompatActivity implements HabitListener, HabitEventFragment.OnFragmentInteractionListener{

    private ArrayList<HabitEvent> events;
    private EventsAdapter recyclerAdapter;

    private RecyclerView recyclerView;
    private ImageView backIcon;

    private Habit passedInHabit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TO DO: et passed in Habit from the EditorView Fragment

        //---- For testing only (delete after) ----
        int[] weekFreq = {1,1,1,1,1,1,1,1};

        HabitEvent event1 = new HabitEvent(null, "Dinner", 0, "Turkey Dinner Yum");
        HabitEvent event2 = new HabitEvent(null, "Lunch", 1, "Starved this time :(");
        HabitEvent event3 = new HabitEvent(null, "Breakfast", 2, "8 Bowls of Rice Krispies");

        events = new ArrayList<HabitEvent>();
        events.add(event1); events.add(event2); events.add(event3);

        passedInHabit = new Habit("gobble gobble", "turkey", null, weekFreq, true, events);

        //----- END TEST -----
        setContentView(R.layout.habit_event_activity);
        setHabitName();
        setAdapter();
        setAddButton();
        setBackButton();
    }

    private void setHabitName(){
        TextView habitName = findViewById(R.id.habit_events_habit_name);
        habitName.setText("Eat Food");
    }

    private void setAdapter() {
        recyclerAdapter = new EventsAdapter(passedInHabit.getEvents(), this);
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
     * This method launches the fragment to add a new event
     */
    private void setAddButton() {
        FloatingActionButton addButton = findViewById(R.id.add_habit_btn);
        addButton.setOnClickListener(view -> {
            DialogFragment newFragment = new HabitEventFragment();
            Bundle args = new Bundle();
            args.putSerializable("habit", passedInHabit);
            newFragment.setArguments(args);

            newFragment.show(getSupportFragmentManager(),"ADD_EVENT");
        });
    }

    @Override
    public void onHabitClick(int position) {
        Toast.makeText(this, "la", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method receives the event from the fragment.
     * @param event
     */
    @Override
    public void addNewEvent(HabitEvent event) {
        passedInHabit.getEvents().add(event);
        recyclerAdapter.notifyItemInserted(passedInHabit.getEvents().size() - 1);
    }

    @Override
    public void editEvent(HabitEvent newEvent, HabitEvent oldEvent) {

    }
}