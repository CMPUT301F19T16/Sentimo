package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.view.View;

import com.example.sentimo.Mood;
import com.example.sentimo.Situations.Situation;

import java.text.ParseException;

/**
 *
 *The EditMoodFragment class is a fragment that edits a Mood object that is then sent to
 *the main screen.
 *This class is a modified version of the ListyCity demo in Lab 3 and uses a lot of resources
 *from the AddCityFragment class.
 *I don't know who wrote the ListyCity demo, but the Android Studio .zip file is on eclass at this URL
 *Url == https://eclass.srv.ualberta.ca/course/view.php?id=54165
 */
public class EditMoodFragment extends ChangeMoodFragment {
    private int position;
    private Mood initialMood;
    private EditMoodListener listener;

    /**
     * Constructor to assign pre-existing mood and position in list
     * @param mood The original mood
     * @param position The position in the list
     */
    public EditMoodFragment(Mood mood, int position){
        this.position = position;
        this.initialMood = mood;
    }

    // Subclass UI initialization

    /**
     * Subclass specific initialization (required to ensure called after UI hookup)
     */
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
        if (initialMood.getLatitude() != null) {
            locationCheckBox.setChecked(true);
        } else locationCheckBox.setChecked(false);
        emojiImageButton.setText(initialMood.getEmotion().getName());
        emojiImageView.setImageResource(this.emotion.getImage());
        emojiImageButton.setVisibility(View.INVISIBLE);
        emojiImageView.setVisibility(View.VISIBLE);
        emojiImageView.setBackgroundColor(Color.parseColor(this.emotion.getColour()));
        emotion = initialMood.getEmotion();

        locationCheckBox.setEnabled(false);
    }

    // Subclass listener interfaces and methods

    /**
     * Listener for activity calling EditMoodFragment to receive a mood back
     */
    public interface EditMoodListener{
        void onConfirmEditPressed(Mood mood, int position);
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

    /**
     * Listener to return mood to parent activity
     * @param mood: the mood created by the ChangeMoodFragment
     */
    @Override
    public void callListener(Mood mood) {
        listener.onConfirmEditPressed(mood, position);
    }

    /**
     * Dialog builder specific to AddMoodFragment
     * @return the initialized AlertDialog.Builder
     */
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

    @Override
    protected Location subclassLocationReturnBehaviour() {
        Location newLocation = new Location("");
        newLocation.setLongitude(initialMood.getLongitude());
        newLocation.setLatitude(initialMood.getLatitude());
        return newLocation;
    }
}
