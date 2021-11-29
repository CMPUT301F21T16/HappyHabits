package com.example.happyhabitapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//TODO: Add adapters. Make sure that the recycler adapter notifies the list view of any changes when they occur. --DONE
//TODO: Add button functionalities to toggle which view is displayed.                                            --DONE
//TODO: Update the Username, profile pictures and habits according to firebase.
//TODO: Add the FAB to open add/edit fragment                                                                    --DONE
//TODO: Refactor adapters/activities/etc...

public class MergedDisplayActivity extends AppCompatActivity

        implements HabitListener, Add_Edit_Fragment.onFragmentInteractionListener, FirebaseAuth.AuthStateListener, FirestoreCallback, EditOrViewFragment.onFragmentInteractionListener{


    //Firebase-specific attributes

    private final String TAG = "MergedDisplayActivity";       //Contains a log of firebase activity


    //Activity-related attributes
    private int TODAY = 0;         //Constants for changing the display type
    private int ALL= 1;

    private User currentUser;      //Change this to firebase??
    private HabitsAdapter recyclerAdapter;  //For the view of all habits (interactable)
    private DashboardAdapter listAdapter;   //For the view of today's habits (view only)
    private int buttonSelected = TODAY;     //Indicates what state buttons are in.

    private ArrayList<Habit> habitList = new ArrayList<Habit>();

    private ListView listView;              //Preserve information on visibility swaps
    private RecyclerView recyclerView;

    private FireBase fire = new FireBase(); // FireBase
    private boolean[] existDiaplayName = {false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fire.setApi(this);
        User user = new User(fire.getUserName());
        fire.setUser(user);
        fire.displayNameExists(fire.getUserName(), fire.getCurrent_uid(), existDiaplayName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_display);
        fire.getHabitList(habitList);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        //Eventually get from the login-screen. For now, make a dummy version.
        Calendar today = Calendar.getInstance();




        setAdapters();
        setButtonListeners();
        setUsername();
    }

    //---Firebase related methods---

    /**
     * Takes user back to the log-in screen
     */
    private void startLogin(){
        startActivity(new Intent(MergedDisplayActivity.this, LogInActivity.class));
        this.finish();
    }

    /**
     * Displays the three dots to prompt log out in the top-right of header
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    /**
     * Displays relevant options to user when the three dots are pressed
     *
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.logout){
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startLogin();
                            }
                            else{
                                Log.e(TAG, "onComplete", task.getException());
                            }
                        }
                    });
        }
        return true;
    }

    /**
     * Works with onStop and onAuthStateChanged to verify user
     */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    /**
     * Works with onStart and onAuthStateChanged to verify user
     */
    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    /**
     *  Works with onStart and onStop to verify user
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startLogin();
            return;
        }
        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(getTokenResult -> Log.d(TAG, "onSuccess" + getTokenResult.getToken()));
    }


    //---Display related methods---


    /**
     * Fills the username field
     */
    private void setUsername(){
        TextView username = findViewById(R.id.userName); //Delete the duplicate "username" ID in activity_habit.xml
        username.setText(fire.getUserName());
    }

    /**
     * Sets up the adapter for the list view
     * and the recycler view (initially hidden)
     */
    private void setAdapters(){
        setRecyclerAdapter();
        setListAdapter();
    }

    /**
     * Sets an adapter that supports gestures on all habit entries
     * including swipe to delete, tap to edit/view, longpress+drag to re-order.
     * Sources from all of the user's habits.
     */
    private void setRecyclerAdapter(){

        recyclerAdapter = new HabitsAdapter(habitList, this);
        recyclerView = findViewById(R.id.habits_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //Set data to be displayed linearly (instead of grid, etc...)
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new EventAndHabitTouchHelper(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerAdapter.setTouchHelper(itemTouchHelper);                    //Add gesture support via callbacks
        itemTouchHelper.attachToRecyclerView(recyclerView);                 //Connect to the recyclerview

        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Sets an adapter that only supports static listing of habit entries. No interaction.
     * Sources from user's habits that coincide with today's date.
     */
    private void setListAdapter(){


        listAdapter = new DashboardAdapter(this, getTodaysHabits(habitList));

        listView = findViewById(R.id.todays_habits_list);
        listView.setAdapter(listAdapter);
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
     * Sets all button listeners in the activity
     */
    private void setButtonListeners() {

        Button todayButton = findViewById(R.id.todays_habits_btn);
        Button allButton = findViewById(R.id.all_habits_btn);
        FloatingActionButton addButton = findViewById(R.id.add_habit_btn);
        ImageView profileIcon = findViewById(R.id.dashboard_profile_pic);

        todayButton.setOnClickListener((View v) -> {
            buttonToggle(TODAY);
        });

        allButton.setOnClickListener((View v) -> {
            buttonToggle(ALL);
        });

        //Go to add/edit habit fragment
        addButton.setOnClickListener((View v) -> {

            DialogFragment addFragment = new Add_Edit_Fragment();
            Bundle args = new Bundle();
            args.putSerializable("habit", null);
            addFragment.setArguments(args);

            addFragment.show(getSupportFragmentManager(), "ADD_HABIT");
        });

        profileIcon.setOnClickListener((View v) -> {
            Intent profileIntent = new Intent(MergedDisplayActivity.this, ProfilePageActivity.class);
            startActivity(profileIntent);
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
               currentButton = findViewById(R.id.todays_habits_btn);
               otherButton = findViewById(R.id.all_habits_btn);
               listView.setVisibility(View.INVISIBLE);                  //Hide the listview, show the recycler view.
               recyclerView.setVisibility(View.VISIBLE);
            }
            else {
               currentButton = findViewById(R.id.all_habits_btn);
               otherButton = findViewById(R.id.todays_habits_btn);
               listView.setVisibility(View.VISIBLE);                    //Hide the recycler, show the list view.
               recyclerView.setVisibility(View.INVISIBLE);
//               setListAdapter();    //Re-draw listView in case any habits were removed
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
     * Helper method for the recycler adapter
     * @param position
     */
    @Override
    public void onHabitClick(int position) {
        //TODO: Have an intermediary fragment that asks user where they would like to be directed.
//        Habit selectedHabit = currentUser.getHabitList().get(position);
        Habit selectedHabit = habitList.get(position);

        //Go to new add/edit fragment

        DialogFragment newFragment = new EditOrViewFragment();
        Bundle args = new Bundle();
        args.putSerializable("habit", selectedHabit);
        newFragment.setArguments(args);

        newFragment.show(getSupportFragmentManager(),"EDIT_HABIT");
    }

    @Override
    public void onAddPressed(Habit newHabit) {
//        currentUser.addHabit(newHabit);             //adds habit to data list
        fire.setHabit(newHabit);
        fire.getHabitList(habitList);
        setAdapters();


//        if (recyclerAdapter.getItemCount() == 1) {
//            setRecyclerAdapter();                   //re-bind adapter if list was empty
//        }
//        else {
//            recyclerAdapter.notifyDataSetChanged(); //notifies adapter of change
//        }
        buttonToggle(ALL);                          //Swap to the all habits view to see change
    }

    @Override
    public void onEditPressed(Habit newHabit, Habit oldHabit) {
        List<Habit> dataList = recyclerAdapter.getHabitList();
        int pos = dataList.indexOf(oldHabit);
        dataList.set(pos, newHabit);
        recyclerAdapter.notifyDataSetChanged();
        fire.delHabit(oldHabit);
        fire.setHabit(newHabit);
    }

    /**
     * Launches an AddEditFragment in order to allow
     * User to edit an existing habit
     */
    @Override
    public void goToEdit(Habit habit) {
        DialogFragment newFragment = new Add_Edit_Fragment();
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "Edit Habit");
    }
    /* =============================================================== Methods for FirestoreCallback ======================================================= */
    @Override
    public void callHabitList(ArrayList<Habit> habits) {
        setAdapters();

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {
        return;
    }

    @Override
    public void checkUser(boolean[] has) {
        if (has[0] == true) {
            Toast.makeText(this, "Username Already Used", Toast.LENGTH_SHORT).show();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
            AuthCredential credential = EmailAuthProvider
                    .getCredential("user@example.com", "password1234");

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                            }
                                        }
                                    });
                        }
                    });
        } else if (has[0] == false){
            User user = new User(fire.getUserName());
            fire.setUser(user);
        }

    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {

    }
    /* =========================================================================================================================================================== */

    /**
         * Launches the HabitEvents Activity to allow user to view
         * HabitEvents associated with a Habit
         */
    @Override
    public void goToEvents(Habit habit) {
        Intent eventActivity = new Intent( MergedDisplayActivity.this , HabitEventActivity.class);
        eventActivity.putExtra("habit", habit); // pass in the activity
        startActivity(eventActivity);
    }
}