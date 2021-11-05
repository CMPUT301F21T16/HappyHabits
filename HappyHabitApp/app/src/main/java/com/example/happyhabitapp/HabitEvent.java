package com.example.happyhabitapp;

import java.util.Calendar;
import java.util.Date;

public class HabitEvent extends Habit{
    private String about;

    public HabitEvent(String title, String reason, Calendar date, int[] week_freq, String about) {
        super(title, reason, date, week_freq,true);
        this.about = about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }
}
