package com.example.sentimo.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.InputErrorType;
import com.example.sentimo.Mood;
import com.example.sentimo.R;
import com.example.sentimo.Situations.Situation;
import com.example.sentimo.TimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.text.ParseException;

public abstract class ChangeMoodFragment extends DialogFragment implements SelectSituationFragment.SelectSituationListener, SelectMoodFragment.SelectMoodFragmentInteractionListener {
    protected TextView dateTextView;
    protected TextView timeTextView;
    protected ImageView emojiImageView;
    protected Button emojiImageButton;
    protected EditText reasonEditText;
    protected Button reasonImageButton;
    protected ImageView reasonImageView;
    protected Button situationButton;
    protected CheckBox locationCheckBox;
    protected Emotion emotion;
    protected Situation situation;
    protected Uri localPathToImage;
    protected String onlinePathToImage;


    final int SUCCESSFUL_PICTURE_RETURN = 71;


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
                InputErrorType errorCode = isDataValid();
                if (errorCode != InputErrorType.DataValid) {
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
                Location location = null;
                location = subclassLocationReturnBehaviour();
//              location = ((EditMoodFragment)ChangeMoodFragment.this).initialMood.getLocation();
                if (locationCheckBox.isChecked()) {
                    location = subclassLocationReturnBehaviour();
                }
                if (location != null) {
                    Log.d("LATITUDE", Double.toString(location.getLatitude()));
                    Log.d("LONGITUDE", Double.toString(location.getLongitude()));
                    Log.d("PHOTO", localPathToImage.toString());
                } else {
                    Log.d("LATLONG", "No Location");
                }
                // Need to add if statements for null date, time, or emotion
                Mood myMood = new Mood(timef, ChangeMoodFragment.this.emotion, reason, ChangeMoodFragment.this.situation, location.getLongitude(), location.getLatitude(), localPathToImage.toString(), onlinePathToImage);
                callListener(myMood);
                ChangeMoodFragment.this.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setId(R.id.change_mood_fragment_positive_button);

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
        reasonImageButton = view.findViewById(R.id.reason_image_button);
        reasonImageView = view.findViewById(R.id.reason_image);
        situationButton = view.findViewById(R.id.situation_button);
//        situationTextView = view.findViewById(R.id.situation_text);
        locationCheckBox = view.findViewById(R.id.location_checkbox);


        emojiImageButton.setOnClickListener(emotionClick);
        emojiImageView.setOnClickListener(emotionClick);

        situationButton.setOnClickListener(situationClick);

        reasonImageButton = view.findViewById(R.id.reason_image_button);
        reasonImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inspired by https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Photograph for Reason"), SUCCESSFUL_PICTURE_RETURN);
            }
        });

        localPathToImage = null;
        onlinePathToImage = null;

    }

    /**
     * Displays a warning for invalid date of type specified by warningType
     * @param warningType the code for the type of warning to display
     */
    public void displayWarning(InputErrorType warningType) {
        new InvalidDataWarningFragment(warningType).show(getChildFragmentManager(), null);
    }

    /**
     * Checks if data is valid, returns and appropriate data invalid code if not
     * @return integer code indication type of data invalidity, or 0 if data valid
     */
    public InputErrorType isDataValid() {
        if (!(ChangeMoodFragment.this.emotion != null)) {
            return InputErrorType.CMFNullMoodError;
        }
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();
        TimeFormatter timef = new TimeFormatter();
        try {
            timef.setTimeFormat(date, time);
        } catch (ParseException e) {
            return InputErrorType.CMFTimeParseError;
        }
        String reason = reasonEditText.getText().toString();
        if (reason.length() > 20) {
            return InputErrorType.CMFReasonTooLongError;
        }
        int spaceCount = 0;
        for (int i = 0; i < reason.length(); i++) {
            if (reason.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        if (spaceCount > 2) {
            return InputErrorType.CMFReasonTooManyWordsError;
        }
        if (locationCheckBox.isChecked() & ChangeMoodFragment.this instanceof AddMoodFragment) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return InputErrorType.CMFNoLocationPermission;
            }

        }
        return InputErrorType.DataValid;
    }

    // Inspired by https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUCCESSFUL_PICTURE_RETURN && resultCode == -1 && data != null && data.getData() != null ) {
            localPathToImage = data.getData();
        }
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


    // Should get rid of method, no longer use Location
    /**
     * Returns the location for the mood to be created
     * @return
     */
    protected abstract Location subclassLocationReturnBehaviour();
}
