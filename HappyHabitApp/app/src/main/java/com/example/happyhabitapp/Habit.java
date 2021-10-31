package com.example.happyhabitapp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Habit implements Serializable {
    private String title;
    private String reason;
    private Calendar date;
    private int[] week_freq;

    public Habit(String title, String reason, Calendar date, int[] week_freq) {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.week_freq = week_freq;
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

    //Returns days selected in human readable format
    public String getWeekAsStr(){
        String weekDaysSelected = "";

        String[] week = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        for (int k = 0; k < week.length - 1; k++) {
            if (week_freq[k] == 1) {
                weekDaysSelected = weekDaysSelected.concat(week[k]);
                if (k != week.length - 2) {
                    weekDaysSelected = weekDaysSelected.concat(", ");
                }
            }
        }
        return weekDaysSelected;
    }

    public void setWeek_freq(int[] week_freq) {
        this.week_freq = week_freq;
    }
}