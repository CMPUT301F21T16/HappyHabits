package com.example.happyhabitapp;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class HabitTouchHelper extends ItemTouchHelper.Callback{

    private final ItemTouchHelperAdapter touchHelperAdapter;

    public HabitTouchHelper(ItemTouchHelperAdapter touchHelperAdapter) {
        this.touchHelperAdapter = touchHelperAdapter;
    }

    //We want to deal with this within the viewHolder itself.
    @Override
    public boolean isLongPressDragEnabled(){
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //Unhighlight an element when we release it.
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.theme_bg)
        );
    }

    //Highlight an element when we select it.
    @SuppressLint("ResourceAsColor")
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder.itemView.setBackgroundColor(R.color.highlighted_entry);
        }
    }

    //Specifies which action is triggered according to direction
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        touchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        touchHelperAdapter.onItemSwipe(viewHolder.getAdapterPosition());
    }
}
