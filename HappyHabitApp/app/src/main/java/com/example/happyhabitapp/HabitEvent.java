package com.example.happyhabitapp;

import java.util.Calendar;
import java.util.Date;


public class HabitEvent {

    //Define constants for status
    private int STATUS_INCOMPLETE = 0;
    private int STATUS_COMPLETE = 1;
    private int STATUS_IN_PROGRESS = 2;

    private Habit habit;
    private Calendar event_date;
    private String pic_path;
    private String location;
    private String description;
    private String title;
    private int status;

    /* for fire base */
    public HabitEvent() {
    }

    /* counstructors */
    public HabitEvent(Habit habit, Calendar event_date, String title, int statusCode) {
        this.habit = habit;
        this.event_date = event_date;
        this.title = title;
        this.status = statusCode;
    }

    public HabitEvent(Habit habit, Calendar event_date, int statusCode, String pic_path, String location, String description) {
        this.habit = habit;
        this.event_date = event_date;
        this.pic_path = pic_path;
        this.location = location;
        this.description = description;
        this.status = statusCode;
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

    public void setStatus(int statusCode){
        this.status = statusCode;
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

    public int getStatus() {
        return status;
    }

}
