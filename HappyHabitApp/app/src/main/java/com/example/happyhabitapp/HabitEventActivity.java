package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * This class represents the activity that displays all of the habit's events
 * and as an intermediary between the adding/editing of those events.
 */

//TODO: Create a view model, adapter, and listener for the habit events
//TODO: Render all view models and connect to the adapter
public class HabitEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);
    }




}