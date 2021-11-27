package com.example.happyhabitapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
public class EditOrViewFragment extends DialogFragment {

    private View view;
    private Button eventsButton;
    private Button editButton;
    private Habit habit;
    private onFragmentInteractionListener listener;

    /**
     * Initializes the fragment
     * Inflates the view of the fragment and gets buttons.
     */
    private void initFragment(LayoutInflater inflater) {

        view = inflater.from(getActivity()).inflate(R.layout.edit_or_view_fragment, null);
        eventsButton = view.findViewById(R.id.go_to_events_btn);
        editButton = view.findViewById(R.id.go_to_edit_btn);
    }

    /**
     * Controls the actions taken on the creation of the Dialog Fragmment
     * Handles possible button interactions when fragment is creatd
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // if a argument is failed then dismiss the dialog immediately
        // an argument should always be passed
        if (getArguments() == null) {
           getDialog().dismiss();;
        }

        else {
            // initialize all the views
            initFragment(inflater);

            // get passed in habit
            habit = (Habit) getArguments().getSerializable("habit");

            // functionality for the go to events button
            // starts the HabitEventActivity activity
            eventsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goToEvents(habit);
                    getDialog().dismiss();
                }
            });

            // functionality for the edit button
            // enables user to edit an existing Habit
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goToEdit(habit);
                    getDialog().dismiss();
                }
            });
        }
        return view;
    }

    public interface onFragmentInteractionListener{
        void goToEdit(Habit habit);
        void goToEvents(Habit habit);
    }


    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof onFragmentInteractionListener){
            listener = (onFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "must implement onFragmentInteractionListener");
        }

    }
}