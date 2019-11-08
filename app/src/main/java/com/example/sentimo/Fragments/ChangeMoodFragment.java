package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.media.Image;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Mood;
import com.example.sentimo.R;
import com.example.sentimo.Situations.Situation;
import com.example.sentimo.TimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public abstract class ChangeMoodFragment extends DialogFragment implements SelectSituationFragment.SelectSituationListener, SelectMoodFragment.SelectMoodFragmentInteractionListener {
    protected TextView dateTextView;
    protected TextView timeTextView;
    protected ImageView emojiImageView;
    protected Button emojiImageButton;
    protected EditText reasonEditText;
    protected Button reasonImageButton;
    protected ImageView reasonImageView;
    protected Button situationButton;
//    protected TextView situationTextView;
    protected CheckBox locationCheckBox;
    protected Emotion emotion;
    protected Situation situation;

    protected View.OnClickListener emotionClick;
    protected View.OnClickListener situationClick;
    protected View view;


    // Method for reassigning positive button clicker to avoid automatic dismissal found at
    // StackOverflow post:https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        // Should add TextWatchers

        emotionClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectMoodFragment().show(getChildFragmentManager(), "SELECT_MOOD");
            }
        };

        situationClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectSituationFragment().show(getChildFragmentManager(), "SELECT_SITUATION");
            }
        };

        sharedInitialization();

        subclassInitialization();

        AlertDialog.Builder builder = returnBuilder();
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = dateTextView.getText().toString();
                        String time = timeTextView.getText().toString();
//                        String emotionText = emojiImageButton.getText().toString();
                        int errorCode = isDataValid();
                        if (errorCode != 0) {
                            displayWarning(errorCode);
                            return;
                        }
                        TimeFormatter timef = new TimeFormatter();
                        try {
                            timef.setTimeFormat(date, time);
                        } catch (ParseException e) {
                            return;
                        }
                        String reason = reasonEditText.getText().toString();
                        Boolean location = locationCheckBox.isChecked();
                        // Need to add if statements for null date, time, or emotion
                        Mood myMood = new Mood(timef, ChangeMoodFragment.this.emotion, reason, ChangeMoodFragment.this.situation, location);
                        callListener(myMood);
                        ChangeMoodFragment.this.dismiss();
                    }
                });
        return dialog;
    }

    @Override
    public void MoodReturned(Emotion emotion) {
        if (emotion != null) {
            this.emotion = emotion;
            emojiImageButton.setText(this.emotion.getName());
            emojiImageButton.setVisibility(View.INVISIBLE);
            emojiImageView.setVisibility(View.VISIBLE);
            emojiImageView.setImageResource(emotion.getImage());
            emojiImageView.setBackgroundColor(Color.parseColor(emotion.getColour()));
        }
    }

    @Override
    public void SituationReturned(Situation situation) {
        this.situation = situation;
        if (situation != null) {
            situationButton.setText(situation.getName());
        } else {
            situationButton.setText("(Optional)");
        }
    }



    /**
     *Shared initialization between subclasses
     *Separate non-constructor function required to allow hookup of UI before initialization
     */
    private void sharedInitialization() {
        View view = null;
        if (ChangeMoodFragment.this instanceof AddMoodFragment) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.add_mood_fragment, null);
        } else if (ChangeMoodFragment.this instanceof EditMoodFragment) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_mood_fragment, null);
        } else {
            throw new RuntimeException("CHANGE MOOD FRAGMENT RECEIVED UNKNOWN SUBCLASS");
        }

        this.view = view;

        dateTextView = view.findViewById(R.id.date_text);
        timeTextView = view.findViewById(R.id.time_text);
        emojiImageView = view.findViewById(R.id.emotion_image);
        emojiImageButton = view.findViewById(R.id.emotion_button);
        reasonEditText = view.findViewById(R.id.reason_editText);
        reasonImageButton = view.findViewById(R.id.reason_button);
        reasonImageView = view.findViewById(R.id.reason_image);
        situationButton = view.findViewById(R.id.situation_button);
//        situationTextView = view.findViewById(R.id.situation_text);
        locationCheckBox = view.findViewById(R.id.location_checkbox);


        emojiImageButton.setOnClickListener(emotionClick);
        emojiImageView.setOnClickListener(emotionClick);

        situationButton.setOnClickListener(situationClick);

    }

    /**
     * Displays a warning for invalid date of type specified by warningType
     * @param warningType the code for the type of warning to display
     */
    public void displayWarning(int warningType) {
        new InvalidDataWarningFragment(warningType).show(getChildFragmentManager(), null);
    }

    /**
     * Checks if data is valid, returns and appropriate data invalid code if not
     * @return integer code indication type of data invalidity, or 0 if data valid
     */
    public int isDataValid() {
        if (!(ChangeMoodFragment.this.emotion != null)) {
            return 1;
        }
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();
        TimeFormatter timef = new TimeFormatter();
        try {
            timef.setTimeFormat(date, time);
        } catch (ParseException e) {
            return 2;
        }
        String reason = reasonEditText.getText().toString();
        if (reason.length() > 20) {
            return 3;
        }
        int spaceCount = 0;
        for (int i = 0; i < reason.length(); i++) {
            if (reason.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        if (spaceCount > 2) {
            return 4;
        }
        return 0;
    }

    /**
     * Initialization specific to subclass
     * Require separate function rather than constructor for ordering reasons
     */
    protected abstract void subclassInitialization();

    /**
     * Listener for dismissing back to activity with a created Mood
     * @param mood: the mood created by the ChangeMoodFragment
     */
    protected abstract void callListener(Mood mood);

    /**
     * Returns a builder for the AlertDialog specific to the subclass
     * @return
     */
    protected abstract AlertDialog.Builder returnBuilder();
}
