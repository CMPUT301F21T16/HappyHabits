package com.example.happyhabitapp;

import androidx.annotation.NonNull;

/**
 * For use with the HabitListRecyclerAdapter
 * Represents and instance of a habit
 */
public class HabitViewModel {
    private String title;
    private String reason;
    private int[] frequency;

    public HabitViewModel(@NonNull String title, @NonNull String reason, @NonNull int[] frequency){
        this.title = title;
        this.reason = reason;
        this.frequency = frequency;   //Just display integers for now...
    }

    public int[] getFrequency() {
        return frequency;
    }

    public void setFrequency(int[] frequency) {
        this.frequency = frequency;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
