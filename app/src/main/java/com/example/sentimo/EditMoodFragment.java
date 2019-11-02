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

import java.text.ParseException;

public class EditMoodFragment extends ChangeMoodFragment {
    private int position;
    private Mood initialMood;
    private EditMoodListener listener;

    // Subclass UI initialization

    @Override
    protected void subclassInitialization() {
        this.emotion = initialMood.getEmotion();
        this.situation = initialMood.getSituation();

        dateTextView.setText(initialMood.getTime().getDateString());
        timeTextView.setText(initialMood.getTime().getTimeString());
        reasonEditText.setText(initialMood.getReason());
        Situation moodSituation = initialMood.getSituation();
        if (moodSituation != null) {
            situationButton.setText(moodSituation.getName());
        } else {
            situationButton.setText("(Optional)");
        }
        locationCheckBox.setChecked(initialMood.getLocationPermission());
        emojiImageButton.setText(initialMood.getEmotion().getName());
        emotion = initialMood.getEmotion();

    }

    // Subclass listener interfaces and methods

    public interface EditMoodListener{
        void onConfirmEditPressed(Mood mood, int position);
    }

    public EditMoodFragment(Mood mood, int position){
        this.position = position;
        this.initialMood = mood;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof EditMoodListener){
            listener = (EditMoodListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentListener");
        }
    }

    @Override
    public void callListener(Mood mood) {
        listener.onConfirmEditPressed(mood, position);
    }



    // Alert dialog builder method

    @Override
    protected AlertDialog.Builder returnBuilder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Mood")
                .setNegativeButton("Back", null)
                .setPositiveButton("Confirm Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = dateTextView.getText().toString();
                        String time = timeTextView.getText().toString();
                        String reason = reasonEditText.getText().toString();
                        Boolean locationPermission = locationCheckBox.isChecked();

                        // date string to formatted Date
                        TimeFormatter timef = new TimeFormatter();
                        try {
                            timef.setTimeFormat(date, time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Mood mood = new Mood(timef, EditMoodFragment.this.emotion, reason, EditMoodFragment.this.situation, locationPermission);
                        listener.onConfirmEditPressed(mood, position);
                    }
                });

    }

}
