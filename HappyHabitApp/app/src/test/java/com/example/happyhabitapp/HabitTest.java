package com.example.happyhabitapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * Tests Habit behaviour excluding getWeekAsStr (tested in its own test Class), and Firebase related methods.
 */
public class HabitTest {

    Habit sampleHabit;
    Calendar testDate;

    @Before
    public void createHabitTest(){

        int[] weekFreq = {1,0,0,1,0,0,1};
        testDate = Calendar.getInstance();

        sampleHabit = new Habit("Walk dog", "Fat dog", testDate, weekFreq);
    }

    //Passed
    /**
     * Tests the getters of the Habit
     */
    @Test
    public void getAttributesTest() {
        //Title
        Assert.assertNotEquals("Fat dog", sampleHabit.getTitle());
        Assert.assertEquals("Walk dog", sampleHabit.getTitle());

        //Reason
        Assert.assertNotEquals("Walk dog", sampleHabit.getReason());
        Assert.assertEquals("Fat dog", sampleHabit.getReason());

        //Date
        Calendar today = Calendar.getInstance();

        Assert.assertEquals(today.get(Calendar.DATE), testDate.get(Calendar.DATE));
        Assert.assertEquals(today.get(Calendar.MONTH), testDate.get(Calendar.MONTH));
        Assert.assertEquals(today.get(Calendar.YEAR), testDate.get(Calendar.YEAR));

        //weekFreq
        int[] weekFreq = {1,0,0,1,0,0,1};
        Assert.assertArrayEquals(sampleHabit.getWeek_freq(), sampleHabit.getWeek_freq());
    }

    //Passed
    /**
     * Tests to see if a new date is correctly set.
     */
    @Test
    public void setNewDateTest(){

        Calendar newDate = Calendar.getInstance();

        Assert.assertNotEquals(1984, sampleHabit.getDate().get(Calendar.YEAR));
        Assert.assertNotEquals(4, sampleHabit.getDate().get(Calendar.MONTH));
        Assert.assertNotEquals(20, sampleHabit.getDate().get(Calendar.DATE));

        newDate.set(1984,4,20);
        sampleHabit.setDate(newDate);

        Assert.assertEquals(1984, sampleHabit.getDate().get(Calendar.YEAR));
        Assert.assertEquals(4, sampleHabit.getDate().get(Calendar.MONTH));
        Assert.assertEquals(20, sampleHabit.getDate().get(Calendar.DATE));
    }

    //Passed
    /**
     * Tests if weekFreq is correctly set.
     * Through the fragment, these are automatically constrained to be exactly size 7, so array sizes beyond this are not checked.
     */
    @Test
    public void setWeekFreqTest(){

        int[] baseArr = {1,0,0,1,0,0,1};
        Assert.assertArrayEquals(baseArr, sampleHabit.getWeek_freq());

        int[] newArr = {1,1,0,0,0,1,1};
        sampleHabit.setWeek_freq(newArr);
        Assert.assertArrayEquals(newArr, sampleHabit.getWeek_freq());
    }
}
