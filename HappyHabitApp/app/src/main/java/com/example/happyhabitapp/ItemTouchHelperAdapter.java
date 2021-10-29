package com.example.happyhabitapp;

/**
 * Contains the interface required to implement swiping and re-ordering in the habit activity.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}
