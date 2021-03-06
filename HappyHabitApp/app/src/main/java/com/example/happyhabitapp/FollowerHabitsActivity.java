package com.example.happyhabitapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * This class represents the dashboard of a follower, which is view-only
 * It is similar to the standard Dashboard activity.
 */
public class FollowerHabitsActivity extends AppCompatActivity implements FirestoreCallback{

    private int TODAY = 0;         //Constants for changing the display type
    private int ALL= 1;
    private int buttonSelected = TODAY;     //Indicates what state buttons are in.


    //TODO: Add onClickListener to each item to view events
    private FireBase fire = new FireBase();
    private NoTouchHabitAdapter todaysHabitsAdapter;
    private NoTouchHabitAdapter allHabitsAdapter;   //For the view of today's habits (view only)
    private ArrayList<Habit> habitList = new ArrayList<Habit>();
    private ArrayList<HabitEvent> eventList = new ArrayList<HabitEvent>();

    private RecyclerView todaysHabitsView;              //Preserve information on visibility swaps
    private RecyclerView allHabitsView;
    private String username;
    private ImageView followerProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fire.setApi(this);
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = extra.getString("username");
        }
        fire.getOthersHabit(username, habitList);
        followerProfilePic = findViewById(R.id.follower_dashboard_profile_pic);
        fire.getOtherPic(username);
        setContentView(R.layout.activity_follower_habits);
        //setListAdapters();
        setButtons();
        setUsername();
    }

    /**
     * Sets both list views that on touch, will bring up the habit's respective events.
     */
    private void setListAdapters() {
        allHabitsView = findViewById(R.id.follower_habits_list);
        todaysHabitsView = findViewById(R.id.follower_todays_habits_list);

        //Set adapter for todays habits
        todaysHabitsAdapter = new NoTouchHabitAdapter(getTodaysHabits(habitList), username);
        todaysHabitsView.setLayoutManager(new LinearLayoutManager(this));
        todaysHabitsView.setHasFixedSize(true);
        todaysHabitsView.setAdapter(todaysHabitsAdapter);

        //Set adapter for all habits
        allHabitsAdapter = new NoTouchHabitAdapter(habitList, username);
        allHabitsView.setLayoutManager(new LinearLayoutManager(this));
        allHabitsView.setHasFixedSize(true);
        allHabitsView.setAdapter(allHabitsAdapter);
    }

    /**
     * Sets all the relevant buttons on the activity with onClickListeners
     */
    private void setButtons() {
        ImageView profileIcon = findViewById(R.id.follower_dashboard_profile_pic);
        ImageView backButton = findViewById(R.id.follower_habits_back_btn);
        Button todayButton = findViewById(R.id.follower_todays_habits_btn);
        Button allButton = findViewById(R.id.follower_all_habits_btn);

        //Return to profile activity
        profileIcon.setOnClickListener(v -> {
            Intent profileIntent = new Intent(FollowerHabitsActivity.this, ProfilePageActivity.class);
            startActivity(profileIntent);
        });

        //Return to personal dashboard
        backButton.setOnClickListener(v -> {
            Intent dashboardIntent = new Intent(FollowerHabitsActivity.this, MergedDisplayActivity.class);
            startActivity(dashboardIntent);
        });

        //Sets the two toggle buttons
        todayButton.setOnClickListener(v -> {
            buttonToggle(TODAY);
        });

        allButton.setOnClickListener(v -> {
            buttonToggle(ALL);
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
                currentButton = findViewById(R.id.follower_todays_habits_btn);
                otherButton = findViewById(R.id.follower_all_habits_btn);
                todaysHabitsView.setVisibility(View.INVISIBLE);                  //Hide the listview, show the recycler view.
                allHabitsView.setVisibility(View.VISIBLE);
            }
            else {
                currentButton = findViewById(R.id.follower_all_habits_btn);
                otherButton = findViewById(R.id.follower_todays_habits_btn);
                todaysHabitsView.setVisibility(View.VISIBLE);                    //Hide the recycler, show the list view.
                allHabitsView.setVisibility(View.INVISIBLE);
            }
            swapColor(currentButton, otherButton);
            buttonSelected = (buttonSelected + 1) % 2;                  //Swap the state
        }
    }

    /**
     * Helper function to button toggle. "Swaps" the colors of the buttons.
     * @param current the button that needs to be toggled off
     * @param other the button that needs to be toggled on
     */
    private void swapColor(Button current, Button other) {
        //De-select the current button
        other.setBackgroundTintList(getResources().getColorStateList(R.color.theme_secondary));   //Different setter due to material button
        other.setTextColor(getResources().getColor(R.color.theme_primary));

        //Select the other button
        current.setBackgroundTintList(getResources().getColorStateList(R.color.theme_primary));
        current.setTextColor(getResources().getColor(R.color.theme_secondary));
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
     * set the header view for activity
     */
    private void setUsername() {
        TextView usernameTextView = findViewById(R.id.follower_dashboard_title);

        String headerContent = username;

        usernameTextView.setText(headerContent+"'s Habits");
    }

    @Override
    public void callHabitList(ArrayList<Habit> habits) {
        setListAdapters();
    }

    @Override
    public void callUserList(ArrayList<User> requesters) {

    }

    @Override
    public void checkUser(boolean[] has) {

    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {

    }

    @Override
    public void callUri(Uri uri) {
        Glide.with(this)
                .load(uri)
                .into(followerProfilePic);
    }
}