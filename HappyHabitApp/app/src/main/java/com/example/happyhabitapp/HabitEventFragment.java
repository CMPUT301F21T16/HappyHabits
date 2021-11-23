package com.example.happyhabitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This is the class for the Habit Event Fragment
 * This fragment allows one to view the details of an existing habit event or to enter the details
 * for a new Habit Event
 */
public class HabitEventFragment extends DialogFragment {

    // Attributes

    private ImageView eventPhoto; // Optional image to be uploaded by the user showcasing their habit event
    private TextView dateDisplay; //Text displaying the date the event was done
    private Button addPhotoButton; // Button that enables user to upload or change the eventPhoto
    private EditText eventTitle; // The title of the habit event
    private EditText eventReason; // an optional comment made by the user on the habit event
    private Spinner status; // drop down menu that enables user to select a status for the habit event
    private ArrayAdapter<CharSequence> statusAdapter; // ArrayAdapter to allow us to select from an preset list of statuses
    private View view; // The fragments overall layout
    private OnFragmentInteractionListener listener; // listener for the FragmentListener interface


    /**
     * Denotes actions to be taken when the fragment is first created
     * Initializes the fragment and determines the whether user wants to add
     * or edit a habit event
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog OnCreateDialog(@Nullable Bundle savedInstanceState){
        initFragment();
        Bundle bundle = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


       if ( bundle != null && bundle.containsKey("Edit Event")){
           return EditEvent(builder);
       }
       else {
           return AddNewEvent(builder);
       }
    }

    /**
     * Interface that controls that denotes whether a user
     * chooses to add or edit a habit event
     */
    public interface OnFragmentInteractionListener{
        void addNewEvent(HabitEvent event);
        void editEvent(HabitEvent newEvent, HabitEvent oldEvent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException((context.toString())
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Initializes all the views of the Fragment by connecting
     * all the event attributes to the associated xml views on creation
     */
    private void initFragment(){

        // connect all Views from habit event xml file
        view =  LayoutInflater.from(getActivity()).inflate(R.layout.habit_event_fragment_layout,null);
        eventPhoto = view.findViewById(R.id.habit_event_pic);
        dateDisplay = view.findViewById(R.id.display_event_date);
        addPhotoButton = view.findViewById(R.id.take_photo_btn);
        eventTitle = view.findViewById(R.id.habit_event_title);
        eventReason = view.findViewById(R.id.habit_event_reason);
        status = view.findViewById(R.id.status_menu);

        // initialise the adapter for the status drop down menu
        statusAdapter = ArrayAdapter.createFromResource((Context) listener, R.array.statuses, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(statusAdapter);

        return;
    }

    /**
     * Creates a Fragment that supports the addition of a brand new Habit Event
     * User inputted values are used to get attributes
     * @param builder
     * @return
     */
    private Dialog AddNewEvent(AlertDialog.Builder builder){

        // format the date into a string to display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date = Calendar.getInstance();
        String dateString = dateFormat.format(date.getTime());

        //Display date
        dateDisplay.setText(dateString);

        // return user inputs
        return builder
                .setView(view)
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("ADD HABIT", (dialogInterface, i) -> {

                    // get user input fields into strings
                    String title = eventTitle.getText().toString();
                    String reason = eventReason.getText().toString();


                    if (reason.compareTo("") == 0 && title.compareTo("") != 0) {
                        HabitEvent habitEvent = new HabitEvent(date, title);
                        listener.addNewEvent(habitEvent);
                    }
                    else if (reason.compareTo("") != 0 && title.compareTo("") != 0){
                        HabitEvent habitEvent = new HabitEvent(date, title);
                        listener.addNewEvent(habitEvent);

                    }
                }).create();
    }

    /**
     * Create that fragment that supports the editing of an existing habit
     * Displays the already existing attrbutes and allows users to change them
     * @param builder
     * @return
     */
    private Dialog EditEvent(AlertDialog.Builder builder){

        return builder
                .setView(view)
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("DONE", (dialogInterface, i) -> {

                }).create();

    }



}