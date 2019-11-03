package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.sentimo.Mood;
import com.example.sentimo.Situations.Situation;

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
                // Positive Button behaviour set in super class to override default dismissal behaviour
                .setPositiveButton("Confirm Edit", null);
    }

}
