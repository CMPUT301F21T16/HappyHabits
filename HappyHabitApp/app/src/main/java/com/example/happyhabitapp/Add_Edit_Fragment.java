package com.example.happyhabitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class Add_Edit_Fragment extends DialogFragment {

    private EditText habit_title;
    private DatePicker habit_starting_date;
    private EditText habit_reason;
    private RadioGroup habit_freq;
    private onFragmentInteractionListener listener;


    public interface onFragmentInteractionListener{
        void onAddPressed(Habit newHabit);
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

    public static Add_Edit_Fragment newInstance(Habit habit){
        Bundle args = new Bundle();
        args.putSerializable("habit",habit);

        Add_Edit_Fragment fragment = new Add_Edit_Fragment();
        fragment.setArguments(args);
        return fragment;
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
        habit_freq = view.findViewById(R.id.group_radio_units);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Add Medicine")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add Medicine", (dialogInterface, i) -> {

                    String title = habit_title.getText().toString();

                    Calendar date = Calendar.getInstance();
                    date.set(habit_starting_date.getYear(), habit_starting_date.getMonth(), habit_starting_date.getDayOfMonth());

                    String reason = habit_reason.getText().toString();

                    int selectedId = habit_freq.getCheckedRadioButtonId();
                    //mg is a default selection of unit
                    String freq = "Sun";
                    if (selectedId == R.id.freq_Mon) {
                        freq = "Mon";
                    } else if (selectedId == R.id.freq_Tue) {
                        freq = "Tue";
                    } else if (selectedId == R.id.freq_Wed) {
                        freq = "Wed";
                    } else if (selectedId == R.id.freq_Thu) {
                        freq = "Thr";
                    } else if (selectedId == R.id.freq_Fri) {
                        freq = "Fri";
                    } else if (selectedId == R.id.freq_Sat) {
                        freq = "Sat";
                    }

                    // If statement checks if the values inputted are not empty. Date and unit has default options so those are not checked
                    if(title.compareTo("") != 0 && reason.compareTo("") != 0){
                        Habit newHabit = new Habit(title, reason, date, freq);
                        listener.onAddPressed(newHabit);
                    }
                }).create();
    }
}
