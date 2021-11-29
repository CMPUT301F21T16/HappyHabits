package com.example.happyhabitapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProfilePageActivity extends AppCompatActivity implements FirestoreCallback{

    FollowsAdapter followsAdapter;
    private FireBase fire = new FireBase(); // FireBase

    private ArrayList<User> followerList = new ArrayList<User>();
    private ArrayList<User> followeeList = new ArrayList<User>();       //To be passed into Firebase to get lists back
    private ArrayList<User> pendingRequests = new ArrayList<User>();
    private Integer follower_num;
    private Integer followee_num;
    private boolean[] has_user = {false};

    String request_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fire.setApi(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        fire.getFollowerList(followerList);
        fire.getFolloweeList(followeeList);
        fire.getRequestList(pendingRequests);

//        setList();
//        setPreliminaryInfo();
        setButtons();
        setRequestSender();
    }

    /**
     * This method gets and sets the username fields, since date field, and follower/followee counts
     * in the layout
     */
    private void setPreliminaryInfo() {
        TextView usernameTextView = findViewById(R.id.profile_page_username);
        TextView followerCountTextView = findViewById(R.id.follower_count);
        TextView followCountTextView = findViewById(R.id.following_count);
        usernameTextView.setText(fire.getUserName());
        follower_num = followerList.size();
        followee_num = followeeList.size();
        followerCountTextView.setText(follower_num.toString()); //Get the count of the followers/followees
        followCountTextView.setText(followee_num.toString());
    }

    /**
     * Sets the user
     */
    private void setList() {
        //Set list
        followsAdapter = new FollowsAdapter(this, pendingRequests,true);    //View only
        ListView userRequests = findViewById(R.id.follow_req_list);
        userRequests.setAdapter(followsAdapter);
        followsAdapter.notifyDataSetChanged();
    }

    private void setButtons() {
        setFollowerButton();
        setFollowingButton();
        setBackButton();
    }

    /**
     * Defines and launches a new intent.
     * Go to the users page with a flag that tells the new activity which list (followers) to source from.
     */
    private void setFollowerButton() {
        LinearLayout followerButton = findViewById(R.id.follower_btn);
        followerButton.setOnClickListener(v -> {
            Intent followerIntent = new Intent(ProfilePageActivity.this, ViewUsersActivity.class);
            followerIntent.putExtra("GET_FOLLOW_STATE", "Followers");
            startActivity(followerIntent);
        });
    }

    /**
     * Defines and launches a new intent.
     * Go to the users page with a flag that tells the new activity which list (following) to source from.
     */
    private void setFollowingButton() {
        LinearLayout followingButton = findViewById(R.id.following_btn);
        followingButton.setOnClickListener(v -> {
            Intent followerIntent = new Intent(ProfilePageActivity.this, ViewUsersActivity.class);
            followerIntent.putExtra("GET_FOLLOW_STATE", "Following");
            startActivity(followerIntent);
        });
    }

    /**
     * Defines and launches a new intent.
     * Returns the user to the dashboard activity.
     */
    private void setBackButton() {
        ImageView backButton = findViewById(R.id.profile_page_back_btn);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(ProfilePageActivity.this, MergedDisplayActivity.class);
            startActivity(backIntent);
        });
    }

    /**
     * Implements user send request functionality within the user "search bar"
     */
    private void setRequestSender() {
        EditText requestContent = findViewById(R.id.req_to_username);
        ImageView sendButton = findViewById(R.id.send_request);

        sendButton.setOnClickListener(v -> {
            request_name = requestContent.getText().toString();
            fire.hasUser(request_name, has_user);
            requestContent.setText("");



        });
    }




    /* ===================================================================== Method for FirestoreCallback ========================================================================== */
    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {
        setList();
        setPreliminaryInfo();
    }

    @Override
    public void checkUser(boolean[] has) {
        if (has_user[0]){
            fire.sendRequest(request_name);
        }else{
            Toast.makeText(this, "User Doesn't Exits!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {
    }
}