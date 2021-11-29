package com.example.happyhabitapp;


import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;


/**
 * This class represents the view model for each event of a habit
 */

public class EventViewHolder extends RecyclerView.ViewHolder implements
        View.OnTouchListener, GestureDetector.OnGestureListener {

        GestureDetector eventGestureDetector;

        TextView eventTitleTextView;
        TextView commentTextView;
        ImageView thumbnailImageView;
        ImageView eventStatusImageView;

        private ItemTouchHelper touchHelper;
        private HabitListener viewListener;


    public EventViewHolder(@NonNull View eventView, ItemTouchHelper helper, HabitListener eventListener) {
        super(eventView);

        eventTitleTextView = eventView.findViewById(R.id.habit_event_title);
        commentTextView = eventView.findViewById(R.id.event_comment);
        thumbnailImageView = eventView.findViewById(R.id.event_thumbnail);
        eventStatusImageView = eventView.findViewById(R.id.event_status);

        eventGestureDetector = new GestureDetector(eventView.getContext(), this);
        touchHelper = helper;
        viewListener = eventListener;

        eventView.setOnTouchListener(this);
    }

    public void attachData(HabitEvent event){
        eventTitleTextView.setText(event.getTitle());
        commentTextView.setText(event.getDescription());
        //TODO: Replace this with the actual image
        String encode = event.getImage();
        Bitmap image = null;

        try {
            image = decodeFromFirebaseBase64(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        thumbnailImageView.setImageBitmap(image);


        //Switch which icon is shown in response to the retrieved status
        switch(event.getStatus()) {
            case 0:
                setImageIcon(R.drawable.ic_not_completed_icon, R.color.incomplete_red);
                break;
            case 1:
                setImageIcon(R.drawable.ic_completed_icon, R.color.complete_green);
                break;
            case 2:
                setImageIcon(R.drawable.ic_in_progress_icon, R.color.in_progress_yellow);
                break;
        }
    }

    /**
     * Helper function to set the icon and color for the event status
     * @param iconId the resource id of the drawable vector
     * @param tint the resource id of the tint of vector
     */
    private void setImageIcon(int iconId, int tint) {
        eventStatusImageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(eventStatusImageView.getContext(), tint)));
        eventStatusImageView.setImageResource(iconId);
    }

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
        eventGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
