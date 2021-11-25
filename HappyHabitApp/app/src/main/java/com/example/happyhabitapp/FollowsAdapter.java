package com.example.happyhabitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * A class that adapts and inflates a list of users
 */
public class FollowsAdapter extends ArrayAdapter<User> {

    User currentUser;
    ArrayList<User> users;
    private Context context;
    boolean isAcceptable;

    /**
     * Constructor for the adapter
     * @param context the context of the list
     * @param currentUser the user who is viewing the other the list of users.
     * @param isAcceptable a boolean representing whether or not the user can be accepted/rejected
     */
    public FollowsAdapter(Context context, ArrayList<User> users, User currentUser, boolean isAcceptable) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
        this.currentUser = currentUser;
        this.isAcceptable = isAcceptable;
    }

    /**
     * Inflates the view of each element in the file.
     */
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        User requestingUser = users.get(pos);

        if (view == null) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.user_request_content, parent, false);
        }
        //Getters
        TextView usernameTextView = view.findViewById(R.id.follow_request_username);
        Button acceptReqButton = view.findViewById(R.id.accept_req_btn);
        Button rejectReqButton = view.findViewById(R.id.reject_req_btn);

        //Setters
        usernameTextView.setText(requestingUser.getUsername());

        //Do not render the buttons if the users are already accepted
        if (!isAcceptable) {
            acceptReqButton.setVisibility(View.GONE);
            rejectReqButton.setVisibility(View.GONE);
        }
        //Otherwise attach listeners
        else {
            acceptReqButton.setOnClickListener(v -> {
                //TODO: Add pending list to User?
                //Add to users followers
                currentUser.getPendingList().remove(requestingUser);
                currentUser.getFollowerList().add(requestingUser);
            });
            rejectReqButton.setOnClickListener(v -> {
                currentUser.getPendingList().remove(requestingUser);
            });
        }
        return view;
    }





}
