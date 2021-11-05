package com.example.happyhabitapp;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DashBoardUITest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    //**IMPORTANT if User is logged in prior to running app, comment out authenticate method call
    // this is because the app launches from different states and activities depending on logged in status
    // We test if we can go to different activities and come back
    // starting from the DashBoard
    @Test
    public void testTransition(){

        //authenticate();

        // Real Test starts
        // start from Dashboard
        // click on button to go to HabitList Activity
        solo.clickOnButton("Habits List");
        // make sure were back in the HabitListActivity
        solo.assertCurrentActivity("Failed to move", HabitActivity.class);
        // click on the back button
        solo.clickOnView( solo.getView(R.id.logo));
        // make sure were back
        solo.assertCurrentActivity("Failed to return to Dashboard", DashBoard.class);

    }

    public void authenticate(){
        //**IMPORTANT** before proper tests are run authentication is required
        // This section simulates authentications which may be altered as the accounts are changed or
        // the state of the app changes
        solo.clickOnButton("Login/Register");
        solo.clickOnButton("Sign in with email");
        solo.enterText((EditText) solo.getView(R.id.email), "anthony@test.com");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "abcd1234");
        solo.clickOnButton("Sign in");
    }



}
