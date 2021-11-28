package com.example.happyhabitapp;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the class for the Habit Event Fragment
 * This fragment allows one to view the details of an existing habit event or to enter the details
 * for a new Habit Event
 */
public class HabitEventFragment extends DialogFragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

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
    private ActivityResultLauncher<Intent> getLocationFromMap;
    private LatLng latlng;
    private Boolean edit;
    String currentPhotoPath;

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

        addPhotoButton.setOnClickListener(view -> dispatchTakePictureIntent());


        getLocationFromMap = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        latlng = intent.getExtras().getParcelable("latlng");
                        Log.d("testReceive", String.valueOf(latlng.latitude));
                        Log.d("testReceive", String.valueOf(latlng.longitude));
                    }
                });
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
        edit = false;

        addLocationButton.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.M)
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getContext(),MapActivity.class);
                 intent.putExtra("latlng", latlng);
                 intent.putExtra("edit",edit);
                 getLocationFromMap.launch(intent);
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

        // convert the event's date into a readable string
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar date = habitEvent.getEvent_date();
        String dateString = dateFormat.format(date.getTime());
        edit = true;

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MapActivity.class);
                intent.putExtra("latlng", latlng);
                intent.putExtra("edit",edit);
                getLocationFromMap.launch(intent);
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
                        listener.editEvent( new HabitEvent(date, title, status, reason), habitEvent);
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
        // status strings appear on the drop down menu
        int pos = event.getStatus();
        statusMenu.setSelection(pos);

        return;
    }

    /**
     * Start the camera by dispatching a camera intent.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that the camera is functional and available to use
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * The activity returns with the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            eventPhoto.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        FireBase fire = new FireBase();

        /* Will Have to cahnge this so it will be in our firebase class
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("test").document("test1");

        Map<String,String> map = new HashMap<>();
        map.put("EncodedImage",imageEncoded);
        ref
                .set(map)
                .addOnSuccessListener(unused -> Log.d("TEST", "onSuccess: user added to fire"))
                .addOnFailureListener(e -> Log.e("TEST", "onFailure: could not add user", e));

         */
    }

    public Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
