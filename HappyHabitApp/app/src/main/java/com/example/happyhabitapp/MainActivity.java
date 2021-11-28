/**
 * This Activity is responsable for login screen
 * Author: Frank Li
 */

package com.example.happyhabitapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "";
    int AUTHUI_REQUEST_CODE = 10001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // if signed in go to Dashboard
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent testIntent = new Intent(MainActivity.this, MergedDisplayActivity.class);
            startActivity(testIntent);
            this.finish();
        }

        new CountDownTimer(1500, 1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent loginIntent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(loginIntent);
            }

        }.start();



    }





//    public void handleLoginRegister(View view) {
//        List<AuthUI.IdpConfig> provider = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build()
//        );
//
//        Intent intent = AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(provider)
//                .setLogo(R.drawable.hhicon2)
//                .setAlwaysShowSignInMethodScreen(true)
//                .build();
//
//        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AUTHUI_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // We have signed in the user or we have a new user
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Log.d(TAG, "new/singin: " + user.toString());
//                //Checking for User (New/Old)
//                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
//                    //This is a New User
//                    Toast.makeText(this,"Welcome new User", Toast.LENGTH_SHORT).show();
//                } else {
//                    //This is a returning user
//                    Toast.makeText(this,"Welcome back", Toast.LENGTH_SHORT).show();
//
//                }
//
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                this.finish();
//
//            } else {
//                // Signing in failed
//                IdpResponse response = IdpResponse.fromResultIntent(data);
//                if (response == null) {
//                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");
//                } else {
//                    Log.e(TAG, "onActivityResult: ", response.getError());
//                }
//            }
//        }
//    }






}