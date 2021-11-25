package com.example.happyhabitapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;


public class HabitsListTest {

    User sampleUser;

    @Before
    public void createUser() {

        ArrayList<Habit> testList = new ArrayList<Habit>();
        ArrayList<User> blankFollowerList = new ArrayList<User>();

        Calendar rightNow = Calendar.getInstance();
        int[] selectedDates = {1,0,1,0,1,1,1};
        Habit habit1 = new Habit("Get Food", "I am hungry", rightNow, selectedDates);
        Habit habit2 = new Habit("Feed dog", "They are hungry", rightNow, selectedDates);
        Habit habit3 = new Habit("Run for president", "MAGA", rightNow, selectedDates);

        testList.add(habit1);
        testList.add(habit2);
        testList.add(habit3);

        sampleUser = new User("Jimmy", "some path", testList, blankFollowerList, null);
    }

    @Test
    public void isAdapted() {
        HabitsAdapter testAdapter = new HabitsAdapter(sampleUser.getHabitList(), null);
        Assert.assertEquals(3, testAdapter.getItemCount());

    }








}
