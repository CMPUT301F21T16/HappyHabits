package com.example.happyhabitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders a RecyclerView of {@link HabitViewHolder} instances with data passed from {@link User}'s habit list.
 * This list is non-interactable
 */
public class NoTouchHabitAdapter extends RecyclerView.Adapter {

    private ArrayList<Habit> habitList;
    private String username;

    //For use on the dashboard beloninging to logged-in user
    public NoTouchHabitAdapter(ArrayList<Habit> habitList) {
        this.habitList = habitList;
        this.username = "";
    }

    public NoTouchHabitAdapter(ArrayList<Habit> habitList, String username) {
        this.habitList = habitList;
        this.username = username;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist_content, parent, false);
        if(username != "") {
            return new HabitViewHolder(view, username);     //construct if a different user is passed in
        }
        else {
            return new HabitViewHolder(view);               //construct if no other user is passed in
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Takes each element in the list and binds it to a viewHolder
        ((HabitViewHolder) holder).attachData(habitList.get(position));
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public List<Habit> getHabitList(){
        return habitList;
    }
}


