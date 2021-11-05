package com.example.happyhabitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;


public class Add_Edit_Fragment extends DialogFragment {

    private EditText habit_title;
    private DatePicker habit_starting_date;
    private EditText habit_reason;
    private List<MaterialDayPicker.Weekday> pickerSelectedDays = new ArrayList<>();
    private MaterialDayPicker dayPicker;
    private onFragmentInteractionListener listener;
    private View view;

    static Add_Edit_Fragment newInstance(Habit habit){
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);

        Add_Edit_Fragment fragment = new Add_Edit_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface onFragmentInteractionListener{
        void onAddPressed(Habit newHabit);
        void onEditPressed(Habit newHabit, Habit oldHabit);
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
    private void initFragment() {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_habit_fragment_layout, null);
        habit_title = view.findViewById(R.id.habit_title_editText);
        habit_reason = view.findViewById(R.id.habit_reason_editText);
        habit_starting_date = view.findViewById(R.id.habit_starting_date);
        dayPicker = view.findViewById(R.id.day_picker);

    }
    /**
     * initialize the EditTexts, DatePicker, and checkboxes. Create a Dialog Fragment from add_edit_habit_fragment_layout.xml
     *  and on Add habit click, store all values in a new habit Object and return the Dialog Fragment instance
     *
     * @param savedInstanceState
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        initFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.Theme_AddEditFragment);
        Habit selectedHabit = (Habit) getArguments().getSerializable("habit");

        if(selectedHabit != null) { //if edit habit was clicked
            //editing the Habit
            return editHabit(selectedHabit, builder);
        } else { //if add habit was clicked
            return addHabit(builder);
        }
    }

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
                    // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                    if(title.compareTo("") != 0 && reason.compareTo("") != 0 && !pickerSelectedDays.isEmpty()){
                        Habit newHabit = new Habit(title, reason, date, week_freq);
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

                    // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                    if(title.compareTo("") != 0 && reason.compareTo("") != 0 && !pickerSelectedDays.isEmpty()){
                        Habit newHabit = new Habit(title,reason,date,freq);
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

