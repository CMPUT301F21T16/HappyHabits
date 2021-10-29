package com.example.happyhabitapp;

import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class habitTouchHelper extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter habitTouchAdapter;

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
                ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white);
    }

    //On item drag, highlight it with the theme blue
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            viewHolder.itemView.setBackgroundColor(
                    ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white);
        }


    }

    //Required to implement
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }


}
