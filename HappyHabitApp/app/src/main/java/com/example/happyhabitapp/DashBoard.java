package com.example.happyhabitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ImageView user_prof = (ImageView) findViewById(R.id.user_profile_pic);
        user_prof.setBackgroundResource(R.drawable.lol);
    }


}