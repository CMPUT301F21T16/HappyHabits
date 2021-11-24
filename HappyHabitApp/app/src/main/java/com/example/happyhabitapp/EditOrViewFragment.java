package com.example.happyhabitapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

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

    /**
     * Inflates the view of the fragment and gets buttons.
     */
    private void initFragment(LayoutInflater inflater) {

        view = inflater.from(getActivity()).inflate(R.layout.edit_or_view_fragment, null);
        eventsButton = view.findViewById(R.id.go_to_events_btn);
        editButton = view.findViewById(R.id.go_to_edit_btn);
    }

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
            eventsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent eventActivity = new Intent( getActivity() , HabitEventActivity.class);
                    startActivity(eventActivity);
                    getDialog().dismiss();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogFragment newFragment = new Add_Edit_Fragment();
                    Bundle args = new Bundle();
                    args.putSerializable("habit", habit);
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "Edit Habit");
                    getDialog().dismiss();
                }
            });
        }

        getDialog().dismiss();
        return view;
    }
}