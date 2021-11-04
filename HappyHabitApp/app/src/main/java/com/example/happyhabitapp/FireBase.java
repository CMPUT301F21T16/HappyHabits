package com.example.happyhabitapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBase {
    private static final String TAG = "FireBase";
    private User user;
    private Habit habit;
    private User followers;
    private User followees;
    private HabitEvent event;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String current_uid;

    private String user_name;
    CollectionReference Habit = db.collection("Habits");
    CollectionReference Followers = db.collection("Followers");
    CollectionReference Followees = db.collection("Followees");
    CollectionReference User = db.collection("User");
    CollectionReference habitEvent = db.collection("Events");


    /* Constructors */
    public FireBase(User user, Habit habit, User followers, User followees, HabitEvent event) {
        this.user = user;
        this.habit = habit;
        this.followers = followers;
        this.followees = followees;
        this.event = event;
    }

    /* setters */
    public void setUser(User user) {
        Map<String, Object> map = new HashMap<>();

        map.put("Current_uid", user.getCurrent_uid());
        map.put("Name", user.getUsername());
        map.put("Pic_path", user.getPicPath());
        map.put("habits", user.getHabitList());
        map.put("followers", user.getFollowList());

        User
                //.add(user)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Added user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }

    public void setHabit(Habit habit) {
        Map<String, Object> map = new HashMap<>();
        map.put("Current_uid", habit.getCurrent_uid());
        map.put("Title", habit.getTitle());
        map.put("Reason", habit.getReason());
        //map.put("Days", habit.getWeekAsStr());
        map.put("Dates", habit.getDate());


        Habit
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        CollectionReference habitEvent = Habit.document(documentReference.getId()).collection("HabitEvents");

                        Log.d(TAG, "onSuccess: Added habit");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });

    }

    public void setFollowers(User followers) {
        Followers
                .add(followers)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Added followers");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }

    public void setFollowees(User followees) {
        Followees
                .add(followees)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Added followees");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }

    public void setHabitEventEvent(HabitEvent event) {

        Map<String, Object> map = new HashMap<>();
        map.put("Current_uid", event.getCurrent_uid());
        map.put("Title", event.getTitle());
        map.put("Reason", event.getReason());
        //map.put("Days", habit.getWeekAsStr());
        map.put("Dates", event.getDate());
        map.put("About", event.getAbout());
        habitEvent
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: Added event");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
    }


    /* getters */


    public String getCurrent_uid() {
        current_uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        return current_uid;
    }

    public User getUser() {

        db
                .collection("User")
                .whereEqualTo("Current_uid", getCurrent_uid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: We are getting the date");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "onSuccess: " + snapshotList.get(0).getString("Name"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
        return user;
    }

    public String getUserName(){
        //final String[] user_name = new String[1];
        db
                .collection("User")
                .whereEqualTo("Current_uid", getCurrent_uid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: We are getting the date");
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            Log.d(TAG, String.valueOf(doc.getData().get("Name")));
                            user_name = (String) doc.getString("Name");

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
        return user_name;
    }


    public Habit getHabit() {
        db
                .collection("Habits")
                .whereEqualTo("Current_uid", getCurrent_uid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: We are getting the date");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: snapshotList){
                            Log.d(TAG, "onSuccess: " + snapshot.getData().toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
        return habit;
    }

    public User getFollowers() {
        db
                .collection("Followers")
                .whereEqualTo("Current_uid", getCurrent_uid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Log.d(TAG, "onSuccess: We are getting the date");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "onSuccess: " + snapshotList.get(0).getString("Name"));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
        return followers;
    }

    public User getFollowees() {
        db
                .collection("Followees")
                .whereEqualTo("Current_uid", getCurrent_uid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Log.d(TAG, "onSuccess: We are getting the date");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "onSuccess: " + snapshotList.get(0).getString("Name"));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
        return followees;
    }

    public HabitEvent getEvent() {
        db
                .collection("Events")
                .whereEqualTo("Current_uid", getCurrent_uid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Log.d(TAG, "onSuccess: We are getting the date");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        Log.d(TAG, "onSuccess: " + snapshotList.get(0).getString("About"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

        return event;
    }
}
