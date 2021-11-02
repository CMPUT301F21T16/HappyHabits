package com.example.happyhabitapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**Implements the User Object.
 *
 * @author Jonathan
 * @version 1.0
 */

public class User {
    //Private Variables

    private String picPath;  //Represents the path of the profilePicture (Not mandatory?)
    private String username;

    private Date dateToday;
    private ArrayList<Habit> habitList;
    private ArrayList<User> followList;

    //Constructors
    /**
     *Constructor for a new user who opts out of setting a profile picture.
     * @param username a String that is unique when compared against the database.
     */
    public User(String username) {
        this.username = username;
        //this.picPath = someDefaultPath; //Where we will put the default profile image
        this.dateToday = getDateToday();
        this.habitList = new ArrayList<Habit>();
    }

    /**
     * Constructor for a new user who sets a profile picture.
     * @param username a String that is unique when compared against the database.
     * @param path a String that specifies location of profile picture.
     */
    public User(String username, String path) {
        this.username = username;
        this.picPath = path;
        this.dateToday = getDateToday();
        this.habitList = new ArrayList<Habit>();
    }

    /**
     * Constructor for a returning user.
     * @param username a String that is unique when compared against the database.
     * @param path a String that specifies location of profile picture.
     * @param habitList an ArrayList of Habit that contains all the user's habits.
     * @param followList an ArrayList of User that contains all the user's friends/followers/followees?
     */
    public User(String username, String path, ArrayList<Habit> habitList, ArrayList<User> followList) {
        this.username = username;
        this.picPath = path;
        this.habitList = habitList;
        this.followList = followList;
        this.dateToday = getDateToday();
    }

    //Getters

    /**
     * Get and return the User instance if usernames match.
     * @param username the String of a unique username.
     * @return User if usernames match; null otherwise.
     */
    public User getUser(String username){
        if (this.username.equals(username)) {
            return this;    //Return object
        }
        return null;    //Not the correct user.
    }

    /**
     * Get and return the username.
     * @return String containing the user's username.
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Get today's date  using Calendar library.
     * Used internally as constructors and updating the date in case of day-rollover.
     * @return Date.
     */
    public Date getDateToday(){
        return Calendar.getInstance().getTime();
    }

    public ArrayList<Habit> getHabitList(){
        return this.habitList;
    }

    public ArrayList<User> getFollowList(){
        return this.followList;
    }

    //Setters

    /**
     * Add habit to the list of habits.
     * @param userHabit a Habit to be appended to the user-kept ArrayList.
     */
    public void addHabit(Habit userHabit) {
        this.habitList.add(userHabit);
    }

   public void deleteHabit(Habit userHabit) {
        this.habitList.remove(userHabit);
   }

    /**
     * Replaces an old habit with a new habit at the same position in the Arraylist.
     * @param oldHabit the old habit to be replaced.
     * @param newHabit the new habit to put in place of the old.
     */

   public void editHabit(Habit oldHabit, Habit newHabit) {
        int position = this.habitList.indexOf(oldHabit);    //Put at the same position in array
        this.habitList.set(position, newHabit);
   }


    /**
     * Add User to the list of followers.
     * @param follower the User to be added to the follow list.
     */
    public void follow(User follower) {
        this.followList.add(follower);
    }

    public void setProfilePic(String path){
        this.picPath = path;
    }

    /**
     * Sets Habit Event
     * @param setHabit the Habit to add an event to.
     */
    public void setHabitEvent(Habit setHabit){
        //Not sure how to implement this, so empty for now
    }
}
