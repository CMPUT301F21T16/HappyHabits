package com.example.happyhabitapp;

import java.util.Calendar;

public class HabitEvent extends Habit{
    public HabitEvent(String title, String reason, Calendar date, int[] week_freq) {
        super(title, reason, date, week_freq);
    }
}
