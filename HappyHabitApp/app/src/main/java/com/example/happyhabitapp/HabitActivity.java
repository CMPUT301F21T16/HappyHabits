package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//TODO: Create Brand new RecycleView Adapter, Implement HelperAdapter Interface, habitTouchHelper class, and make sure all elements are changeable.

public class HabitActivity extends AppCompatActivity implements HabitListener, Add_Edit_Fragment.onFragmentInteractionListener {

    //Private variables
    private User currentUser;   //Contains habit list, profile picture, and username
    private RecyclerView habitViewList;
    private ImageButton addButton; // button to add habits to list
    private HabitsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);


        ArrayList<Habit> testList = new ArrayList<Habit>();
        int[] weekFreq = {1,1,0,1,1,1,1};
        testList.add(new Habit("A", "B", null, weekFreq));
        testList.add(new Habit("C", "D", null, weekFreq));
        testList.add(new Habit("E", "F", null, weekFreq));


        currentUser = new User("TestUser", "somePath", testList, null);
        initActivity();
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
        setUsername();
        setButtonListeners();
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
        adapter = new HabitsAdapter(currentUser.getHabitList(), this);      //Connect list to our own custom adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.habit_list);   //Select our RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));        //Set our data to be displayed linearly (instead of grid, etc.)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new HabitTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        //TODO: Look at this part in the tutorial again?
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);                                           //Attach our RecycleView to our list via adapter.
    }

    private void setUsername(){
        TextView usernameField = (TextView) findViewById(R.id.username);
        usernameField.setText(currentUser.getUsername());
    }

    /**
     * Initializes Listeners for all buttons in the Activity
     */
    private void setButtonListeners(){
        addButton = findViewById(R.id.add_habit_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Add_Edit_Fragment.show(getSupportFragmentManager(),"ADD_HABIT");
            }
        });
    }


    /**
     * Launches a new instance of
     * @param position
     *       the position of a selected habit in the data list of Habits
     */
    public void onHabitClick(int position){
        Habit selectedHabit = currentUser.getHabitList().get(position);
        //Go to new add/edit fragment
        DialogFragment newFragment = Add_Edit_Fragment.newInstance(selectedHabit);
        newFragment.show(getSupportFragmentManager(),"EDIT_HABIT");
    }

    /**
     * Adds a new Habit to the list when the ADD button is pressed on
     * the Add_Edit_fragment
     * @param newHabit
     *      a Habit to add to the list
     */
    @Override
    public void onAddPressed(Habit newHabit){
        // Needs to firebase implementation
        // this code is likely to be replaced
        adapter.getHabitList().add(newHabit); // adds habit to data list
        adapter.notifyItemInserted(adapter.getItemCount() - 1); // notifies item at last position has been added
        adapter.notifyDataSetChanged(); // notifies adpater of change

    }

    /**
     * Edits the information of an existing Habit in the list by replacing the habit
     * with a new one with updated information
     * @param newHabit
     *      The new Habit that to add to the list
     * @param oldHabit
     *      The old Habit to be swapped with the new Habit
     */
    @Override
    public void onEditPressed(Habit newHabit, Habit oldHabit){
        List<Habit> dataList = adapter.getHabitList();
        int pos = dataList.indexOf(oldHabit);
        dataList.set(pos, newHabit);
        adapter.notifyDataSetChanged();
    }


}
