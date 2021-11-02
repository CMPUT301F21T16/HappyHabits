package com.example.happyhabitapp;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    private List<Habit> habitList;
    private ItemTouchHelper touchHelper;
    private HabitListener habitListener;

    public HabitsAdapter(List<Habit> habitList, HabitListener habitListener) {
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
        habitList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper helper) {
        this.touchHelper = helper;
    }

    public List<Habit> getHabitList(){
        return habitList;
    }
}
