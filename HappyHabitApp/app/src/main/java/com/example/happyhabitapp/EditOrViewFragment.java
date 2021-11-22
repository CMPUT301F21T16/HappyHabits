package com.example.happyhabitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A dialog fragment that extends a dialog fragment.
 * Used as an intermediary between the habit listings
 * and the editing/viewing of said habits
 * Use the {@link EditOrViewFragment} factory method to
 * create an instance of this fragment.
 */
public class EditOrViewFragment extends Fragment {

    private View view;

    private Button eventsButton;
    private Button editButton;

    /**
     * Inflates the view of the fragment and gets buttons.
     */
    private void initFragment() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_or_view_fragment, null);

        eventsButton = view.findViewById(R.id.go_to_events_btn);
        editButton = view.findViewById(R.id.go_to_edit_btn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Habit habit = (Habit) getArguments().getSerializable("habit");   //To be passed to next fragment, or discarded
            initFragment();

            eventsButton.setOnClickListener((View v) -> {
                //Launch a new activity, passing the habit to the activity
            });

            editButton.setOnClickListener((View v) -> {
                //Replace this fragment with the add_edit fragment, passing over the habit.
            });
        }
    }
}