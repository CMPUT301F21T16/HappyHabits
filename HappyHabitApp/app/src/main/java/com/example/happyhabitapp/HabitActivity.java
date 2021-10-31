package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
        setList();
        //setImage();
        //setUsername();
    }


    //private void setImage() {
        //Unsure how to do this without knowing fireBase implementation
    //}

    /**
     * Sets up the list and all relevant listeners.
     */

    //TODO: have to set up an item touch helper to assist in re-ordering, and swipe
    private void setList() {
        //Connect the adapter to the recyclerView
        HabitsAdapter adapter = new HabitsAdapter(currentUser.getHabitList());      //Connect list to our own custom adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.habit_list);   //Select our RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));        //Set our data to be displayed linearly (instead of grid, etc.)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new HabitTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //TODO: Look at this part in the tutorial again?
        //something
        //something

        recyclerView.setAdapter(adapter);                                           //Attach our RecycleView to our list via adapter.
    }

//    private void setUsername(){
//        TextView usernameField = (TextView) findViewById(R.id.username);
//        usernameField.setText(currentUser.getUsername());
//    }
}
