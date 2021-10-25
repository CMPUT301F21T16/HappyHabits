package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Add_Edit_Fragment.onFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Access a Cloud Firestore instance from your Activity
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        Calendar date = Calendar.getInstance();
        date.set(2000,3,5);
        int[] arry = {0,0,0,1,0,0,1};
        Habit h = new Habit("dsd","ewrerwre",date, arry);
        final FloatingActionButton addHabitButton = findViewById(R.id.add_habit);

        final FloatingActionButton editHabitButton = findViewById(R.id.edit_habit);

        addHabitButton.setOnClickListener((v) -> {
            DialogFragment frag = Add_Edit_Fragment.newInstance(null);
            frag.show(getSupportFragmentManager(),"ADD_HABIT");

        });



        editHabitButton.setOnClickListener((v) -> {
            DialogFragment newFragment = Add_Edit_Fragment.newInstance(h);
            newFragment.show(getSupportFragmentManager(),"EDIT_HABIT");
        });
    }

    public void onAddPressed(Habit newHabit) {
        //do nothing
    }

    @Override
    public void onEditPressed(Habit newHabit, Habit oldHabit) {

    }
}