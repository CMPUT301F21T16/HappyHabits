package com.example.happyhabitapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ViewUsersActivity extends AppCompatActivity implements FirestoreCallback{


    //TODO: Connect to firebase here

    String followState; //Whether followers or followees are to be listed.
    FollowsAdapter followsAdapter;

    ArrayList<User> followerList = new ArrayList<User>();
    ArrayList<User> followeeList = new ArrayList<User>();
    ListView followUsers;


    FireBase fire = new FireBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fire.setApi(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_followers_followees);
        followUsers = findViewById(R.id.user_list);

        Intent intent = getIntent();
        followState = intent.getStringExtra("GET_FOLLOW_STATE"); //Get what type of data is to be rendered

        fire.getFolloweeList(followeeList); //get the follower/followee list from firebase
        fire.getFollowerList(followerList);
        setPreliminaryInfo();
        setList();
        setBackButton();
    }

    private void setPreliminaryInfo() {
        TextView usernameTextView = findViewById(R.id.follow_list_username);
        TextView listHeader = findViewById(R.id.following_or_followee);
        // to display the userProfile from firebase storage
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            if (currentUser.getPhotoUrl() != null){
                ImageView userProfile = findViewById(R.id.follow_list_pic);
                Glide.with(this)
                        .load(currentUser.getPhotoUrl())
                        .into(userProfile);
            }
        }

        usernameTextView.setText(fire.getUserName());
        listHeader.setText(followState);
    }

    private void setList() {
        if (followState.equals("Followers")) {
            followsAdapter = new FollowsAdapter(this, followerList, false);
        }
        else {
            followsAdapter = new FollowsAdapter(this, followeeList, false);
        }

        followUsers.setAdapter(followsAdapter);
    }

    private void setBackButton() {
        ImageView backButton = findViewById(R.id.follow_list_back_btn);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(ViewUsersActivity.this, ProfilePageActivity.class);
            startActivity(backIntent);
        });
    }



    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {
        setPreliminaryInfo();
        setList();
    }

    @Override
    public void checkUser(boolean[] has) {

    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {
    }

    @Override
    public void callUri(Uri uri) {

    }
}
