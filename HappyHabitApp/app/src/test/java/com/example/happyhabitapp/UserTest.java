package com.example.happyhabitapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UserTest {

    private User sampleUser;

    @Before
    public void makeUser(){
        ArrayList<Habit> habitList = new ArrayList<Habit>();
        ArrayList<User> followList = new ArrayList<User>();
        sampleUser = new User("Todd", "toddsPic", habitList, followList);
    }

    //Passed
    @Test
    public void habitListAddRemove() {
        int[] weekFreq = {1,1,1,1,1,1,1};
        Habit newHabit = new Habit("eat food", "I am hungry", null, weekFreq);
        sampleUser.addHabit(newHabit);
        Assert.assertEquals(1, sampleUser.getHabitList().size());

        sampleUser.deleteHabit(newHabit);
        Assert.assertEquals(0, sampleUser.getHabitList().size());
    }

    //Passed
    @Test
    public void testName() {
        Assert.assertEquals("Todd", sampleUser.getUsername());
    }

    //Passed
    @Test
    public void editHabit(){
        int[] weekFreq = {1,1,1,1,1,1,1};
        Habit newHabit = new Habit("eat food", "I am hungry", null, weekFreq);
        Habit oldHabit = new Habit("run away from home", "AAAAAA", null, weekFreq);

        sampleUser.addHabit(oldHabit);

        Assert.assertEquals(sampleUser.getHabitList().get(0), oldHabit); //First item is old Habit

        sampleUser.editHabit(oldHabit, newHabit);

        Assert.assertEquals(sampleUser.getHabitList().get(0), newHabit); //First item is new Habit
    }


}
