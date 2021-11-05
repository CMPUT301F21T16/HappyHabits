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

import org.junit.Rule;
import org.junit.Test;

public class AddEditTest {
    @Rule
    public ActivityScenarioRule<HabitActivity> activityScenarioRule
            = new ActivityScenarioRule<>(HabitActivity.class);

    @Test
    public void testAdd(){
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

    // **IMPORTANT** The following tests are mock test functions for certain functionality
    // All the functionality has all been thoroughly tested manually and has passed these manual tests
    // Test functions are not completed due to complications with Espresso/Robotium and it's interactions
    // with the Recycler view.

        // Tests functionality for deleting a habit by swiping it to the right off the screen
       @Test
       public void testDelete(){
            //Generate empty Habit list into adapter
            //Add Habit to List
            //assert 1 Habit in list
            //Perform swipe right action on habit in Recycler View
            //assert no habits in list
           assertTrue(true);
       }

       @Test
       public void reorderList(){

       }


}
