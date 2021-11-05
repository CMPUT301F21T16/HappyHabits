package com.example.happyhabitapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.robotium.solo.Solo ;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddEditTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<HabitActivity> rule
            = new ActivityTestRule<>(HabitActivity.class);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

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

        /**
         * find add habit button and click it
         */
        UiObject addHabit = device.findObject(new UiSelector().text("ADD HABIT"));
        if (addHabit.exists()){
            try{
                addHabit.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }

        // Reads on the screen for Title, Reason, and Wednesday
        assertTrue(solo.waitForText("Title",1,10)
                && solo.waitForText("Reason",1,10)
                && solo.waitForText("Wednesday",1,10));
    }

    @Test
    public void testEdit() {
        /**
         * click habit
         */
        onView(ViewMatchers.withId(R.id.habit_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.habit_title_editText))
                .perform(clearText(),typeText("newTitle"), closeSoftKeyboard());

        onView(withId(R.id.habit_reason_editText))
                .perform(clearText(),typeText("newReason"), closeSoftKeyboard());
        /**
         * deselect wednesday
         */
        chooseDays = device.findObject(new UiSelector().text("W"));
        if (chooseDays.exists()){
            try{
                chooseDays.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }
        /**
         * find F button from day selector and click it
         */
        chooseDays = device.findObject(new UiSelector().text("F"));
        if (chooseDays.exists()){
            try{
                chooseDays.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }
        /**
         * find done editing button and click it
         */
        UiObject editHabit = device.findObject(new UiSelector().text("DONE EDITING"));
        if (editHabit.exists()){
            try{
                editHabit.click();
            } catch (UiObjectNotFoundException e){
                e.printStackTrace();
            }
        }

        assertTrue(solo.waitForText("newTitle",1,10)
                && solo.waitForText("newReason",1,10)
                && solo.waitForText("Friday",1,10));

    }

}
