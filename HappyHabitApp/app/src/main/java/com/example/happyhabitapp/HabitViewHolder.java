package com.example.happyhabitapp;

import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HabitViewHolder extends RecyclerView.ViewHolder {

    private TextView titleTextView;
    private TextView reasonTextView;
    private TextView frequencyTextView;
    private ImageButton dragHandle;

    public HabitViewHolder(View habitView) {
        super(habitView);

        titleTextView = (TextView) habitView.findViewById(R.id.habit_title);
        reasonTextView = (TextView) habitView.findViewById(R.id.reason_text);
        frequencyTextView = (TextView) habitView.findViewById(R.id.selected_dates);
        dragHandle = (ImageButton) habitView.findViewById(R.id.drag_handle);
    }

    public void attachData(Habit habit) {
        titleTextView.setText(habit.getTitle());
        reasonTextView.setText(habit.getReason());
        frequencyTextView.setText(habit.getWeekAsStr());
    }
}
