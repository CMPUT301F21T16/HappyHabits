package com.example.happyhabitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomHabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habitList;
    private Context context;

    /**
     * This is a constructor for a Custom list of Habits
     * @param context
     * @param habit
     *      ArrayList of Habits
     */
    public CustomHabitList(Context context, ArrayList<Habit> habit) {
        super(context,0, habit);
        this.habitList = habit;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.customlist_content, parent, false);
        }

        Habit habit = habitList.get(position);

        return view;
    }
}
