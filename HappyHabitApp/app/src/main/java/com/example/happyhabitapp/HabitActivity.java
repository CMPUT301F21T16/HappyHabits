package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//TODO: Create Brand new RecycleView Adapter, Implement HelperAdapter Interface, habitTouchHelper class, and make sure all elements are changeable.

public class HabitActivity extends AppCompatActivity {

    //Private variables
    private User currentUser;   //Contains habit list, profile picture, and username
    private RecyclerView habitViewList;
    private HabitListRecyclerAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
    }

    private void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Sets all initial elements (profile picture, current list, username)
     * Sets all eventListeners (AddButton, deleteSwipe, longPressEdit, re-order)
     */
    private void setUpActivity(){
        initActivity();
    }

    private void initActivity() {
        setImage();
        setList();
        setUsername();
    }


    private void setImage() {
        //Unsure how to do this without knowing fireBase implementation
    }

    /**
     * Sets up the list and all relevant listeners.
     */

    //TODO: have to set up an item touch helper to assist in re-ordering, and swipe
    private void setList() {
        habitViewList = findViewById(R.id.habit_list);                  //What is to be displayed
        List<Habit> habitDetailList = currentUser.getHabitList();  //List that data is drawn from
        habitAdapter = new HabitListRecyclerAdapter(habitDetailList);
        habitViewList.setAdapter(habitAdapter);
    }

    private void setUsername(){
        TextView usernameField = (TextView) findViewById(R.id.username);
        usernameField.setText(currentUser.getUsername());
    }
}
