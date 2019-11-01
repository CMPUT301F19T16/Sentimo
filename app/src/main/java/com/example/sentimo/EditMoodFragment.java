package com.example.sentimo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditMoodFragment extends DialogFragment implements SelectMoodFragment.OnFragmentInteractionListener, SelectSituationFragment.SelectSituationListener {

    private int position;
    private Mood mood;
    private OnFragmentInteractionListener listener;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText reasonEditText;
    private Button emojiButton;
    private Button situationButton;
    private CheckBox locationCheckBox;
    private Emotion emotion;

    public interface OnFragmentInteractionListener{
        void onConfirmEditPressed(Mood mood, int position);
    }

    public EditMoodFragment(Mood mood, int position){
        this.position = position;
        this.mood = mood;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_mood_fragment, null);

        dateEditText = view.findViewById(R.id.edit_date);
        timeEditText = view.findViewById(R.id.edit_time);
        reasonEditText = view.findViewById(R.id.edit_reason_editText);
        situationButton = view.findViewById(R.id.edit_situation_button);
        locationCheckBox = view.findViewById(R.id.edit_location_checkbox);
        emojiButton = view.findViewById(R.id.edit_emotion_button);

        dateEditText.setText(mood.getDate());
        timeEditText.setText(mood.getTime());
        reasonEditText.setText(mood.getReason());
        situationButton.setText(mood.getSituation());
        locationCheckBox.setChecked(mood.getLocationPermission());
        emojiButton.setText(mood.getEmotion().getName());
        emotion = mood.getEmotion();

        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectMoodFragment().show(getChildFragmentManager(), "SELECT_MOOD");
            }
        });

        situationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectSituationFragment().show(getChildFragmentManager(), "SELECT_SITUATION");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Mood")
                .setNegativeButton("Back", null)
                .setPositiveButton("Confirm Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = dateEditText.getText().toString();
                        String time = timeEditText.getText().toString();
                        String reason = reasonEditText.getText().toString();
                        String situation = situationButton.getText().toString();
                        Boolean locationPermission = locationCheckBox.isChecked();
                        Mood mood = new Mood(date, time, emotion, reason, situation, locationPermission);
                        listener.onConfirmEditPressed(mood, position);
                    }
                }).create();
    }


    @Override
    public void onDonePressed(Emotion emotion){
        if (emotion != null){
            this.emotion = emotion;
            emojiButton.setText(emotion.getName());
        }
    }

    @Override
    public void situationReturned(String string) {
        if (string != null) {
            situationButton.setText(string);
        }
    }
}
