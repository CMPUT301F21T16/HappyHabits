package com.example.happyhabitapp;

import java.util.ArrayList;

public interface FirestoreCallback {
    void callHabitList(ArrayList<Habit> habits);
    void callRequestList(ArrayList<User> requesters);
    boolean checkUser(boolean has);
}
