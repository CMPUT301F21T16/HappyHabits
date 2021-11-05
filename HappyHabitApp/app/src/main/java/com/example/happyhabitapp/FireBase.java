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

    private static final String TAG = "FireBase"; // for the logd and loge will be deleted after finishing the implementation

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                .update(map)
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

        List<Integer> freq = new ArrayList<Integer>();
        for (int i = 0; i < (event.getWeek_freq().length); i++){
            freq.add(event.getWeek_freq()[i]);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Title", event.getTitle());
        map.put("Reason", event.getReason());
        map.put("Days", freq);
        map.put("Dates", event.getDate());
        map.put("About", event.getAbout());
        HabitList
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

    /**
     * this function return document reference to document: User (each user's name)
     * @return
     */
    public DocumentReference userRef(){
        return User;
    }

    /**
     * this function return collection reference to collection: HabitList
     * @return
     */
    public CollectionReference hbLstRef(){
        return HabitList;
    }

    /**
     * this function return collection reference to collection: Followers
     * @return
     */
    public CollectionReference getFollowers() {
        return Followers;
    }

    /**
     * this function return collection reference to collection: Followees
     * @return
     */
    public CollectionReference getFollowees() {
        return Followees;
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



    //public void readFollowerLst(MyCallback myCallback){
        /*
        Followers
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        followerLst.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting followers");
                            User follower = doc.toObject(com.example.happyhabitapp.User.class);
                            Log.d(TAG, "onEvent: " + follower.getUsername());
                            followerLst.add(follower);
                        }
                    }
                });
         */



    /*
        Followers
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                Log.d(TAG, "onEvent: getting followers");
                                User follower = documentSnapshot.toObject(com.example.happyhabitapp.User.class);
                                Log.d(TAG, "onEvent: " + follower.getUsername());
                                followerLst.add(follower);
                                myCallback.onCallback(followerLst);
                            }
                        }
                        else {
                            Log.e(TAG, "onComplete: can't get data", task.getException());
                        }
                    }
                });


    }

     */





}

