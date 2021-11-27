
/**
 * This class is able to upload desired data to the firebase, and get the current user uid
 * Author: Katia Zhang, Frank Li
 */



package com.example.happyhabitapp;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBase implements FirestoreCallback{

    private final String TAG = "FireBase";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    


    private String current_uid;

    private CollectionReference Users = db.collection("Users");
    private DocumentReference User = Users.document(getUsername());
    private CollectionReference HabitList = User.collection("HabitList");
    private CollectionReference Followers = User.collection("Followers");
    private CollectionReference Followees = User.collection("Followees");
    private CollectionReference Requests = User.collection("Requests");
    FirestoreCallback fireapi;

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
        map.put("Public", habit.getPublicHabit());

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

    public void setRequest(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("Name", user.getUsername());
        Requests
                .document(user.getUsername())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: set Request");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    /**
     * this function will upload habit event to the firebase
     * @param event (HabitEvent class object)
     */

//    public void setHabitEventEvent(HabitEvent event) {
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("About", event.getHabit().getTitle());
//        map.put("Date", event.getEvent_date());
//        map.put("picPath", event.getPic_path());
//        map.put("location", event.getLocation());
//        map.put("description", event.getDescription());
//
//        HabitList
//                .document(event.getHabit().getTitle())
//                .collection("Events")
//                .document(event.getTitle())
//                .set(map)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "onSuccess: added event to fire");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: could not add event", e);
//                    }
//                });
//    }


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
     * this function return current user's user name as string
     * @return
     */
    public String getUsername(){
        String current_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return current_name;
    }


    /* get information */

    /**
     * this function get follower list and store in list
     * @param list
     */
    public void getFollowerList(ArrayList<User> list){

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
    public void getFolloweeList(ArrayList<User> list){
        Followees
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting followees");
                            User followee = doc.toObject(com.example.happyhabitapp.User.class);
                            Log.d(TAG, "onEvent: " + followee.getUsername());
                            list.add(followee);
                        }
                    }
                });
    }

//    public void test(){
//        HabitList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (DocumentSnapshot doc: task.getResult()){
//                        doc.getData();
//                    }
//                    Log.d(TAG, "onComplete: ");
//                }else {
//                    Log.e(TAG, "Error: ", task.getException());
//                }
//            }
//        });
//    }


    /**
     * this function get habit list and store in list
     * @param list
     */
    public void getHabitList(ArrayList<Habit> list){
        list.clear();
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        HabitList
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting Habits");
                            map[0] = doc.getData();
                            String title = (String) map[0].get("Title");
                            String reason = (String) map[0].get("Reason");
                            List<Long> freq = (List<Long>) map[0].get("Days");
                            int[] finalFreq = new int[freq.size()];
                            for (int i = 0; i<freq.size(); i++){
                                //Long l = new Long(freq[i]);
                                finalFreq[i] = freq.get(i).intValue();
                            }
                            Map<String, Object> dates = (Map<String, Object>) map[0].get("Dates");
                            Timestamp createDate = (Timestamp) dates.get("time");
                            Date cal = createDate.toDate();
                            Calendar finalDate  = Calendar.getInstance();
                            finalDate.setTime(cal);
                            boolean pub = (boolean) map[0].get("Public");
                            Habit habit = new Habit(title, reason, finalDate, finalFreq, pub, null);
                            Log.d(TAG, String.valueOf(finalFreq[0]));
                            list.add(habit);
                        }
                        fireapi.callHabitList(list);
                    }
                });
    }






    /**
     * this function get habit event list and store in list
     * @param list
     */
    public void getEventList(ArrayList<HabitEvent> list, Habit habit){
        HabitList
                .document(habit.getTitle())
                .collection("Events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting Habit events");
                            HabitEvent event = doc.toObject(HabitEvent.class);
                            Log.d(TAG, "onEvent: " + event.getTitle());
                            list.add(event);
                        }
                    }
                });
    }


    public void getRequestList(ArrayList<User> requesters){
        requesters.clear();
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        Requests
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "onEvent: getting Requester List");
                for (QueryDocumentSnapshot doc: value){
                    map[0] = doc.getData();
                    String requester_name = (String) map[0].get("Name");
                    User user = new User(requester_name);
                    requesters.add(user);
                }
                fireapi.callRequestList(requesters);
            }
        });
    }


    /* delete data from firebase */

    /**
     * this function delete user from users
     */
    public void delUser(){
        User.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: deleted user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: couldn't delete user", e);
                    }
                });
    }

    /**
     * this fucntion delete certain habit from HabitList
     * @param habit
     */
    public void delHabit(Habit habit){
        HabitList
                .document(habit.getTitle())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: deleted haibt");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: couldn't delete habit", e);
                    }
                });
    }

    /**
     * this function delete follower from Followers
     * @param follower
     */
    public void delFollower(User follower){
        Followers.document(follower.getUsername())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: deleted follower");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: couldn't delete follower", e);
                    }
                });
    }


    /**
     * this function delete followee from Followees
     * @param followee
     */
    public void delFollowee(User followee){
        Followees.document(followee.getUsername())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: deleted followee");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: couldn't delete followee", e);
                    }
                });
    }

    /**
     * this function delete havit event from Events
     * @param habit
     * @param event
     */
    public void delEvent(Habit habit,HabitEvent event){
        HabitList
                .document(habit.getTitle())
                .collection("Events")
                .document(event.getTitle())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: deleted event");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: couldn't delete event", e);
                    }
                });
    }

    public void setApi(FirestoreCallback fireapi){
        this.fireapi = fireapi;
    }

    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callRequestList(ArrayList<User> requesters) {

    }
}

