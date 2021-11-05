package com.example.happyhabitapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class AddEditTest {
    @Rule
    public ActivityScenarioRule<HabitActivity> activityScenarioRule
            = new ActivityScenarioRule<>(HabitActivity.class);

    @Test
    public void testAdd(){
        onView(withId(R.id.add_habit_btn)).perform(click()); //click add button

        onView(withId(R.id.habit_title_editText)) // put "Title" in habit title Edit text box
                .perform(typeText("Title"), closeSoftKeyboard());

        onView(withId(R.id.habit_reason_editText)) // put "Reason" in habit reason Edit text box
                .perform(typeText("Reason"), closeSoftKeyboard());

        /**
         * find object with "W" and click it to select Wednesday from day selector
         */
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject chooseDays = device.findObject(new UiSelector().text("W"));
        if (chooseDays.exists()){
            try{
                chooseDays.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }
        /**
         * find Add Habit button and click it to add habit to list
         */
        UiObject addHabit = device.findObject(new UiSelector().text("ADD HABIT"));
        if (addHabit.exists()){
            try{
                addHabit.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }

    }
    @Test
    public void testEdit(){
        onView(withId(R.id.add_habit_btn)).perform(click());

        onView(withId(R.id.habit_title_editText))
                .perform(typeText("Title"), closeSoftKeyboard());

        onView(withId(R.id.habit_reason_editText))
                .perform(typeText("Reason"), closeSoftKeyboard());

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject chooseDays = device.findObject(new UiSelector().text("W"));
        if (chooseDays.exists()){
            try{
                chooseDays.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }

        UiObject addHabit = device.findObject(new UiSelector().text("ADD HABIT"));
        if (addHabit.exists()){
            try{
                addHabit.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }

    }

}
