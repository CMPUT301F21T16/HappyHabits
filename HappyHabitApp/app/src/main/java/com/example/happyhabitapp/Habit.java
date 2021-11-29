package com.example.happyhabitapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class represents a habit of a {@link User}
 * @author Anuj, Armaan
 */
public class Habit implements Serializable {
    private String title;
    private String reason;
    private Calendar date;
    private int[] week_freq;
    private boolean publicHabit;
    private ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();


    public Habit() {
    }


    public Habit(String title, String reason, Calendar date, int[] week_freq, boolean publicHabit) {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.week_freq = week_freq;
        this.publicHabit = publicHabit;
//        HabitEvent dummy = new HabitEvent();
//        this.events.add(dummy);
    }

    public Habit(String title, String reason, Calendar date, int[] week_freq, boolean publicHabit, ArrayList<HabitEvent> events) {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.week_freq = week_freq;
        this.publicHabit = publicHabit;
        this.events = events;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public ArrayList<HabitEvent> getEvents() {
        return events;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int[] getWeek_freq() {
        return week_freq;
    }

    public void setWeek_freq(int[] week_freq) {
        this.week_freq = week_freq;
    }

    public boolean getPublicHabit() {
        return publicHabit;
    }

    public void setPublicHabit(boolean publicHabit) {
        this.publicHabit = publicHabit;
    }
    public void setEvents(ArrayList<HabitEvent> events) {
        this.events = events;
    }

    /**
     * Reformats days selected in human readable format
     * @return String
     */
    public String getWeekAsStr(){
        String weekDaysSelected = "";

        String[] week = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] weekAbbv = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};   //Abbv days for display purposes

        for (int k = 0; k < week.length; k++) {
            if (week_freq[k] == 1) {
                if(weekDaysSelected.compareTo("") != 0) {
                    weekDaysSelected = weekDaysSelected.concat(", ");
                }
                weekDaysSelected = weekDaysSelected.concat(weekAbbv[k]);
                if (k != week.length - 1) {

                }
            }
        }
        return weekDaysSelected;
    }
}
