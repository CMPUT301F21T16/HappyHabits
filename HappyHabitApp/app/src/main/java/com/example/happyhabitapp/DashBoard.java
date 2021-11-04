package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DashBoard extends AppCompatActivity {

    private User currentUser;
    private ArrayList<Habit> todaysHabits;
    private ArrayAdapter<Habit> habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        this.currentUser = getUser();
        todaysHabits = getTodaysHabits();
        setButton();
        setList();

        ImageView user_prof = (ImageView) findViewById(R.id.user_profile_pic);
        user_prof.setBackgroundResource(R.drawable.lol);
    }

    private User getUser(){
        //Eventually get from the login-screen. For now, make a dummy version.
        Calendar today = Calendar.getInstance();

        //-------TEST INFO - REMOVE LATER -------
        int[] selectedDates = {1,0,0,1,0,0,0};
        int[] selectedDates2 = {0,0,0,0,0,1,1};
        Habit habit1 = new Habit("Get Food", "I am hungry", today, selectedDates);
        Habit habit2 = new Habit("Feed dog", "They are hungry", today, selectedDates2);
        Habit habit3 = new Habit("Test the list", "Who knows if it works", today, selectedDates);

        ArrayList<Habit> testList = new ArrayList<Habit>();
        testList.add(habit1); testList.add(habit2); testList.add(habit3);
        //-----------------------------------
        currentUser = new User("TestUser", "somePath", testList, null);
        return currentUser;
    }

    private ArrayList<Habit> getTodaysHabits(){
        ArrayList<Habit> todaysHabits = new ArrayList<Habit>();
        ArrayList<Habit> allHabits = currentUser.getHabitList();
        Calendar today = Calendar.getInstance();

        for(int i = 0; i < currentUser.getHabitList().size(); i++) {
            Habit currentHabit = allHabits.get(i);
            //Adds habit if it the corresponding weekday has a value of 1
            //DAY_OF_WEEK sets Sunday to be 1, Monday to be 2, etc...
            if(currentHabit.getWeek_freq()[today.get(Calendar.DAY_OF_WEEK) - 1] == 1) {
                todaysHabits.add(currentHabit); //Preserves order
            }
        }
        return todaysHabits;
    }

    /**
     * Adds a view only list view from the user's list
     */
    private void setList(){
        habitAdapter = new DashboardAdapter(this, todaysHabits);    //View only
        ListView todaysHabitList = (ListView) findViewById(R.id.today_habit_list);
        todaysHabitList.setAdapter(habitAdapter);
    }

    /**
     * Connects the goToList Button with the Habits Activity
     */
    private void setButton(){
        Button goToListBtn = (Button) findViewById(R.id.go_to_list_button);
        goToListBtn.setOnClickListener(view ->
        {
            Intent habitActivity = new Intent(DashBoard.this, HabitActivity.class);
            //habitActivity.putExtra(User currentUser);
            startActivity(habitActivity);
        });
    }
}