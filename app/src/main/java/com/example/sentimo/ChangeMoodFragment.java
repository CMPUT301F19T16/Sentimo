package com.example.sentimo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class ChangeMoodFragment extends DialogFragment implements SelectSituationFragment.SelectSituationListener, SelectMoodFragment.SelectMoodFragmentInteractionListener {
    protected TextView dateTextView;
    protected TextView timeTextView;
    //private Image emojiImageView;
    protected Button emojiImageButton;
    protected EditText reasonEditText;
    protected Button reasonImageButton;
    //private Image reasonImageView;
    protected Button situationButton;
    protected TextView situationTextView;
    protected CheckBox locationCheckBox;
    protected Emotion emotion;
    protected Situation situation;

    protected View.OnClickListener emotionClick;
    protected View.OnClickListener situationClick;
    protected View view;


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
        return builder.create();
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
        if (situation != null) {
            this.situation = situation;
            situationButton.setText(situation.getName());
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
        //emojiImageView = view.findViewById(R.id.add_emotion_image);
        emojiImageButton = view.findViewById(R.id.emotion_button);
        reasonEditText = view.findViewById(R.id.reason_editText);
        reasonImageButton = view.findViewById(R.id.reason_button);
        //reasonImageView = view.findViewById(R.id.add_reason_image);
        situationButton = view.findViewById(R.id.situation_button);
        situationTextView = view.findViewById(R.id.situation_text);
        locationCheckBox = view.findViewById(R.id.location_checkbox);

        emojiImageButton.setOnClickListener(emotionClick);

        situationButton.setOnClickListener(situationClick);
    }

    protected abstract void subclassInitialization();

    protected abstract void callListener(Mood mood);

    protected abstract AlertDialog.Builder returnBuilder();
}
