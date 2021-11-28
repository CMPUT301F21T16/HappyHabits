/**
 * the interface is for callBack functions that will cooperate with FireBase methods
 * author: Frank Li
 */

package com.example.happyhabitapp;

import java.util.ArrayList;

public interface FirestoreCallback {
    void callHabitList(ArrayList<Habit> habits);
    void callUserList(ArrayList<User> requesters);
    void checkUser(boolean[] has);
    void callEventList(ArrayList<HabitEvent> events);
}
