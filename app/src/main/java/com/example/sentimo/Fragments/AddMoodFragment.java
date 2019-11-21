package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import com.example.sentimo.Mood;
import com.example.sentimo.TimeFormatter;

import java.util.Calendar;
import java.util.Date;


/**
 *
 *The AddMoodFragment class is a fragment that creates a Mood object that is then sent to
 *the main screen.
 *This class is a modified version of the ListyCity demo in Lab 3 and uses a lot of resources
 *from the AddCityFragment class.
 *I don't know who wrote the ListyCity demo, but the Android Studio .zip file is on eclass at this URL
 *Url == https://eclass.srv.ualberta.ca/course/view.php?id=54165
 */
public class AddMoodFragment extends ChangeMoodFragment {
    private AddMoodListener listener;


    /**
     * Subclass specific initialization (required to ensure called after UI hookup)
     */
    @Override
    protected void subclassInitialization() {
        this.emotion = null;
        this.situation = null;
        emojiImageButton.setVisibility(View.VISIBLE);
        emojiImageView.setVisibility(View.INVISIBLE);

        TimeFormatter timef = new TimeFormatter();
        Date time = new Date();
        time.setTime(Calendar.getInstance().getTimeInMillis());
        timef.setTime(time);
        dateTextView.setText(timef.getDateString());
        timeTextView.setText(timef.getTimeString());

    }

    /**
     * Listener for activity calling AddMoodFragment to receive a mood back
     */
    public interface AddMoodListener {
        void onDonePressed(Mood newMood);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Attaching AddMoodListener from parent Activity
        if (context instanceof AddMoodListener) {
            this.listener = (AddMoodFragment.AddMoodListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement AddMoodListener");
        }
    }

    /**
     * Listener to return mood to parent activity
     * @param mood: the mood created by the ChangeMoodFragment
     */
    @Override
    public void callListener(Mood mood) {
        listener.onDonePressed(mood);
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
                .setTitle("Add Mood")
                .setNegativeButton("Back", null)
                // Positive Button behaviour set in super class to override default dismissal behaviour
                .setPositiveButton("Done", null);
    }

}
