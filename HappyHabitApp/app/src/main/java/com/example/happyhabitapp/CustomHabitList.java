package com.example.happyhabitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This is a Class for a Custom ArrayAdapter
 * This stores a list of Habits and displays their attributes
 * into a View
 */
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

        // get instance of Habit
        Habit habit = habitList.get(position);

        // get TextViews to display Habit attributes
        TextView habitName = view.findViewById(R.id.habitName);
        TextView habitFrequency = view.findViewById(R.id.habitFrequency);
        TextView habitReason = view.findViewById(R.id.habitReason);

        //Get attributes from Habits
        String name = habit.getTitle();
        String reason = habit.getReason();

        /* To do:
            translate the week_freq array into strings to display
         */

        habitName.setText(name);
        habitReason.setText(reason);



        return view;
    }
}
