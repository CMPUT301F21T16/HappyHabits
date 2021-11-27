package com.example.happyhabitapp;

import android.content.ClipData;
import android.media.Image;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Represents the view model for each {@link Habit}.
 * Used with {@link HabitsAdapter}.
 * @author Jonathan, Anthony
 */

public class HabitViewHolder extends RecyclerView.ViewHolder implements
    View.OnTouchListener, GestureDetector.OnGestureListener
{

    GestureDetector habitGestureDetector;

    private TextView titleTextView;
    private TextView reasonTextView;
    private TextView frequencyTextView;
    private ImageButton dragHandle;

    private ItemTouchHelper touchHelper;
    private HabitListener viewListener;

    /**
     * Gets all relevant data fields and initializes a {@link GestureDetector} to set to the View.
     * @param habitView a {@link View}
     * @param helper an {@link ItemTouchHelper}
     * @param habitListener an instance of {@link HabitListener}
     */
    public HabitViewHolder(View habitView, ItemTouchHelper helper, HabitListener habitListener) {
        super(habitView);

        titleTextView = (TextView) habitView.findViewById(R.id.habit_title);
        reasonTextView = (TextView) habitView.findViewById(R.id.reason_text);
        frequencyTextView = (TextView) habitView.findViewById(R.id.selected_dates);

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

}
