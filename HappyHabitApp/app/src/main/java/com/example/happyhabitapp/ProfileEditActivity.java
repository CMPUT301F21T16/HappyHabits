package com.example.happyhabitapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {
    private FireBase fire = new FireBase();
    private TextView userName;
    private ImageView userProfile;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        userName = findViewById(R.id.user_name);
        userProfile = findViewById(R.id.imageView4);
        upload = findViewById(R.id.button3);
        userName.setText(fire.getUserName());
//        userProfile.setBackground(default_profile_pic);
        upload.setText("Upload");
    }
}