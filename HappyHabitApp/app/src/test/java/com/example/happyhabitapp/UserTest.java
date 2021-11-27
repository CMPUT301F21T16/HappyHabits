package com.example.happyhabitapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests User behaviour excluding Firebase related methods.
 */
public class UserTest {

    private User sampleUser;

    @Before
    public void makeUser(){
        ArrayList<Habit> habitList = new ArrayList<Habit>();
        ArrayList<User> followList = new ArrayList<User>();
        sampleUser = new User("Todd", "toddsPic", habitList, followList, null);
    }

    //Passed
    /**
     * Checks if both adding and removing habits from the list is functional
     */
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
    /**
     * Checks if the username of the User is correctly assigned.
     */
    @Test
    public void testName() {
        Assert.assertEquals("Todd", sampleUser.getUsername());
    }

    //Passed
    /**
     * Checks if a habit is edited.
     */
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

    /**
     * Checks if both adding and removing followers from the list is functional
     */
    @Test
    public void followListAddRemove(){
        int[] weekFreq = {1,1,1,1,0,0,1};

        User sampleFollower = new User("Sarah", "somePicture", null, null, null);

        sampleUser.follow(sampleFollower);
        Assert.assertEquals(1, sampleUser.getFollowList().size());

        sampleUser.unfollow(sampleFollower);
        Assert.assertEquals(0, sampleUser.getFollowList().size());
    }

    /**
     * Checks if User is correctly returned on username input
     */
    @Test
    public void isUserMatchUsername() {
        User userRetrieved;

        //Name is wrong
        userRetrieved = sampleUser.getUser("Sarah");
        Assert.assertNull(userRetrieved);

        //Whitespace is inconsistent
        userRetrieved = sampleUser.getUser(" Todd");
        Assert.assertNull(userRetrieved);

        //Casing is inconsistent
        userRetrieved = sampleUser.getUser("todd");
        Assert.assertNull(userRetrieved);

        //Username matches completely.
        userRetrieved = sampleUser.getUser("Todd");
        Assert.assertNotNull(userRetrieved);
    }
}
