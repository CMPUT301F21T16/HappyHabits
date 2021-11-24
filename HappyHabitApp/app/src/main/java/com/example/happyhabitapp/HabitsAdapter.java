package com.example.happyhabitapp;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders a RecyclerView of {@link HabitViewHolder} instances with data passed from {@link User}'s habit list.
 * @author Jonathan, Anthony
 */
public class HabitsAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    private ArrayList<Habit> habitList;
    private ItemTouchHelper touchHelper;
    private HabitListener habitListener;

    private FireBase fire = new FireBase();

    public HabitsAdapter(ArrayList<Habit> habitList, HabitListener habitListener) {
        this.habitList = habitList;
        this.habitListener = habitListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist_content, parent, false);
        return new HabitViewHolder(view, touchHelper, habitListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Takes each element in the list and binds it to a viewHolder
        ((HabitViewHolder) holder).attachData(habitList.get(position));
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Habit fromHabit = habitList.get(fromPosition);
        habitList.remove(fromHabit);
        habitList.add(toPosition, fromHabit);   //Removed habit is re-added at the new position.
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwipe(int position) {
        fire.delHabit(habitList.get(position));
        habitList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, habitList.size());
//        notifyItemRemoved(position);
        //Delete from firebase
    }

    public void setTouchHelper(ItemTouchHelper helper) {
        this.touchHelper = helper;
    }

    public List<Habit> getHabitList(){
        return habitList;
    }
}
