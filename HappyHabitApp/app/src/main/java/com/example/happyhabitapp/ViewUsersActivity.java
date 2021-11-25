package com.example.happyhabitapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUsersActivity extends AppCompatActivity {

    //TODO: Add an attribute that tells whether header is "Followers" or is "Followees"
    String followState; //Whether followers or followees are to be listed.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_followers_followees);
        getFollowState();
    }


}
