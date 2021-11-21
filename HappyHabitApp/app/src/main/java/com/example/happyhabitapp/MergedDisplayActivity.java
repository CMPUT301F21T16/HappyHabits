package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

//TODO: Add adapters. Make sure that the recycler adapter notifies the list view of any changes when they occur.
//TODO: Add button functionalities to toggle which view is displayed.
//TODO: Update the Username, profile pictures and habits according to firebase.
//TODO: Add the FAB to open add/edit fragment
//TODO: Refactor adapters/activities/etc...

public class MergedDisplayActivity extends AppCompatActivity implements HabitListener, Add_Edit_Fragment.onFragmentInteractionListener {

    private int TODAY = 0;
    private int ALL= 1;

    private User currentUser;      //Change this to firebase??
    private HabitsAdapter recyclerAdapter;  //For the view of all habits (interactable)
    private DashboardAdapter listAdapter;   //For the view of today's habits (view only)
    private int buttonSelected = TODAY;     //Indicates what state buttons are in.

    private ListView listView;              //Preserve information on visibility swaps
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_display);

        //Eventually get from the login-screen. For now, make a dummy version.
        Calendar today = Calendar.getInstance();

        //-------TEST INFO - REMOVE LATER -------
        int[] selectedDates = {1,0,0,1,0,0,0};
        int[] selectedDates2 = {0,0,0,0,1,1,1};
        Habit habit1 = new Habit("Get Food", "I am hungry", today, selectedDates,true);
        Habit habit2 = new Habit("Feed dog", "They are hungry", today, selectedDates2,false);
        Habit habit3 = new Habit("Test the list", "Who knows if it works", today, selectedDates,true);

        ArrayList<Habit> testList = new ArrayList<Habit>();
        testList.add(habit1); testList.add(habit2); testList.add(habit3);
        //-----------------------------------
        currentUser = new User("TestUser", "somePath", testList, null);

        setAdapters();
        setButtonListeners();
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
        recyclerView = findViewById(R.id.habits_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //Set data to be displayed linearly (instead of grid, etc...)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new HabitTouchHelper(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerAdapter.setTouchHelper(itemTouchHelper);                    //Add gesture support via callbacks
        itemTouchHelper.attachToRecyclerView(recyclerView);                 //Connect to the recyclerview

        recyclerView.setAdapter(recyclerAdapter);
    }


    /**
     * Sets an adapter that only supports static listing of habit entries. No interaction.
     * Sources from user's habits that coincide with today's date.
     */
    private void setListAdapter(){
        listAdapter = new DashboardAdapter(this, getTodaysHabits(currentUser.getHabitList()));
        listView = findViewById(R.id.todays_habits_list);
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
     * Sets all button listeners in the activity
     */
    private void setButtonListeners() {

        Button todayButton = findViewById(R.id.todays_habits_btn);
        Button allButton = findViewById(R.id.all_habits_btn);
        FloatingActionButton addButton = findViewById(R.id.add_habit_btn);

        todayButton.setOnClickListener((View v) -> {
            buttonToggle(TODAY);
        });

        allButton.setOnClickListener((View v) -> {
            buttonToggle(ALL);
        });

        //Go to add/edit habit fragment
        addButton.setOnClickListener((View v) -> {

            DialogFragment addFragment = new Add_Edit_Fragment();
            Bundle args = new Bundle();
            args.putSerializable("habit", null);
            addFragment.setArguments(args);

            addFragment.show(getSupportFragmentManager(), "ADD_HABIT");
        });
    }

    /**
     * Toggles the view that is displayed when a different button is clicked.
     * To be called by the button click listeners
     * @param mode an int representing what mode is to be toggled to
     */
    private void buttonToggle(int mode) {

        Button currentButton;
        Button otherButton;

        if (buttonSelected != mode) {        //Only trigger if the button isn't already selected
            if (mode == ALL) {
               currentButton = findViewById(R.id.todays_habits_btn);
               otherButton = findViewById(R.id.all_habits_btn);
               listView.setVisibility(View.INVISIBLE);                  //Hide the listview, show the recycler view.
               recyclerView.setVisibility(View.VISIBLE);
            }
            else {
               currentButton = findViewById(R.id.all_habits_btn);
               otherButton = findViewById(R.id.todays_habits_btn);
               listView.setVisibility(View.VISIBLE);                    //Hide the recycler, show the list view.
               recyclerView.setVisibility(View.INVISIBLE);
               setListAdapter();    //Re-draw listView in case any habits were removed
            }
            swapColor(currentButton, otherButton);
            buttonSelected = (buttonSelected + 1) % 2;                            //Swap the state
        }
    }

    /**
     * Helper function to button toggle. "Swaps" the colors of the buttons.
     * @param current the button that needs to be toggled off
     * @param other the button that needs to be toggled on
     */
    private void swapColor(Button current, Button other) {
        //De-select the current button
        current.setBackgroundTintList(getResources().getColorStateList(R.color.theme_secondary));   //Different setter due to material button
        current.setTextColor(getResources().getColor(R.color.theme_primary));

        //Select the other button
        other.setBackgroundTintList(getResources().getColorStateList(R.color.theme_primary));
        other.setTextColor(getResources().getColor(R.color.theme_secondary));
    }

    /**
     * Helper method for the recycler adapter
     * @param position
     */
    @Override
    public void onHabitClick(int position) {


    }

    @Override
    public void onAddPressed(Habit newHabit) {
        currentUser.addHabit(newHabit); // adds habit to data list
        recyclerAdapter.notifyDataSetChanged();    // notifies adapter of change

        //Swap to the all habits view to see change
        buttonToggle(ALL);
    }

    @Override
    public void onEditPressed(Habit newHabit, Habit oldHabit) {

    }
}