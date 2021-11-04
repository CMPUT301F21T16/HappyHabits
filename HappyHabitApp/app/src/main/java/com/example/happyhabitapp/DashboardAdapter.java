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
 * Creates an adapted for the non-interactable listview of today's habits in the dashboard activity
 */
public class DashboardAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;


    public DashboardAdapter(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.customlist_content, parent, false);
        }

        Habit habit = habits.get(pos);

        //Getters
        TextView habitTitle = view.findViewById(R.id.habit_title);
        TextView habitReason = view.findViewById(R.id.reason_text);
        TextView habitFreq = view.findViewById(R.id.selected_dates);
        Button dragHandle = view.findViewById(R.id.drag_handle);

        //Setters
        habitTitle.setText(habit.getTitle());
        habitTitle.setText(habit.getReason());
        habitTitle.setText(habit.getWeekAsStr());
        dragHandle.setVisibility(View.GONE);        //Since non-intractable, hide the drag handle

    return view;
    }
}
