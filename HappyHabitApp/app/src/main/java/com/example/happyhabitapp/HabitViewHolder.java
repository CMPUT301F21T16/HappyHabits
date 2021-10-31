package com.example.happyhabitapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Represents the view of our recyclerView. To be used in tandem with the recycler adapter.
 */

public class HabitViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView reasonTextView;
    private TextView frequencyTextView;
    private ImageButton dragHandle;

    /**
     * Constructor to connect all the ViewHolder with the XML file
     * @param itemView some itemView that comes from somewhere
     */
    public HabitViewHolder(final View itemView){
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.habit_title);
        reasonTextView = (TextView) itemView.findViewById(R.id.reason_text);
        frequencyTextView = (TextView) itemView.findViewById(R.id.selected_dates);
        dragHandle = (ImageButton) itemView.findViewById(R.id.drag_handle);
    }

    /**
     * Sets all relevant fields to view model specifications
     * @param
     */
    public void attachData(final Habit habit) {
        titleTextView.setText(habit.getTitle());
        reasonTextView.setText(habit.getReason());
        frequencyTextView.setText(FreqToString(habit));
        //Add event listener elsewhere???
    }

    /**
     * Converts an array of integers to their respective days
     * @param habit
     * @return resultStr
     */
    //TODO: Convert from just numbers to the weekdays
    protected String FreqToString(final Habit habit) {
        int[] freqArray = habit.getWeek_freq();
        String resultStr = "";

        for (int i = 0; i < freqArray.length; i++) {
            String intAsStr = String.valueOf(freqArray[i]);
            resultStr += intAsStr ;
        }
        return resultStr;
    }











}
