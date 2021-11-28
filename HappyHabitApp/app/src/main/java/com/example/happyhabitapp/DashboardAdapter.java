package com.example.happyhabitapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * An adapter for the non-interactable listview of today's habits in the dashboard activity
 * @author Jonathan
 */
public class DashboardAdapter extends ArrayAdapter<Habit> implements FirestoreCallback{

    private ArrayList<Habit> habits;
    private Context context;
    private FireBase fire = new FireBase();
    private ProgressBar progressBar;
    private TextView progressBarText;
    private View progressView;
    private String username;
    private ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();
    private Integer percentage;

    public DashboardAdapter(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        fire.setApi(this);
        this.habits = habits;
        this.context = context;
        this.username = "";
    }

    public DashboardAdapter(Context context, ArrayList<Habit> habits, String username){
        super(context, 0, habits);
        fire.setApi(this);
        this.habits = habits;
        this.context = context;
        this.username = username;
    }



    /**
     * Inflates the view of each element in the file.
     */
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.customlist_content, parent, false);
        }
        progressView = view;                  //Required for progress bar manipulation


        Habit habit = habits.get(pos);

        //Getters
        TextView habitTitle = view.findViewById(R.id.habit_title);
        TextView habitReason = view.findViewById(R.id.reason_text);
        TextView habitFreq = view.findViewById(R.id.selected_dates);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBarText = view.findViewById(R.id.progress_text);

        //Setters
        //habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitFreq.setText(habit.getWeekAsStr());
        setProgressOnBar(habit);

    return view;
    }

    private void setProgressOnBar(Habit habit) {
        getProgressOnBar(habit);
//        progressBar = view.findViewById(R.id.progress_bar);
//        progressBarText = view.findViewById(R.id.progress_text);
    }

    /**
     * Colors the progress bar using its percentage
     * @param percentage an int representing the percentage of the progress bar
     */
    private void fillProgressBar(int percentage) {
        final LayerDrawable progressDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable progressPortion = progressDrawable.getDrawable(1);                             //Get the top layer

        if (percentage <= 33) {
            progressBarText.setTextColor(progressView.getContext().getResources().getColor(R.color.progress_indicator_low));
            progressPortion.setTint(progressView.getContext().getResources().getColor(R.color.progress_indicator_low));
        }
        else if (percentage <= 66) {
            progressBarText.setTextColor(progressView.getContext().getResources().getColor(R.color.progress_indicator_mid));
            progressPortion.setTint(progressView.getContext().getResources().getColor(R.color.progress_indicator_mid));
        }
        else {
            progressBarText.setTextColor(progressView.getContext().getResources().getColor(R.color.progress_indicator_high));
            progressPortion.setTint(progressView.getContext().getResources().getColor(R.color.progress_indicator_high));
        }
        progressBar.setProgress(percentage);
    }

    /**
     * Determines what portion of the bar is filled, and what color it is to be set to.
     */
    private void getProgressOnBar(Habit habit) {
        // the body is in callEvents for firebase asynchronous access
        if (username != ""){
            //fire.getOthersEvent(username, events, habit);
        }else {
            fire.getEventList(events, habit);
        }

    }


    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {

    }

    @Override
    public void checkUser(boolean[] has) {

    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {
        int progressCap = events.size();
        float totalProgress = 0;

        if (progressCap == 0) {
            progressBar.setVisibility(View.INVISIBLE);//If there are no respective events, progress bar will not show up.
            progressBarText.setVisibility(View.INVISIBLE);
        }
        else {
            progressBar.setMax(progressCap);
        }

        //Gets the total progress of the event
        for (int i = 0; i < progressCap; i++) {
            int status = events.get(i).getStatus();  //Get the status code of the habit event

            switch (status) {
                case 0: totalProgress += 0;     //Event is incomplete
                    break;
                case 1: totalProgress += 1;     //Event is completed
                    break;
                case 2: totalProgress += 0.5;   //Event is in progress
                    break;
            }
        }
        float temp = totalProgress/progressCap * 100;
        Integer temp2 = Math.round(temp);
        percentage = temp2;  //Gets a rounded percentage out of 100
        progressBarText.setText(percentage.toString());

        fillProgressBar(percentage);
        progressBar.setProgress(percentage);
    }
}
