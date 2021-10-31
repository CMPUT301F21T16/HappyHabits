package com.example.happyhabitapp;

/**
 * Adapts User touches to the RecyclerView
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwipe(int position);
}

