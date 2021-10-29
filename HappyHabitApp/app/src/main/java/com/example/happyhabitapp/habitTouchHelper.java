package com.example.happyhabitapp;

import android.annotation.SuppressLint;
import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

//TODO: Make drag only occur on handle button if possible?
//TODO: implement this within our Recycle Adapter.

//From: CodingWithMitch (https://www.youtube.com/watch?v=uvzP8KTz4Fg)

public class habitTouchHelper extends ItemTouchHelper.Callback {

    final private ItemTouchHelperAdapter habitTouchAdapter;

    public habitTouchHelper(ItemTouchHelperAdapter habitTouchAdapter) {
        this.habitTouchAdapter = habitTouchAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        //We want to handle this within the view where we can use the drag handle
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //On item release, redraw item un-highlighted
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.theme_bg)
        );
    }

    //On item drag, highlight it with the theme blue
    @SuppressLint("ResourceAsColor")
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder.itemView.setBackgroundColor(R.color.highlighted_entry);
        }
    }

    //Required to implement
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags  = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //For re-arrangement of list elements
        int swipeFlags = ItemTouchHelper.RIGHT;                     //For deletion of list elements
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromPosition, @NonNull RecyclerView.ViewHolder toPosition) {
        habitTouchAdapter.onItemMove(fromPosition.getAdapterPosition(), toPosition.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        habitTouchAdapter.onItemSwiped(viewHolder.getAdapterPosition());
    }
}
