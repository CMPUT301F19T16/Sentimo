package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.view.View;

import com.example.sentimo.Mood;
import com.example.sentimo.R;
import com.example.sentimo.Situations.Situation;

/**
 * A class to display the most recent Moods of any followed friends who have approved
 * your follow request.
 */
public class FriendMoodDisplayFragment extends ChangeMoodFragment{

    public FriendMoodDisplayFragment(Mood mood) {
        this.initialMood = mood;
    }


    /**
     * Initialization behaviour
     * Disables interactable views, since fragment is display only
     */
    @Override
    protected void subclassInitialization() {
        emojiImageButton.setOnClickListener(null);
        emojiImageButton.setEnabled(false);
        emojiImageView.setOnClickListener(null);
        emojiImageButton.setEnabled(false);
        situationButton.setOnClickListener(null);
        situationButton.setEnabled(false);
        reasonImageButton.setOnClickListener(null);
        reasonImageButton.setEnabled(false);
        reasonEditText.setEnabled(false);
        dateTextView.setEnabled(false);
        timeTextView.setEnabled(false);


        dateTextView.setText(initialMood.getTime().getDateString());
        timeTextView.setText(initialMood.getTime().getTimeString());
        reasonEditText.setText(initialMood.getReason());
        Situation moodSituation = initialMood.getSituation();
        if (moodSituation != null) {
            situationButton.setText(moodSituation.getName());
        } else {
            situationButton.setText(R.string.no_situation_text);
        }
        if (initialMood.getLatitude() != null) {
            locationCheckBox.setChecked(true);
        } else locationCheckBox.setChecked(false);
        emojiImageButton.setText(initialMood.getEmotion().getName());
        emojiImageView.setImageResource(initialMood.getEmotion().getImage());
        emojiImageButton.setVisibility(View.INVISIBLE);
        emojiImageView.setVisibility(View.VISIBLE);
        emojiImageView.setBackgroundColor(Color.parseColor(initialMood.getEmotion().getColour()));

        locationCheckBox.setEnabled(false);
    }

    /**
     * Returns fragment builder for subclass
     * @return AlertDialog.builder for this subclass
     */
    @Override
    protected AlertDialog.Builder returnBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Recent Mood for " + initialMood.getUsername())
                .setNegativeButton("Back", null)
                // Positive Button behaviour set in super class to override default dismissal behaviour
                .setPositiveButton(getString(R.string.friend_mood_display_fragment_positive_text), null);
    }

    /**
     * No location behaviour required in FriendMoodDisplayFragment
     */
    @Override
    protected Location subclassLocationReturnBehaviour() {
        return null;
    }

    /**
     * No listener behaviour required in FriendMoodDisplayFragment
     */
    @Override
    protected void callListener(Mood mood) {
    }
}
