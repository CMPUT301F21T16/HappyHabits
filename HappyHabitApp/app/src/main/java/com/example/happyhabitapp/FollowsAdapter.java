package com.example.happyhabitapp;

import android.content.Context;
import android.content.Intent;
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

    ArrayList<User> users;
    private Context context;
    boolean isAcceptable;


    private FireBase fire = new FireBase();

    /**
     * Constructor for the adapter
     * @param context the context of the list
     * @param isAcceptable a boolean representing whether or not the user can be accepted/rejected
     */
    public FollowsAdapter(Context context, ArrayList<User> users, boolean isAcceptable) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
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

        //Do not render the buttons if the users are already accepted as followers
        if (!isAcceptable) {
            acceptReqButton.setVisibility(View.GONE);
            rejectReqButton.setVisibility(View.GONE);

            //Set a listener to go to the user's dashboard instead
            view.setOnClickListener(v -> {
                //TODO: Pass the selected username to the dashboard

                Intent toUserDashboard = new Intent(getContext(), FollowerHabitsActivity.class);
                toUserDashboard.putExtra("username", requestingUser.getUsername());
                getContext().startActivity(toUserDashboard);
            });
        }
        //Otherwise attach listeners to accept/reject
        else {
            acceptReqButton.setOnClickListener(v -> {
                //Add to users followers
                fire.delRequst(requestingUser);       //Remove from the pending list in firebase
                users.remove(requestingUser);       //Remove from the adapted list
                fire.setFollowers(requestingUser);//Add to the followers list in firebase
                notifyDataSetChanged();
            });
            rejectReqButton.setOnClickListener(v -> {
                fire.delRequst(requestingUser);//Remove from the pending list in firebase
                users.remove(requestingUser);
            });
        }
        return view;
    }


}
