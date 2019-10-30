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

// The AddMoodFragment class is a fragment that creates a Mood object that is then displayed on
// the main screen.

//// This class is a modified version of the ListyCity demo in Lab 3 and uses a lot of resources
//// from the AddCityFragment class.
//// I don't know who wrote the ListyCity demo, but the Android Studio .zip file is on eclass at this URL
//// Url == https://eclass.srv.ualberta.ca/course/view.php?id=54165
public class AddMoodFragment extends DialogFragment {
    private TextView dateTextView;
    private TextView timeTextView;
    //private Image emojiImageView;
    private Button emojiImageButton;
    private EditText reasonEditText;
    private Button reasonImageButton;
    //private Image reasonImageView;
    private Button situationButton;
    private TextView situationTextView;
    private CheckBox locationCheckBox;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onDonePressed(Mood newMood);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            this.listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_mood_fragment, null);

        dateTextView = view.findViewById(R.id.add_date);
        timeTextView = view.findViewById(R.id.add_time);
        //emojiImageView = view.findViewById(R.id.add_emotion_image);
        emojiImageButton = view.findViewById(R.id.add_emotion_button);
        reasonEditText = view.findViewById(R.id.add_reason_editText);
        reasonImageButton = view.findViewById(R.id.add_reason_image_button);
        //reasonImageView = view.findViewById(R.id.add_reason_image);
        situationButton = view.findViewById(R.id.add_situation_button);
        situationTextView = view.findViewById(R.id.add_situation_situation);
        locationCheckBox = view.findViewById(R.id.add_location_checkbox);

        // Should add TextWatchers

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Mood")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = dateTextView.getText().toString();
                        String time = timeTextView.getText().toString();
                        Emotion emotion = new Worried(); // Temp until emotion is implemented
                        String reason = reasonEditText.getText().toString();
                        String situation = situationTextView.getText().toString();
                        Boolean location = locationCheckBox.isChecked();
                        // Need to add if statements for null date, time, or emotion
                        listener.onDonePressed(new Mood(date, time, emotion, reason, situation, location));
                    }
                }).create();


    }
}
