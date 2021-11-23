package com.example.happyhabitapp;

import java.util.Calendar;
import java.util.Date;

public class HabitEvent {
    private Habit habit;
    private Calendar event_date;
    private String pic_path;
    private String location;
    private String description;
    private String title;

    /* for fire base */
    public HabitEvent() {
    }

    /* counstructors */
    public HabitEvent(Habit habit, Calendar event_date, String title) {
        this.habit = habit;
        this.event_date = event_date;
        this.title = title;
    }


    public HabitEvent(Habit habit, Calendar event_date, String title, String description) {
        this.habit = habit;
        this.event_date = event_date;
        this.title = title;
        this.description = description;
    }

    public HabitEvent(Habit habit, Calendar event_date, String pic_path, String location, String description) {
        this.habit = habit;
        this.event_date = event_date;
        this.pic_path = pic_path;
        this.location = location;
        this.description = description;
    }

    /* setters */

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEvent_date(Calendar event_date) {
        this.event_date = event_date;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* getters */
    public Habit getHabit() {
        return habit;
    }

    public Calendar getEvent_date() {
        return event_date;
    }

    public String getPic_path() {
        return pic_path;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
