package com.example.happyhabitapp;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Represents the view model for each {@link Habit}.
 * Used with {@link HabitsAdapter}.
 * @author Jonathan, Anthony
 */

public class HabitViewHolder extends RecyclerView.ViewHolder implements
    View.OnTouchListener, GestureDetector.OnGestureListener, FirestoreCallback
{
    GestureDetector habitGestureDetector;

    private FireBase fire = new FireBase();

    private View view;

    private TextView titleTextView;
    private TextView reasonTextView;
    private TextView frequencyTextView;

    private ItemTouchHelper touchHelper;
    private HabitListener viewListener;

    private ProgressBar progressBar;
    private TextView progressBarText;
    private ArrayList<HabitEvent> event_list = new ArrayList<>();
    private Integer percentage = 0;

    /**
     * Gets all relevant data fields and initializes a {@link GestureDetector} to set to the View.
     * @param habitView a {@link View}
     * @param helper an {@link ItemTouchHelper}
     * @param habitListener an instance of {@link HabitListener}
     */
    public HabitViewHolder(View habitView, ItemTouchHelper helper, HabitListener habitListener) {
        super(habitView);
        view = habitView;
        titleTextView = (TextView) habitView.findViewById(R.id.habit_title);
        reasonTextView = (TextView) habitView.findViewById(R.id.reason_text);
        frequencyTextView = (TextView) habitView.findViewById(R.id.selected_dates);
        fire.setApi(this);
        habitGestureDetector = new GestureDetector(habitView.getContext(), this);
        touchHelper = helper;
        viewListener = habitListener;

        habitView.setOnTouchListener(this);
    }

    /**
     * Sets the data in the view model according to {@link Habit} instance.
     * @param habit
     */
    public void attachData(Habit habit) {
        titleTextView.setText(habit.getTitle());
        reasonTextView.setText(habit.getReason());
        frequencyTextView.setText(habit.getWeekAsStr());
        setProgressOnBar(habit);
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
        fire.getEventList(event_list, habit);
        // the body is in callEvents for firebase asynchronous access
    }


    //All the below methods are required by the interfaces implemented

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        //Does nothing
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        viewListener.onHabitClick(getAdapterPosition());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        touchHelper.startDrag(this);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        habitGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    /* ================================================================= Methods for FirestoreCallback ============================================================ */
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
