package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Emotions.EmotionType;
import com.example.sentimo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Class that represents the fragment that shows up after the user presses
 * on the Button/ImageButton in the add/edit mood fragment.
 * Closes automatically when the user presses on one of the Emotion buttons.
 */
public class SelectMoodFragment extends DialogFragment {
    SelectMoodFragmentInteractionListener listener;
    Emotion emotion;
    Button happyButton;
    Button sadButton;
    Button madButton;
    Button lovedButton;
    Button embarassedButton;
    Button tiredButton;
    Button confidentButton;
    Button worriedButton;
    View.OnClickListener buttonListener;
    View view;

    /**
     * This is a function that represents what happens when the fragment is
     * created.
     * Attaches all the buttons from the xml file and closes the fragment
     * when the user presses on an emotion button or the cancel button.
     * @param savedInstanceState
     *      This is the state of the add/edit mood fragment that calls this
     *      fragment.
     * @return
     *      Returns a builder that allows the user to pick an emotion or cancel
     *      out of the fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.select_emoji, null);

        happyButton = view.findViewById(R.id.happyButton);
        sadButton = view.findViewById(R.id.sadButton);
        madButton = view.findViewById(R.id.madButton);
        lovedButton = view.findViewById(R.id.lovedButton);
        embarassedButton = view.findViewById(R.id.embarassedButton);
        tiredButton = view.findViewById(R.id.tiredButton);
        confidentButton = view.findViewById(R.id.confidentButton);
        worriedButton = view.findViewById(R.id.worriedButton);

        buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.happyButton:
                        emotion = new Emotion(EmotionType.Happy);
                        break;
                    case R.id.sadButton:
                        emotion = new Emotion(EmotionType.Sad);
                        break;
                    case R.id.madButton:
                        emotion = new Emotion(EmotionType.Mad);
                        break;
                    case R.id.lovedButton:
                        emotion = new Emotion(EmotionType.Loved);
                        break;
                    case R.id.embarassedButton:
                        emotion = new Emotion(EmotionType.Embarassed);
                        break;
                    case R.id.tiredButton:
                        emotion = new Emotion(EmotionType.Tired);
                        break;
                    case R.id.confidentButton:
                        emotion = new Emotion(EmotionType.Confident);
                        break;
                    case R.id.worriedButton:
                        emotion = new Emotion(EmotionType.Worried);
                        break;
                    default:
                        throw new RuntimeException("Unknown emotion case");
                }
                listener.EmotionReturned(SelectMoodFragment.this.emotion);
                SelectMoodFragment.this.dismiss();
            }
        };

        happyButton.setOnClickListener(buttonListener);
        sadButton.setOnClickListener(buttonListener);
        madButton.setOnClickListener(buttonListener);
        lovedButton.setOnClickListener(buttonListener);
        embarassedButton.setOnClickListener(buttonListener);
        tiredButton.setOnClickListener(buttonListener);
        confidentButton.setOnClickListener(buttonListener);
        worriedButton.setOnClickListener(buttonListener);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Mood")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.EmotionReturned(null);
                    }
                }).create();
    }

    /**
     * Tells the Dialog fragment what is the context of this fragment and
     * what is the listener of the fragment. In this case, it would either be
     * the add mood fragment or the edit mood fragment.
     * @param context
     *      Context object that represents the state and attributes of the
     *      listener and the fragment.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SelectMoodFragmentInteractionListener)getParentFragment();
    }

    /**
     * This is an interface that is implemented by the listener of this fragment,
     * either the add mood fragment or the edit mood fragment. It makes sure that
     * the listener of this fragment parses the emotion correctly and takes
     * appropriate action.
     */
    public interface SelectMoodFragmentInteractionListener {
        void EmotionReturned(Emotion emotion);
    }

}
