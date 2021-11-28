package com.example.happyhabitapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HabitStringTest {

    //Making sure the getWeekAsStr() method works as intended

    Habit sampleHabit = new Habit("A", "B", null, null);

    //Make sure comma doesn't show up before the first entry (i.e ,Monday, Tuesday)
    @Test
    public void TestMonTues(){
        int[] montues = {0,1,1,0,0,0,0};

        sampleHabit.setWeek_freq(montues);
        Assert.assertEquals("Monday, Tuesday", sampleHabit.getWeekAsStr());
    }

    //If no days, then nothing should show up (including commas)
    @Test
    public void TestNoDays(){
        int[] nodays =  {0,0,0,0,0,0,0};
        sampleHabit.setWeek_freq(nodays);
        Assert.assertEquals("", sampleHabit.getWeekAsStr());
    }

    //If all days, make sure there's no terminating comma (i.e Monday, ..., Friday, Saturday,)
    @Test
    public void TestAllDays(){
        int[] alldays = {1,1,1,1,1,1,1};
        sampleHabit.setWeek_freq(alldays);
        Assert.assertEquals("Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday", sampleHabit.getWeekAsStr());
    }




}
