/**
 * This class is able to upload desired data to the firebase, and get the current user uid
 * Author: Katia Zhang, Frank Li
 */



package com.example.happyhabitapp;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class FireBase {

    private final String TAG = "FireBase";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<User> followerLst = new ArrayList<>();

    private String current_uid;

    private CollectionReference Users = db.collection("Users");
    private DocumentReference User = Users.document(getUserName());
    private CollectionReference HabitList = User.collection("HabitList");
    private CollectionReference Followers = User.collection("Followers");
    private CollectionReference Followees = User.collection("Followees");


    /* Constructors */
    public FireBase() {}

    /* Functions to upload to database */

    /**
     * this function upload user's information to firebase
     * @param user (User class object)
     */

    public void setUser(User user) {

        User
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: user added to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add user", e);
                    }
                });

    }

    /**
     * this function upload habits to the firebase
     * @param habit (Habit class object)
     */

    public void setHabit(Habit habit) {
        List<Integer> freq = new ArrayList<Integer>();
        for (int i = 0; i < (habit.getWeek_freq().length); i++){
            freq.add(habit.getWeek_freq()[i]);
        }
        Map<String, Object> map = new HashMap<>();

        map.put("Title", habit.getTitle());
        map.put("Reason", habit.getReason());
        map.put("Days", freq);
        map.put("Dates", habit.getDate());


        HabitList
                .document(habit.getTitle())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added habit to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add habit to fire", e);
                    }
                });

    }

    /**
     * this function upload information about followers to firebase
     * @param followers (User class object)
     */

    public void setFollowers(User followers) {
        Followers
                .document(followers.getUsername())
                .set(followers)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added follower to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add follower", e);
                    }
                });
    }

    /**
     * this function upload information about followees
     * @param followees (User class object)
     */

    public void setFollowees(User followees) {
        Followees
                .document(followees.getUsername())
                .set(followees)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added followees to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add followees", e);
                    }
                });
    }

    /**
     * this function will upload habit event to the firebase
     * @param event (HabitEvent class object)
     */

    public void setHabitEventEvent(HabitEvent event) {



        Map<String, Object> map = new HashMap<>();
        map.put("About", event.getHabit().getTitle());
        map.put("Date", event.getEvent_date());
        map.put("picPath", event.getPic_path());
        map.put("location", event.getLocation());
        map.put("description", event.getDescription());

        HabitList
                .document(event.getHabit().getTitle())
                .collection("Events")
                .document(event.getTitle())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added event to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add event", e);
                    }
                });
    }


    /* getters */

    /**
     * This function will return the current userid
     * @return
     */
    public String getCurrent_uid() {
        current_uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        return current_uid;
    }


    /**
     * this function return the collection reference to collection: Users
     * @return
     */
    public CollectionReference usersRef(){
        return Users;
    }


    /* get information: this feature is not working */

    /**
     * this function return current user's user name as string
     * @return
     */
    public String getUserName(){
        String current_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return current_name;
    }


    /**
     * this function get follower list and store in list
     * @param list
     */
    public void getFollowerLst(ArrayList<User> list){

        Followers
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting followers");
                            User follower = doc.toObject(com.example.happyhabitapp.User.class);
                            Log.d(TAG, "onEvent: " + follower.getUsername());
                            list.add(follower);
                        }
                    }
                });
    }

    /**
     * this function get followee list and store in list
     * @param list
     */
    public void getFolloweeLst(ArrayList<User> list){
        Followees
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting followers");
                            User followee = doc.toObject(com.example.happyhabitapp.User.class);
                            Log.d(TAG, "onEvent: " + followee.getUsername());
                            list.add(followee);
                        }
                    }
                });
    }


    /**
     * this function get habit list and store in list
     * @param list
     */
    public void getHabitLst(ArrayList<Habit> list){
        HabitList
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting followers");
                            Habit habit = doc.toObject(Habit.class);
                            Log.d(TAG, "onEvent: " + habit.getTitle());
                            list.add(habit);
                        }
                    }
                });
    }










}

