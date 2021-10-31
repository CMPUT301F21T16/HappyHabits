package com.example.happyhabitapp;

import android.content.ClipData;
import android.media.Image;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class HabitViewHolder extends RecyclerView.ViewHolder implements
    View.OnTouchListener, GestureDetector.OnGestureListener
{

    GestureDetector habitGestureDetector;

    private TextView titleTextView;
    private TextView reasonTextView;
    private TextView frequencyTextView;
    private ImageButton dragHandle;

    private ItemTouchHelper touchHelper;

    public HabitViewHolder(View habitView, ItemTouchHelper helper) {
        super(habitView);

        titleTextView = (TextView) habitView.findViewById(R.id.habit_title);
        reasonTextView = (TextView) habitView.findViewById(R.id.reason_text);
        frequencyTextView = (TextView) habitView.findViewById(R.id.selected_dates);
        dragHandle = (ImageButton) habitView.findViewById(R.id.drag_handle);

        habitGestureDetector = new GestureDetector(habitView.getContext(), this);
        touchHelper = helper;

        habitView.setOnTouchListener(this);

    }

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

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        //TODO: OnHabitListener Implementation
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
