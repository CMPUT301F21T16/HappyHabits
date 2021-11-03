package com.example.happyhabitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Habit_Events extends RecyclerView.Adapter<Habit_Events.ViewHolder> implements Filterable {
    ArrayList<Habit> Habits;
    ArrayList<Habit> filtersHabits ;
    Context context ;
    public Habit_Events(Context context, ArrayList<Habit> Habits){
        this.Habits = Habits;
        this.filtersHabits = Habits;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.activity_dash_board,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull Habit_Events.ViewHolder holder, int position) {
        Habit Habit = Habits.get(position);

        holder.Habit_Name.setText(Habit.getTitle());
        holder.Habit_reason.setText(Habit.getReason());
        holder.Habit_date.setText((CharSequence) Habit.getDate());
        holder.Habit_week_day.setText(Habit.getWeekAsStr());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        /*
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.error_img)
                .fallback(R.drawable.error_img)
                .error(R.drawable.error_img);
        Glide.with(context).load(Habit.image).apply(options).into(holder.ivLogo);
        */

    }



    @Override
    public int getItemCount() {
        return filtersHabits.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Habit> tempList = new ArrayList<>();
                String c = constraint.toString();

                if (c.equals("ALL")) {
                    tempList = Habits;
                } else {
                    for (Habit Habit : Habits) {
                        if (Habit.getWeekAsStr().toUpperCase() .equals(c) ) {
                            tempList.add(Habit);
                        }
                    }
                }

                results.values = tempList;
                results.count = tempList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtersHabits = (ArrayList<Habit>) results.values;
                notifyDataSetChanged();

            }
        };

        return filter;
    }

    public  Habit getHabit(int position) {

        return  filtersHabits.get(position);

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //ImageView Habit_Logo;
        TextView Habit_Name;
        TextView Habit_reason;
        TextView Habit_date;
        TextView Habit_week_day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Habit_Name = itemView.findViewById(R.id.habit_name);
            Habit_reason = itemView.findViewById(R.id.habit_reason);
            Habit_date = itemView.findViewById(R.id.habit_date);
            Habit_week_day = itemView.findViewById(R.id.habit_week_day);

        }
    }
    private OnItemClickListener onItemClickListener;
    public  interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
