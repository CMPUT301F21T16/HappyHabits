package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.ImageView;

public class HabitActivity extends AppCompatActivity {

    //Private variables
    private User currentUser;   //Contains habit list, profile picture, and username
    private RecyclerView habitViewList;
    private arrayAdapter<Habit> habitAdapter;

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

    private void initListeners(){

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
        ArrayList<Habit> habitDetailList = currentUser.getHabitList();  //List that data is drawn from
        habitAdapter = new CustomHabitList(this, habitDetailList);
        new ItemTouchHelper(makeItemTouchCallback(habitDetailList)).attachToRecyclerView(habitViewList);   //Connect the list to itemTouchHelper
        habitViewList.setAdapter(habitAdapter);



    }


    private ItemTouchHelper.Callback makeItemTouchCallback(ArrayList<Habit> detailList) {
        ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelper.Callback(0, ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                detailList.remove(viewHolder.getAdapterPosition());
                habitAdapter.notifyDataSetChanged();
            }
        };
        return itemTouchCallback;
    }




}
