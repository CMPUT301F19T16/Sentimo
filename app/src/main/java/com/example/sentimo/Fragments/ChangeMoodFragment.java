package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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


    // Method for reassigning positive button clicker found from
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
                        if (!isDataValid()) {
                            displayWarning();
                            return;
                        }
                        String reason = reasonEditText.getText().toString();
                        Boolean location = locationCheckBox.isChecked();
                        // Need to add if statements for null date, time, or emotion
                        Mood myMood = new Mood(date, time, ChangeMoodFragment.this.emotion, reason, ChangeMoodFragment.this.situation, location);
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

        situationButton.setOnClickListener(situationClick);
    }

    public void displayWarning() {
        new InvalidDataWarningFragment().show(getChildFragmentManager(), null);
    }

    public boolean isDataValid() {
        if (ChangeMoodFragment.this.emotion != null) {
            return true;
        }
        return false;
    }

    protected abstract void subclassInitialization();

    protected abstract void callListener(Mood mood);

    protected abstract AlertDialog.Builder returnBuilder();
}
