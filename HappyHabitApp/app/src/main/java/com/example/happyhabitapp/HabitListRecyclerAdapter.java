package com.example.happyhabitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom adapter for the recycler view in habit list
 */

//Sources:
    //https://www.willowtreeapps.com/craft/android-fundamentals-working-with-the-recyclerview-adapter-and-viewholder-pattern
    //https://guides.codepath.com/android/using-the-recyclerview

public class HabitListRecyclerAdapter extends RecyclerView.Adapter<HabitViewHolder> {

    private List<Habit> habitList;

    /**
     * Constructor for the List that needs to be rendered
     * @param habits A List object containing HabitViewModels (NOT a Habit class)
     */
    public HabitListRecyclerAdapter(List<Habit> habits) {
        //Convert ArrayList to a List object
        this.habitList = habits;
    }

    @Override
    public HabitViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.customlist_content, parent, false);

        HabitViewHolder habitViewHolder = new HabitViewHolder(view);

        return habitViewHolder;
    }

    @Override
    public void onBindViewHolder(HabitViewHolder habitViewHolder, int position) {
        habitViewHolder.attachData(habitList.get(position));
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }
}