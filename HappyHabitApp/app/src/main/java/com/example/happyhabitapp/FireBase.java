
///**
// * This class is responsible for getting, uploading, deleteing, and all other data communication from Firestore database
// * Author: Frank (Ziang) Li
// */



package com.example.happyhabitapp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private DocumentReference User = Users.document(getUserName());
    private CollectionReference HabitList = User.collection("HabitList");
    private CollectionReference Followers = User.collection("Followers");
    private CollectionReference Followees = User.collection("Followees");
    private CollectionReference Requests = User.collection("Requests");
    FirestoreCallback fireapi;

    /* Constructors */
    public FireBase() {}

    /* ========================================================== Functions to upload to database ================================================================== */

    /**
     * this function upload user's information to firebase
     * @param user (User class object)
     */

    public void setUser(User user) {

        Map<String, Object> map = new HashMap<>();
        map.put("DisplayName", user.getUsername());
        map.put(getCurrent_uid(), "Uid");
        Users.document(user.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){

                        }else {
                            User
                                    .set(map)
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

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: database error", e);
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
        map.put("public", habit.getPublicHabit());


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

    public void setHabitEvent(HabitEvent event, Habit about) {

        Map<String, Object> map = new HashMap<>();
        map.put("About", about.getTitle());
        map.put("Title", event.getTitle());
        map.put("Date", event.getEvent_date());
        map.put("decodedImage", event.getImage());
        if (event.getLocation() != null){
            map.put("longitude", event.getLocation().longitude);
            map.put("latitude", event.getLocation().latitude);
        }
        map.put("Description", event.getDescription());
        map.put("Status", event.getStatus());

        HabitList
                .document(about.getTitle())
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

    /**
     * Send request to certain user
     * @param name the person send request to
     */
    public void sendRequest (String name){
        Map<String, Object> map = new HashMap<>();
        map.put("Name", getUserName());
        DocumentReference other_user = getOtherUser(name);
        CollectionReference others_request = other_user.collection("Requests");
        others_request
                .document(getUserName())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: request sent");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

    }


    /* ================================================================== Functions to Retrieve data from firebase ======================================================= */

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
    public String getUserName(){
        String current_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return current_name;
    }


    /* get information */

    /**
     * this function get follower list and store in list
     * @param list list to store the followers
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
                        fireapi.callUserList(list);
                    }
                });
    }

    /**
     * this function get followee list and store in list
     * @param list list to store the followees
     */
    public void getFolloweeList(ArrayList<User> list){
        ArrayList<String> followees = new ArrayList<>();
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        Users
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        // loop through all existing users
                        Log.d(TAG, "onEvent: getting followees");
                        if (value != null){
                            for (QueryDocumentSnapshot doc: value){
                                map[0] = doc.getData();
                                String followee_name = (String) map[0].get("DisplayName");
                                if (followee_name != null){
                                    Log.d(TAG, "onEvent: null followees");
                                    getOtherUser(followee_name)
                                            .collection("Followers")
                                            .document(getUserName())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    // if the current user is in the follower list of other user, add it to the current user's followee list
                                                    if (documentSnapshot.exists()){
                                                        User followee = new User(followee_name);
                                                        setFollowees(followee);
                                                        list.add(followee);
                                                    }
                                                    fireapi.callUserList(list);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }

                            }
                        }

                    }
                });
    }

    /**
     * this function get habit list and store in list
     * @param list list to store the habits
     */
    public void getHabitList(ArrayList<Habit> list){

        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        HabitList
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
//                        Log.d(TAG, "onEvent: getting Habits");
                        if (value != null){
                            for (QueryDocumentSnapshot doc: value){
                                map[0] = doc.getData();
                                // get title
                                String title = (String) map[0].get("Title");
                                // get reason
                                String reason = (String) map[0].get("Reason");
                                // get week_freq
                                List<Long> freq = (List<Long>) map[0].get("Days");
                                int[] finalFreq = new int[freq.size()];
                                for (int i = 0; i<freq.size(); i++){
                                    //Long l = new Long(freq[i]);
                                    finalFreq[i] = freq.get(i).intValue();
                                }
                                // get date
                                Map<String, Object> dates = (Map<String, Object>) map[0].get("Dates");
                                Timestamp createDate = (Timestamp) dates.get("time");
                                Date cal = createDate.toDate();
                                Calendar finalDate  = Calendar.getInstance();
                                finalDate.setTime(cal);
                                // get public status
                                boolean pub = (boolean) map[0].get("public");
                                // construct habit and add to list
                                Habit habit = new Habit(title, reason, finalDate, finalFreq, pub, null);
                                Log.d(TAG, String.valueOf(finalFreq[0]));
                                list.add(habit);
                            }
                            fireapi.callHabitList(list);
                        }

                    }
                });
    }

    /**
     * this function get habitevents of a habit and store it in the list
     * @param list list to store the events
     * @param habit the habit of the events
     */
    public void getEventList(ArrayList<HabitEvent> list, Habit habit){
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        final int[] final_stat = new int[1];
        HabitList
                .document(habit.getTitle())
                .collection("Events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        Log.d(TAG, "onEvent: getting Habit events");
                        for (QueryDocumentSnapshot doc: value){
                            map[0] = doc.getData();
                            // get date
                            Map<String, Object> dates = (Map<String , Object>) map[0].get("Date");
                            Timestamp createDate = (Timestamp) dates.get("time");
                            Date cal = createDate.toDate();
                            Calendar finalDate = Calendar.getInstance();
                            finalDate.setTime(cal);
                            // get title
                            String title = (String) map[0].get("Title");
                            //get status
                            Long temp_stat = (Long) map[0].get("Status");

                            if (temp_stat == 0){
                                final_stat[0] = 0;
                                Log.d(TAG, "onEvent: 0");
                            } else if (temp_stat == 1){
                                final_stat[0] = 1;
                                Log.d(TAG, "onEvent: 1");
                            }else if (temp_stat == 2){
                                final_stat[0] = 2;
                                Log.d(TAG, "onEvent: 2");
                            }
                            // get description
                            String description = (String) map[0].get("Description");
                            // get picPath
                            String encodeImage = (String) map[0].get("decodedImage");
                            // get location
                            Double longitude = (Double) map[0].get("longitude");
                            Double latitude = (Double) map[0].get("latitude");
                            if (latitude == null || longitude == null){
                                Log.d(TAG, "onEvent: null~");
                                // if no location selected use constructor without location
                                HabitEvent event = new HabitEvent(finalDate, title, final_stat[0], description, encodeImage,null);
                                list.add(event);
                            }else {
                                // if location is selected use full constructor
                                com.google.android.gms.maps.model.LatLng location = new com.google.android.gms.maps.model.LatLng(latitude, longitude);
                                HabitEvent event = new HabitEvent(finalDate, title, final_stat[0], description, encodeImage, location);
                                list.add(event);
                            }
                        }
                        fireapi.callEventList(list);
                    }
                });
    }

    /**
     * This function gets all the pending requests and store it in the passed in ArrayList
     * @param requesters list to store the users in request pending list
     */
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
//                Log.d(TAG, "onEvent: " + requesters.get(0).getUsername());
                fireapi.callUserList(requesters);
            }
        });
    }

    /**
     * get the other people's public habit
     * @param name the other user's name
     * @param list list to store the habits
     */
    public void getOthersHabit(String name, ArrayList<Habit> list){
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        Users
                .document(name)
                .collection("HabitList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "onEvent: getting other's habits");
                        list.clear();
                        for (QueryDocumentSnapshot doc: value){
                            map[0] = doc.getData();
                            // get title
                            String title = (String) map[0].get("Title");
                            // get reason
                            String reason = (String) map[0].get("Reason");
                            // get week_freq
                            List<Long> freq = (List<Long>) map[0].get("Days");
                            int[] finalFreq = new int[freq.size()];
                            for (int i = 0; i<freq.size(); i++){
                                //Long l = new Long(freq[i]);
                                finalFreq[i] = freq.get(i).intValue();
                            }
                            // get date
                            Map<String, Object> dates = (Map<String, Object>) map[0].get("Dates");
                            Timestamp createDate = (Timestamp) dates.get("time");
                            Date cal = createDate.toDate();
                            Calendar finalDate  = Calendar.getInstance();
                            finalDate.setTime(cal);
                            // get public status
                            boolean pub = (boolean) map[0].get("public");
                            // construct habit and add to list
                            Habit habit = new Habit(title, reason, finalDate, finalFreq, pub, null);
                            Log.d(TAG, String.valueOf(finalFreq[0]));
                            if (habit.getPublicHabit()){
                                list.add(habit);
                            }
                        }
                        fireapi.callHabitList(list);
                    }
                });
    }

    /**
     * get the other's selected habit's habit events
     * @param name the other user's name
     * @param list list to store the events
     * @param habit the habit for the events
     */
    public void getOthersEvent (String name, ArrayList<HabitEvent> list, Habit habit){
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        final int[] final_stat = new int[1];
        Users
                .document(name)
                .collection("HabitList")
                .document(habit.getTitle())
                .collection("Events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        Log.d(TAG, "onEvent: getting Habit events");
                        for (QueryDocumentSnapshot doc: value){
                            map[0] = doc.getData();
                            // get date
                            Map<String, Object> dates = (Map<String , Object>) map[0].get("Date");
                            Timestamp createDate = (Timestamp) dates.get("time");
                            Date cal = createDate.toDate();
                            Calendar finalDate = Calendar.getInstance();
                            finalDate.setTime(cal);
                            // get title
                            String title = (String) map[0].get("Title");
                            //get status
                            Long temp_stat = (Long) map[0].get("Status");

                            if (temp_stat == 0){
                                final_stat[0] = 0;
                                Log.d(TAG, "onEvent: 0");
                            } else if (temp_stat == 1){
                                final_stat[0] = 1;
                                Log.d(TAG, "onEvent: 1");
                            }else if (temp_stat == 2){
                                final_stat[0] = 2;
                                Log.d(TAG, "onEvent: 2");
                            }
                            // get description
                            String description = (String) map[0].get("Description");
                            // get picPath
                            String picPath = (String) map[0].get("decodedImage");
                            // get location
                            Double longitude = (Double) map[0].get("longitude");
                            Double latitude = (Double) map[0].get("latitude");
                            if (latitude == null || longitude == null){
                                // if no location selected use constructor without location
                                HabitEvent event = new HabitEvent(finalDate, title, final_stat[0], description, picPath, null);
                                list.add(event);
                            }else {
                                // if location is selected use full constructor
                                double final_longitude = longitude.doubleValue();
                                double final_latitude = latitude.doubleValue();
                                com.google.android.gms.maps.model.LatLng location = new com.google.android.gms.maps.model.LatLng(final_latitude, final_longitude);
                                HabitEvent event = new HabitEvent(finalDate, title, final_stat[0], description, picPath, location);
                                list.add(event);
                            }
                        }
                        fireapi.callEventList(list);
                    }
                });
    }

    public void getOtherPic(String name){
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref
                .child("profileImage")
                .child(name+".jpeg")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        fireapi.callUri(uri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }




    /* ======================================================================== Functions that delete data from firebase ============================================================================ */

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

    /**
     * This function delete a pending request from requesting list
     * @param requester
     */
    public void delRequst(User requester){
        Requests
                .document(requester.getUsername())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: deleted requester");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    /* ======================================================== Functions that check status of data in the firebase ============================================================= */

    /**
     * check if the name is in the Users collection and change the boolean value through callBack
     * @param name
     */
    public void hasUser(String name, boolean[] has){

        has[0] = false;
        Users
                .document(name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            has[0] = true;
                            if (has[0] == true){
                                Log.d(TAG, "onSuccess: yes");
                            }
                        }
                        fireapi.checkUser(has);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * check if new user's username has already been taken
     * @param name
     * @param Uid
     * @param has
     */
    public void displayNameExists(String name, String Uid, boolean has[]){
        final Map<String, Object>[] map = new Map[]{new HashMap<>()};
        has[0] = false;
        Users
                .document(name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess: check names");
                        if (documentSnapshot.exists()){
                            Log.d(TAG, "onSuccess: name exists");
                            User
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String uid = (String) documentSnapshot.get(getCurrent_uid());
                                            if (uid == null){
                                                Log.d(TAG, "onSuccess: different uid");
                                                has[0]=true;
                                            }else {
                                                Log.d(TAG, "onSuccess: same uid");
                                            }
                                            fireapi.checkUser(has);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        
                    }
                });
    }

    /* ================================================================================== Special Helper Functions ============================================================ */

    /**
     * function to get other user's document reference, used when viewing public habits
     * @param name
     * @return DocumentReference
     */
    public DocumentReference getOtherUser (String name){
        return Users.document(name);
    }



    /* ============================================================================ CallBack Related Functions ================================================================== */
    /**
     * Implementing callBack to solve the syncing issue
     * @param fireapi
     */
    public void setApi(FirestoreCallback fireapi){
        this.fireapi = fireapi;
    }

    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {

    }

    @Override
    public void checkUser(boolean[] has) {
    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {

    }

    @Override
    public void callUri(Uri uri) {

    }


}

