package com.example.happyhabitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;


public class Add_Edit_Fragment extends DialogFragment {

    private EditText habit_title;
    private DatePicker habit_starting_date;
    private EditText habit_reason;
    private CheckBox sun,mon,tue,wed,thr,fri,sat;
    private onFragmentInteractionListener listener;

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

    /**
     * initialize the EditTexts, DatePicker, and RadioGroup. Create a Dialog Fragment from add_medicine_fragment_layout.xml
     *  and on Add Medicine click, store all values in a new Medicine Object and return the Dialog Fragment instance
     *
     * @param savedInstanceState
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_habit_fragment_layout, null);
        habit_title = view.findViewById(R.id.habit_title_editText);
        habit_reason = view.findViewById(R.id.habit_reason_editText);
        habit_starting_date = view.findViewById(R.id.habit_starting_date);
        sun = view.findViewById(R.id.SunCheckBox);
        mon = view.findViewById(R.id.MonCheckBox);
        tue = view.findViewById(R.id.TueCheckBox);
        wed = view.findViewById(R.id.WedCheckBox);
        thr = view.findViewById(R.id.ThrCheckBox);
        fri = view.findViewById(R.id.FriCheckBox);
        sat = view.findViewById(R.id.SatCheckBox);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if(getArguments().getSerializable("habit") != null) { //if edit habit was clicked
            Habit habit = (Habit) getArguments().getSerializable("habit");

            habit_title.setText(habit.getTitle());
            habit_reason.setText(habit.getReason());

            Calendar selectedDate = habit.getDate();

            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int day = selectedDate.get(Calendar.DATE);

            habit_starting_date.updateDate(year,month,day);


            habit_starting_date.setMinDate(selectedDate.getTimeInMillis());
            habit_starting_date.setMaxDate(selectedDate.getTimeInMillis());

            int[] week_freq = habit.getWeek_freq();

            if(week_freq[0] == 1){
                sun.setChecked(true);
            }
            if(week_freq[1] == 1){
                mon.setChecked(true);
            }
            if(week_freq[2] == 1){
                tue.setChecked(true);
            }
            if(week_freq[3] == 1){
                wed.setChecked(true);
            }
            if(week_freq[4] == 1){
                thr.setChecked(true);
            }
            if(week_freq[5] == 1){
                fri.setChecked(true);
            }
            if(week_freq[6] == 1){
                sat.setChecked(true);
            }

            //editing the Habit
            return builder
                    .setView(view)
                    .setTitle("View/ Edit Habit")
                    .setNegativeButton("Done viewing", null)
                    .setPositiveButton("Edit habit", (dialogInterface, i) -> {

                        String title = habit_title.getText().toString();

                        String reason = habit_reason.getText().toString();
                        Calendar date = Calendar.getInstance();
                        date.set(habit_starting_date.getYear(), habit_starting_date.getMonth(), habit_starting_date.getDayOfMonth());

                        int[] freq = {1,0,0,0,0,0,0};

                        if(sun.isChecked()){
                            freq[0] = 1;
                        }
                        if(mon.isChecked()){
                            freq[1] = 1;
                        }
                        if(tue.isChecked()){
                            freq[2] = 1;
                        }
                        if(wed.isChecked()){
                            freq[3] = 1;
                        }
                        if(thr.isChecked()){
                            freq[4] = 1;
                        }
                        if(fri.isChecked()){
                            freq[5] = 1;
                        }
                        if(sat.isChecked()){
                            freq[6] = 1;
                        }

                        // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                        if(title.compareTo("") != 0 && reason.compareTo("") != 0){
                            Habit newHabit = new Habit(title,reason,date,week_freq);
                            listener.onEditPressed(newHabit, habit);
                        }
                    }).create();
        }


        //if add habit was clicked
        return builder
                .setView(view)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add habit", (dialogInterface, i) -> {

                    String title = habit_title.getText().toString();

                    Calendar date = Calendar.getInstance();
                    date.set(habit_starting_date.getYear(), habit_starting_date.getMonth(), habit_starting_date.getDayOfMonth());

                    String reason = habit_reason.getText().toString();

                    int[] week_freq = {1,0,0,0,0,0,0};

                    if(sun.isChecked()){
                        week_freq[0] = 1;
                    }
                    if(mon.isChecked()){
                        week_freq[1] = 1;
                    }
                    if(tue.isChecked()){
                        week_freq[2] = 1;
                    }
                    if(wed.isChecked()){
                        week_freq[3] = 1;
                    }
                    if(thr.isChecked()){
                        week_freq[4] = 1;
                    }
                    if(fri.isChecked()){
                        week_freq[5] = 1;
                    }
                    if(sat.isChecked()){
                        week_freq[6] = 1;
                    }
                    // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                    if(title.compareTo("") != 0 && reason.compareTo("") != 0){
                        Habit newHabit = new Habit(title, reason, date, week_freq);
                        listener.onAddPressed(newHabit);
                    }
                }).create();
    }
}
