package com.example.happyhabitapp;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    Date d = new Date();
    int week_freq[] = {1,2,3,4};
    Calendar c = Calendar.getInstance();

    Habit habit = new Habit("jump", "exercise", c, week_freq);

    ArrayList<Habit> habitList;


    ArrayList<User> followList;
    User user = new User("lichild", "path");


    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //habitList.add(habit);
        //followList.add(user);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startLogin();
        }
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
        FireBase fire = new FireBase(user);
        fire.init();
    }

    public void readDocument(View view) {
        Toast.makeText(this, "Reading a doc...", Toast.LENGTH_SHORT).show();

    }

    public void updateDocument(View view) {
        Toast.makeText(this, "updateDocument", Toast.LENGTH_SHORT).show();

    }

    public void deleteDocument(View view) {
    }

    public void getAllDocuments(View view) {
        Toast.makeText(this, "getAllDocuments", Toast.LENGTH_SHORT).show();

    }

    public void getAllDocumentsWithRealtimeUpdates(View view) {
    }
}