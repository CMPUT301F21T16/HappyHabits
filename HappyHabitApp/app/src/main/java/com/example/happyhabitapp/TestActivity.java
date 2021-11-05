/**
 * This activity is used for testing firebase
 * Author: Frank Li
 */


package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
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
import java.util.Map;

public class TestActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    ArrayList<User> list = new ArrayList<>();
    ArrayList<Habit> habits = new ArrayList<>();
    ArrayList<HabitEvent> events = new ArrayList<>();

    Calendar c = Calendar.getInstance();
    int week_frq[] = {1};


    User user = new User("lichild", "path");
    User user2 = new User("koko", "path2");

    Habit habit = new Habit("jump", "exercise",c, week_frq,true);
    Habit habit2 = new Habit("watch", "fun", c, week_frq, true);
    HabitEvent event = new HabitEvent(habit, c, "today's jump");


    FireBase fire = new FireBase();



    private final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        fire.getEventLst(events, habit);
    }

    private void startLogin(){
        startActivity(new Intent(TestActivity.this, MainActivity.class));
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.logout){
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startLogin();
                            }
                            else{
                                Log.e(TAG, "onComplete", task.getException());
                            }
                        }
                    });
        }

        return true;
    }


    public void createDocument(View view) {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
        fire.setUser(user);
        fire.setHabit(habit);
        fire.setFollowers(user2);
        fire.setFollowees(user);
        fire.setHabitEventEvent(event);



    }

    public void readDocument(View view) {

        Integer size = events.size();
        Toast.makeText(this, size.toString(), Toast.LENGTH_SHORT).show();
    }

    public void updateDocument(View view) {
        Toast.makeText(this, "updateDocument", Toast.LENGTH_SHORT).show();
        fire.setHabit(habit2);


    }

    public void deleteDocument(View view) {
    }

    public void getAllDocuments(View view) {
        Toast.makeText(this, "getAllDocuments", Toast.LENGTH_SHORT).show();

    }

    public void getAllDocumentsWithRealtimeUpdates(View view) {
    }


    /* To check if there is a signed in user */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startLogin();
            return;
        }

        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(TAG, "onSuccess" + getTokenResult.getToken());
                    }
                });
    }
}