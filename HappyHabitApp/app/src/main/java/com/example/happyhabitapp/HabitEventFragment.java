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
import android.widget.Toast;

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
    private Spinner statusMenu; // drop down menu that enables user to select a status for the habit event
    private ArrayAdapter<CharSequence> statusAdapter; // ArrayAdapter to allow us to select from an preset list of statuses
    private View view; // The fragments overall layout
    private OnFragmentInteractionListener listener; // listener for the FragmentListener interface
    private String addKey = "habit";
    private String editKey = "event";

    /**
     * Denotes actions to be taken when the fragment is first created
     * Initializes the fragment and determines the whether user wants to add
     * or edit a habit event
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        initFragment(); // initialize all the views with the fragment
        Bundle bundle = getArguments(); // get passed in arguments into the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // An argument is always passed into this fragment, either an existing HabitEvent or a Habit

        // if were editing, we pass in an existing HabitEvent and extract it to modify
       if ( bundle != null && bundle.containsKey("Edit Event")){
           HabitEvent habitEvent = (HabitEvent) bundle.getSerializable("Edit Event");
           return EditEvent(builder, habitEvent);
       }
       // if were adding a new habit, we pass in a Habit that we pass into the constructor
       else if ( bundle != null && bundle.containsKey(addKey)){
           Habit habit = (Habit) bundle.getSerializable(addKey);
           return AddNewEvent(builder, habit);
       }

       else {
           throw new RuntimeException("No proper arguments passed in");
       }

    }

    /**
     * Interface that controls that denotes whether a user
     * chooses to add or edit a habit event
     */
    public interface OnFragmentInteractionListener{
        // add a newly created Habit Event
        void addNewEvent(HabitEvent event);
        // edit an existing habit event
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
        statusMenu = view.findViewById(R.id.status_menu);

        // initialise the adapter for the status drop down menu
        statusAdapter = ArrayAdapter.createFromResource((Context) listener, R.array.statuses, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusMenu.setAdapter(statusAdapter);

        return;
    }

    /**
     * Creates a Fragment that supports the addition of a brand new Habit Event
     * User inputted values are used to get attributes of th event
     * @param builder
     * @return
     */
    private Dialog AddNewEvent(AlertDialog.Builder builder, Habit habit){

        // format the date into a string to display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date = Calendar.getInstance(); // Calendar object to be passed in
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

                    // get the selected status from the menu
                    String statusString = statusMenu.getSelectedItem().toString();
                    int status = getStatusNum(statusString);

                    // try to create new Habit Event

                    // no title is given
                    if (title.compareTo("") == 0) {
                        Context context = getContext();

                        CharSequence text = "Title required";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    // no status is given
                    else if ( status == -1) {
                        Context context = getContext();

                        CharSequence text = "Please select a status";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    // all required fields are filled
                    else {
                        listener.addNewEvent(new HabitEvent(date, title, status, reason));
                    }

                }).create();
    }

    /**
     * Create that fragment that supports the editing of an existing habit
     * Displays the already existing attrbutes and allows users to change them
     * @param builder
     * @return
     */
    private Dialog EditEvent(AlertDialog.Builder builder, HabitEvent habitEvent){

        // Set visible fields to display current Event's attributes
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date = habitEvent.getEvent_date();
        String dateString = dateFormat.format(date.getTime());
        displayCurrentEvent(habitEvent, dateString);

        // return user inputs
        return builder
                .setView(view)
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("DONE", (dialogInterface, i) -> {

                    // get user input fields into strings
                    String title = eventTitle.getText().toString();
                    String reason = eventReason.getText().toString();

                    // get the selected status from the menu
                    String statusString = statusMenu.getSelectedItem().toString();
                    int status = getStatusNum(statusString);

                    // try to create new Habit Event

                    // no title is given
                    if (title.compareTo("") == 0) {
                        Context context = getContext();

                        CharSequence text = "Title required";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    // no status is given
                    else if ( status == -1) {
                        Context context = getContext();

                        CharSequence text = "Please select a status";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    // all required fields are filled
                    else {
                        listener.editEvent( new HabitEvent(date, title, status, reason), habitEvent);
                    }

                }).create();

    }


    /**
     * Translates the possible selectable statuses into the corresponding
     * integers to be passed into the Habit Event
     * @param statusString
     *      String, either: Complete, Incomplete, or In Progress
     * @return
     *      int: either 0 = Incomplete, 1 = Complete, 2 = In Progress, -1 = Error
     */
    private int getStatusNum(String statusString){
        // strings of possible statuses
        String[] statuses = {"Incomplete", "Complete", "In Progress"};
        int size = statuses.length;
        int val = -1; //initialized to error value

        // Status numbers correspond to the their position in the string array
        for (int i = 0; i < size; i++) {
            if (statuses[i].compareTo(statusString) == 0){
                val = i;
            }
        }

        return val;
    }

    /**
     * Displays all the attributes of an existing HabitEvent into the fragment
     * This allows Users to view an existing Event
     * @param event
     */
    private void displayCurrentEvent(HabitEvent event, String date){


        //Display date
        dateDisplay.setText(date);

        // display text fields
        eventTitle.setText(event.getTitle());
        eventReason.setText(event.getDescription());

        // preselect the drop down menu

        // IMPORTANT: This assumes the status numbers properly correspond to the order that the
        // status strings appear on the drop down menu
        int pos = event.getStatus();
        statusMenu.setSelection(pos);

        return;
    }



}
