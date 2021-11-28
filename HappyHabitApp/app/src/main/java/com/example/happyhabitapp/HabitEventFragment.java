package com.example.happyhabitapp;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Button addLocationButton; // Button that enables user to add location to event
    private EditText eventTitle; // The title of the habit event
    private EditText eventReason; // an optional comment made by the user on the habit event
    private Spinner statusMenu; // drop down menu that enables user to select a status for the habit event
    private ArrayAdapter<CharSequence> statusAdapter; // ArrayAdapter to allow us to select from an preset list of statuses
    private View view; // The fragments overall layout
    private OnFragmentInteractionListener listener; // listener for the FragmentListener interface
    private String addKey = "habit";
    private String editKey = "event";
    private ActivityResultLauncher<Intent> getLocationFromMap; //Launch activity to get location
    private ActivityResultLauncher<String> requestPermissionLauncher; // request permissions
    LocationCallback mLocationCallback; // start location polling
    private LatLng latlng = null; // location of habit event
    private Boolean edit; // if editing, true.

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
       if ( bundle != null && bundle.containsKey(editKey)){
           HabitEvent habitEvent = (HabitEvent) bundle.getSerializable(editKey);
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
     * Also initializes the location adding feature
     */
    private void initFragment(){

        // inflate the fragment
        view =  LayoutInflater.from(getActivity()).inflate(R.layout.habit_event_fragment_layout,null);

        // connect all Views from habit event xml file
        eventPhoto = view.findViewById(R.id.habit_event_pic);
        dateDisplay = view.findViewById(R.id.display_event_date);
        addPhotoButton = view.findViewById(R.id.take_photo_btn);
        addLocationButton = view.findViewById(R.id.add_location_btn);
        eventTitle = view.findViewById(R.id.habit_event_title);
        eventReason = view.findViewById(R.id.habit_event_reason);
        statusMenu = view.findViewById(R.id.status_menu);

        // initialise the adapter for the status drop down menu
        statusAdapter = ArrayAdapter.createFromResource((Context) listener, R.array.statuses, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusMenu.setAdapter(statusAdapter);

        getLocationFromMap = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        latlng = intent.getExtras().getParcelable("latlng");
                        Log.d("testReceive", String.valueOf(latlng.latitude));
                        Log.d("testReceive", String.valueOf(latlng.longitude));
                    }
                });

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        Intent intent = new Intent(getContext(),MapActivity.class);
                        intent.putExtra("latlng", latlng);
                        intent.putExtra("edit",edit);
                        getLocationFromMap.launch(intent);
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Location");
                        alertDialog.setMessage("Location is needed if you would like to add it to your habit event, If denied, location will be disabled");

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"allow",new DialogInterface.OnClickListener(){

                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("test", String.valueOf(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)));
                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"deny",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //disable location add button for habit event
                                addLocationButton.setClickable(false);
                                addLocationButton.setAlpha(.5f);
                            }
                        });
                        alertDialog.show();
                    }});
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                }
            };


        return;
    }


    /**
     * Creates a Fragment that supports the addition of a brand new Habit Event
     * User inputted values are used to get attributes of th event
     * @param builder
     * @param habit
     *      Habit: Passed in Habit that the event is associated with
     * @return
     */
    private Dialog AddNewEvent(AlertDialog.Builder builder, Habit habit){

        // format the date into a string to display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date = Calendar.getInstance(); // Calendar object to be passed in
        String dateString = dateFormat.format(date.getTime());

        //Display date
        dateDisplay.setText(dateString);
        edit = false;

        addLocationButton.setOnClickListener(new View.OnClickListener() {
             @SuppressLint("MissingPermission")
             @RequiresApi(api = Build.VERSION_CODES.M)
             @Override
             public void onClick(View view) {
                 if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=  PERMISSION_GRANTED){
                     requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                 }
                 else{
                     // start getting location updates
                     LocationRequest mLocationRequest = LocationRequest.create();
                     mLocationRequest.setInterval(60000);
                     mLocationRequest.setFastestInterval(5000);
                     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                     LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

                     Intent intent = new Intent(getContext(),MapActivity.class);
                     intent.putExtra("latlng", latlng);
                     intent.putExtra("edit",edit);
                     getLocationFromMap.launch(intent);
                 }
             }});

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
                        if (latlng != null){ // location added
                            listener.addNewEvent(new HabitEvent(date,title,status,reason,latlng));
                        }
                        else if (latlng != null){ // picture and location added //TODO: add picpath != null
                            //listener.addNewEvent(new HabitEvent(date, title, status, reason,picpath,latlng));
                        }
                        else{
                        listener.addNewEvent(new HabitEvent(date, title, status, reason));

                        }
                    }

                }).create();
    }


    /**
     * Create that fragment that supports the editing of an existing habit
     * Displays the already existing attributes and allows users to change them
     * @param builder
     * @param habitEvent
     *      HabitEvent: Event that the user wishes to edit
     * @return
     */
    private Dialog EditEvent(AlertDialog.Builder builder, HabitEvent habitEvent){

        // convert the event's date into a readable string
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date = habitEvent.getEvent_date();
        String dateString = dateFormat.format(date.getTime());
        edit = true;
        // set latlng to be the event's previously chosen location
        // if one was not chosen, latlng will null
        latlng = habitEvent.getLocation();

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=  PERMISSION_GRANTED){
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                else{
                    LocationRequest mLocationRequest = LocationRequest.create();
                    mLocationRequest.setInterval(60000);
                    mLocationRequest.setFastestInterval(5000);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

                    Intent intent = new Intent(getContext(),MapActivity.class);
                    intent.putExtra("latlng", latlng);
                    intent.putExtra("edit",edit);
                    getLocationFromMap.launch(intent);
                }
            }});

        // Set visible fields to display current Event's attributes
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
                        if (latlng != null){ // location added
                            listener.editEvent(new HabitEvent(date,title,status,reason,latlng),habitEvent);
                        }
                        else if (latlng != null){ // picture and location added //TODO: add picpath != null
                            //listener.addNewEvent(new HabitEvent(date, title, status, reason,picpath,latlng));
                        }
                        else {
                            listener.editEvent(new HabitEvent(date, title, status, reason), habitEvent);
                        }
                    }

                }).create();

    }


    /**
     * Translates the possible selectable statuses into the corresponding
     * integers to be passed into the Habit Event
     * @param statusString
     *      String: either: Incomplete, Complete, or In Progress
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
     *      HabitEvent object to be viewed
     * @param date
     *      String that corresponds to the event's date of creation
     */
    private void displayCurrentEvent(HabitEvent event, String date){

        //Display date
        dateDisplay.setText(date);

        // display text fields
        eventTitle.setText(event.getTitle());
        eventReason.setText(event.getDescription());

        // preselect the drop down menu

        // IMPORTANT: This assumes the status numbers properly correspond to the order that the
        // status strings appear on the drop down menu, to ensure correctness please to refer to
        // the mapped status attribute values in HabitEvent class
        int pos = event.getStatus();
        statusMenu.setSelection(pos);

        return;
    }



}
