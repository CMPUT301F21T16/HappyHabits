package com.example.happyhabitapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitsAdapter extends RecyclerView.Adapter {

    private List<Habit> habitList;

    public HabitsAdapter(List<Habit> habitList) {
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist_content, parent, false);
        return new HabitViewHolder(view);
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
}
