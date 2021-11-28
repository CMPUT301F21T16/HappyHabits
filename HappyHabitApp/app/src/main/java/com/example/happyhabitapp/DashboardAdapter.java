package com.example.happyhabitapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * An adapter for the non-interactable listview of today's habits in the dashboard activity
 * @author Jonathan
 */
public class DashboardAdapter extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    private ProgressBar progressBar;
    private TextView progressBarText;
    private View view;


    public DashboardAdapter(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * Inflates the view of each element in the file.
     */
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        this.view = view;           //Required for progress bar manipulation

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
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView progressText = view.findViewById(R.id.progress_text);

        //Setters
        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitFreq.setText(habit.getWeekAsStr());
        //setProgressOnBar(habit);

    return view;
    }

    private void setProgressOnBar(Habit habit) {
        getProgressOnBar(habit);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBarText = view.findViewById(R.id.progress_text);
    }

    /**
     * Colors the progress bar using its percentage
     * @param percentage an int representing the percentage of the progress bar
     */
    private void fillProgressBar(int percentage) {
        final LayerDrawable progressDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable progressPortion = progressDrawable.getDrawable(1);                             //Get the top layer

        if (percentage <= 33) {
            progressBarText.setTextColor(view.getContext().getResources().getColor(R.color.progress_indicator_low));
            //progressBar.setProgressTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.progress_indicator_low)));
            progressPortion.setTint(view.getContext().getResources().getColor(R.color.progress_indicator_low));
        }
        else if (percentage <= 66) {
            progressBarText.setTextColor(view.getContext().getResources().getColor(R.color.progress_indicator_mid));
            //progressBar.setProgressTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.progress_indicator_mid)));
            progressPortion.setTint(view.getContext().getResources().getColor(R.color.progress_indicator_mid));
        }
        else {
            progressBarText.setTextColor(view.getContext().getResources().getColor(R.color.progress_indicator_high));
            //progressBar.setProgressTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.progress_indicator_high)));
            progressPortion.setTint(view.getContext().getResources().getColor(R.color.progress_indicator_high));
        }
        progressBar.setProgress(percentage);
    }

    /**
     * Determines what portion of the bar is filled, and what color it is to be set to.
     */
    private void getProgressOnBar(Habit habit) {
        // the body is in callEvents for firebase asynchronous access
    }




}
