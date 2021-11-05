package com.example.happyhabitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * This class is used for the fragment for adding, editing and viewing {@link String}. Viewing
 * and editing is done in the same fragment.
 * @author Anuj, Armaan
 * @version 1.0
 */
public class Add_Edit_Fragment extends DialogFragment {

    /**
     * This variable is linked to the EditText for habit title and is of type {@link EditText}
     */
    private EditText habit_title;

    /**
     * This variable is linked to the DatePicker for the starting date of {@link Habit}. It is of
     * type {@link DatePicker}
     */
    private DatePicker habit_starting_date;

    /**
     * This variable is linked to the EditText for habit reason and is of type {@link EditText}
     */
    private EditText habit_reason;

    /**
     * The widget is used to pick the days of the week selected by the user
     */
    private MaterialDayPicker dayPicker;

    /**
     * A List that is directly connected to the MaterialDayPicker
     */
    private List<MaterialDayPicker.Weekday> pickerSelectedDays;

    /**
     * This listener is linked to the activity the fragment was called in and is of type
     * {@link onFragmentInteractionListener}
     */
    private onFragmentInteractionListener listener;

    /**
     * This variable is linked to the View for thelayout of the fragment in res and is of
     * type {@link View}
     */
    private View view;

    private Switch publicSwitch;
    /**
     * This interface is linked to the interactions the fragment has, which correlate to both
     * adding and editing {@link Habit}
     */
    public interface onFragmentInteractionListener{
        void onAddPressed(Habit newHabit);
        void onEditPressed(Habit newHabit, Habit oldHabit);
    }


    /**
     * initializes all the widgets as well as obtains the habit from the bundle
     **/
    public void initFragment() {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_habit_fragment_layout, null);
        habit_title = view.findViewById(R.id.habit_title_editText);
        habit_reason = view.findViewById(R.id.habit_reason_editText);
        habit_starting_date = view.findViewById(R.id.habit_starting_date);
        dayPicker = view.findViewById(R.id.day_picker);
        publicSwitch = view.findViewById(R.id.publicSwitch);
        pickerSelectedDays = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof onFragmentInteractionListener){
            listener = (onFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onFragmentInteractionListener");
        }
    }

    /**
     * Create a Dialog Fragment from add_edit_habit_fragment_layout.xml and checks what made the
     * user wanted, Adding or editing.
     *
     * @param savedInstanceState
     *
     * @return {@link Dialog}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        Habit habit = (Habit)getArguments().getSerializable("habit");
        initFragment();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.Theme_AddEditFragment);

        if(habit != null) { //if wanting to edit a habit
            //editing the Habit
            return editHabit(habit, builder);
        } else { //if wanting to add a habit
            return addHabit(builder);
        }
    }

    /**
     * When called, This function will build on an AlertDialog.Builder instance and on add Habit
     * clicked in the fragment, it will store all values inputted by the user and performs checks
     * on the validity of some, and if passes, will return the builder
     *
     * @param builder
     *
     * @return {@link AlertDialog.Builder}
     */
    private Dialog addHabit(AlertDialog.Builder builder) {
        return builder
                .setView(view)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add habit", (dialogInterface, i) -> {

                    String title = habit_title.getText().toString();

                    Calendar date = Calendar.getInstance();
                    date.set(habit_starting_date.getYear(), habit_starting_date.getMonth(), habit_starting_date.getDayOfMonth());

                    String reason = habit_reason.getText().toString();

                    int[] week_freq = {0,0,0,0,0,0,0};

                    pickerSelectedDays = dayPicker.getSelectedDays();
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.SUNDAY)){
                        week_freq[0] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.MONDAY)){
                        week_freq[1] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.TUESDAY)){
                        week_freq[2] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.WEDNESDAY)){
                        week_freq[3] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.THURSDAY)){
                        week_freq[4] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.FRIDAY)){
                        week_freq[5] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.SATURDAY)){
                        week_freq[6] = 1;
                    }

                    Boolean switchState = publicSwitch.isChecked();
                    // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                    if(title.compareTo("") != 0 && reason.compareTo("") != 0 && !pickerSelectedDays.isEmpty()){
<<<<<<< HEAD
                        Habit newHabit = new Habit(title, reason, date, week_freq,true);
=======
                        Habit newHabit = new Habit(title, reason, date, week_freq,switchState);
>>>>>>> 14dfae456734c90f6813d92e07eebe4d17d9bab8
                        listener.onAddPressed(newHabit);
                    }
                    else{
                        Context context = getContext();
                        CharSequence text = "Invalid Entry, Try again";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }).create();
    }

    /**
     * When called, This function will build on an AlertDialog.Builder instance and initialize
     * all widgets necessary for editing to be the corresponding value given by the selectedHabit
     * On done editing clicked in the fragment, it will store all values inputted by the user and
     * perform checks on the validity of some, and if passes, will return the builder
     *
     * @param builder
     *
     * @return {@link AlertDialog.Builder}
     */
    private Dialog editHabit(Habit selectedHabit, AlertDialog.Builder builder) {
        habit_title.setText(selectedHabit.getTitle());
        habit_reason.setText(selectedHabit.getReason());

        Calendar selectedDate = selectedHabit.getDate();

        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DATE);


        habit_starting_date.updateDate(year,month,day);


        habit_starting_date.setMinDate(selectedDate.getTimeInMillis() - 1000);
        habit_starting_date.setMaxDate(selectedDate.getTimeInMillis());

        int[] week_freq = selectedHabit.getWeek_freq();
        /**
         * setting picker from weekly frequency
         **/

        if(week_freq[0] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.SUNDAY);
        }
        if(week_freq[1] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.MONDAY);
        }
        if(week_freq[2] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.TUESDAY);
        }
        if(week_freq[3] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.WEDNESDAY);
        }
        if(week_freq[4] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.THURSDAY);
        }
        if(week_freq[5] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.FRIDAY);
        }
        if(week_freq[6] == 1){
            pickerSelectedDays.add(MaterialDayPicker.Weekday.SATURDAY);
        }

        dayPicker.setSelectedDays(pickerSelectedDays);

        publicSwitch.setChecked(selectedHabit.getPublicHabit()); //will check the switch if the boolean value is true or false
        return builder
                .setView(view)
                .setTitle("View and Edit Habit")
                .setNegativeButton("Done viewing", null)
                .setPositiveButton("Done Editing", (dialogInterface, i) -> {

                    String title = habit_title.getText().toString();

                    String reason = habit_reason.getText().toString();
                    Calendar date = Calendar.getInstance();
                    date.set(habit_starting_date.getYear(), habit_starting_date.getMonth(), habit_starting_date.getDayOfMonth());

                    int[] freq = {0,0,0,0,0,0,0};

                    pickerSelectedDays = dayPicker.getSelectedDays();

                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.SUNDAY)){
                        freq[0] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.MONDAY)){
                        freq[1] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.TUESDAY)){
                        freq[2] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.WEDNESDAY)){
                        freq[3] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.THURSDAY)){
                        freq[4] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.FRIDAY)){
                        freq[5] = 1;
                    }
                    if(pickerSelectedDays.contains(MaterialDayPicker.Weekday.SATURDAY)){
                        freq[6] = 1;
                    }

                    Boolean switchState = publicSwitch.isChecked();
                    // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                    if(title.compareTo("") != 0 && reason.compareTo("") != 0 && !pickerSelectedDays.isEmpty()){

                        Habit newHabit = new Habit(title,reason,date,freq,true);

                        listener.onEditPressed(newHabit, selectedHabit);
                    }
                    else{
                        Context context = getContext();
                        CharSequence text = "Invalid Edit, Try again";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }).create();
    }
}

