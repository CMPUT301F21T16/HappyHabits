package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ProfilePageActivity extends AppCompatActivity {

    private User currentUser;
    FollowsAdapter followsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        currentUser = getUser();
        setPreliminaryInfo();
        setList();
        setButtons();
    }

    private User getUser(){
        //Some firebase thing or something that return the User

    }

    /**
     * This method gets and sets the username fields, since date field, and follower/followee counts
     * in the layout
     */
    private void setPreliminaryInfo() {
        TextView usernameTextView = findViewById(R.id.profile_page_username);
        TextView startDateTextView = findViewById(R.id.profile_page_start_date);
        TextView followerCountTextView = findViewById(R.id.follower_count);
        TextView followCountTextView = findViewById(R.id.following_count);

        usernameTextView.setText(currentUser.getUsername());
        startDateTextView.setText(currentUser.getStartDate());
        followerCountTextView.setText(currentUser.getFollowerList().size());
        followCountTextView.setText(currentUser.getFollowList().size());
    }

    private void setList() {
        //Set list
        followsAdapter = new FollowsAdapter(this, currentUser.getFollowerList(), currentUser, true);    //View only
        ListView userRequests = findViewById(R.id.follow_req_list);
        userRequests.setAdapter(followsAdapter);
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
            followerIntent.putExtra("GET_FOLLOW_STATE", "followers");
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
            followerIntent.putExtra("GET_FOLLOW_STATE", "following");
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




}