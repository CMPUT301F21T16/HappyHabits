package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Just launch habit activity for now (Delete when Main Activity is Implemented)
        Intent testIntent = new Intent(MainActivity.this, DashBoard.class);
        startActivity(testIntent);
    }





    // Access a Cloud Firestore instance from your Activity
    //FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Access a Cloud Firestore instance from your Activity
        //FirebaseFirestore db = FirebaseFirestore.getInstance();

    /*

    @Override
    public void onAddPressed(Habit newHabit) {
        //do nothing
    }

    @Override
    public void onEditPressed(Habit newHabit, Habit oldHabit) {

    }
    */


}