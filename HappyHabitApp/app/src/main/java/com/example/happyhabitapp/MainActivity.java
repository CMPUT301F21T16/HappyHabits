package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Just launch habit activity for now
        Intent testIntent = new Intent(MainActivity.this, HabitActivity.class);
        startActivity(testIntent);
    }





    // Access a Cloud Firestore instance from your Activity
    //FirebaseFirestore db = FirebaseFirestore.getInstance();
}