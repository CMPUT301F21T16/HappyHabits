package com.example.happyhabitapp;


import android.graphics.Picture;
import android.location.Location;

import java.util.Calendar;
import java.util.Date;

public class Habit_Events extends Habit {


    public Habit_Events(String title, String reason, Calendar date, int[] week_freq) {
        super(title, reason, date, week_freq);
    }



    private Habit habit;

    private Picture habit_pic;
    private Location location;
    private Date habit_event;
    private String description;

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public Picture getHabit_pic() {
        return habit_pic;
    }

    public void setHabit_pic(Picture habit_pic) {
        this.habit_pic = habit_pic;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getHabit_event() {
        return habit_event;
    }

    public void setHabit_event(Date habit_event) {
        this.habit_event = habit_event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
