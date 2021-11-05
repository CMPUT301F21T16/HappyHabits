package com.example.happyhabitapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class AddEditTest {
    @Rule
    public ActivityScenarioRule<HabitActivity> activityScenarioRule
            = new ActivityScenarioRule<>(HabitActivity.class);

    @Test
    public void testAdd(){
        onView(withId(R.id.add_habit_btn))
    }

}
