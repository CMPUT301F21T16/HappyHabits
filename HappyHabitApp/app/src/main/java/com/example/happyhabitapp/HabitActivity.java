package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

//TODO: Create Brand new RecycleView Adapter, Implement HelperAdapter Interface, habitTouchHelper class, and make sure all elements are changeable.

public class HabitActivity extends AppCompatActivity implements HabitListener, Add_Edit_Fragment.onFragmentInteractionListener {

    //Private variables
    private User currentUser;   //Contains habit list, profile picture, and username
    private RecyclerView habitViewList;
    private ImageButton addButton; // button to add habits to list
    private HabitsAdapter adapter;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        ArrayList<Habit> testList = new ArrayList<Habit>();

        currentUser = new User("TestUser", "somePath", testList, null);
        initActivity();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startLogin();
        }

    }

    /**
     * this function will start login activity
     */
    private void startLogin(){
        startActivity(new Intent(HabitActivity.this, MainActivity.class));
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

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
        setListeners();
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
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.Callback callback = new HabitTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
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
    private void setListeners(){
        addButton = findViewById(R.id.add_habit_btn);
        ImageView backButton = findViewById(R.id.logo);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_Edit_Fragment newFragment = new Add_Edit_Fragment(null);
                newFragment.show(getSupportFragmentManager(),"ADD_HABIT");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previousActivity   = new Intent(HabitActivity.this, DashBoard.class);
                startActivity(previousActivity);
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
        Add_Edit_Fragment newFragment = new Add_Edit_Fragment(selectedHabit);
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
