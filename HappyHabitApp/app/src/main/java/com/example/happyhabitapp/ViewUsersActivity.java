package com.example.happyhabitapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUsersActivity extends AppCompatActivity {

    //TODO: Add an attribute that tells whether header is "Followers" or is "Followees"
    String followState; //Whether followers or followees are to be listed.
    FollowsAdapter followsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_followers_followees);
        Intent intent = getIntent();
        followState = intent.getStringExtra("GET_FOLLOW_STATE");
        setPreliminaryInfo();
        setList();
        setBackButton();
    }

    private void setPreliminaryInfo() {
        TextView usernameTextView = findViewById(R.id.follow_list_username);
        TextView listHeader = findViewById(R.id.following_or_followee);

        usernameTextView.setText(currentUser.getUsername());
        listHeader.setText(followState.toUpperCase());
    }

    private void setList() {
        if (followState.equals("followers")) {
            followsAdapter = new FollowsAdapter(this, currentUser.getFollowerList(), currentUser, false);
        }
        else {
            followsAdapter = new FollowsAdapter(this, currentUser.getFollowList(), currentUser, false);
        }
        ListView followUsers = findViewById(R.id.follow_req_list);
        followUsers.setAdapter(followsAdapter);
    }

    private void setBackButton() {
        ImageView backButton = findViewById(R.id.follow_list_back_btn);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(ViewUsersActivity.this, ProfilePageActivity.class);
            startActivity(backIntent);
        });
    }
}
