package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfilePageActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        currentUser = getUser();
        setPreliminaryInfo();
        setList();
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
    }

}