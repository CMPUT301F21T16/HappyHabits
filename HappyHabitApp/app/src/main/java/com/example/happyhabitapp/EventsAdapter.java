package com.example.happyhabitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Renders a RecyclerView of {@link EventViewHolder} instances with data passed from {@link Habit} events list.
 * @author Jonathan
 */
public class EventsAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    private List<HabitEvent> habitEventList;
    private ItemTouchHelper touchHelper;
    private HabitListener habitListener;

    public EventsAdapter(List<HabitEvent> habitEventList, HabitListener habitListener) {
        this.habitEventList = habitEventList;
        this.habitListener = habitListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_event_content, parent, false);
        return new EventViewHolder(view, touchHelper, habitListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Takes each element in the list and binds it to a viewHolder
        ((EventViewHolder) holder).attachData(habitEventList.get(position));
    }

    @Override
    public int getItemCount() {
        return habitEventList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        HabitEvent fromEvent = habitEventList.get(fromPosition);
        habitEventList.remove(fromEvent);
        habitEventList.add(toPosition, fromEvent);   //Removed habit is re-added at the new position.
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwipe(int position) {
        habitEventList.remove(position);
        notifyItemRemoved(position);
        //Delete from firebase
    }

    public void setTouchHelper(ItemTouchHelper helper) {
        this.touchHelper = helper;
    }
}

