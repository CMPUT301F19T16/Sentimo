package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sentimo.Emotions.Confident;
import com.example.sentimo.Emotions.Embarrassed;
import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Emotions.Happy;
import com.example.sentimo.Emotions.Loved;
import com.example.sentimo.Emotions.Mad;
import com.example.sentimo.Emotions.Sad;
import com.example.sentimo.Emotions.Tired;
import com.example.sentimo.Emotions.Worried;
import com.example.sentimo.R;

/**
 * This class represents the fragment that comes up when the user presses
 * the filter button on the main screen. It prompts the user to select a specific
 * emotion, or all emotions, and then changes the ListView to only display the Moods that
 * match based on emotion.
 */
public class FilterFragment extends DialogFragment {

    private Button happyButton;
    private Button sadButton;
    private Button madButton;
    private Button embarrassedButton;
    private Button confidentButton;
    private Button lovedButton;
    private Button tiredButton;
    private Button worriedButton;
    private Button allButton;
    private Boolean all;
    private OnFragmentInteractionListener listener;
    private Emotion emotion;
    private View.OnClickListener buttonListener;

    public interface OnFragmentInteractionListener{
        void onFilterPressed(Emotion emotion, Boolean all);
    }

    /**
     * This tells the dialog fragment what the context of the instantiated fragment should
     * be. In this context, it makes sure that the listener is the main activity.
     * @param context
     *      This is the context applied to the fragment
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    /**
     *  This is the function that is called when the fragment is first created.
     *  Gets the buttons from the filter_emoji.xml file and adds listeners to all of
     *  them. Automatically closes when the user presses on an emotion button, the
     *  all button, or cancel. Returns to the main activity with an emotion and a
     *  boolean value that represents if all was pressed.
     * @param savedInstanceState
     *      This is the state of the main activity that called the filter
     *      fragment.
     * @return
     *      Returns a builder that represents the fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_emoji, null);

        happyButton = view.findViewById(R.id.filter_happy_button);
        sadButton = view.findViewById(R.id.filter_sad_button);
        madButton = view.findViewById(R.id.filter_mad_button);
        lovedButton = view.findViewById(R.id.filter_loved_button);
        confidentButton = view.findViewById(R.id.filter_confident_button);
        embarrassedButton = view.findViewById(R.id.filter_embarrassed_button);
        tiredButton = view.findViewById(R.id.filter_tired_button);
        worriedButton = view.findViewById(R.id.filter_worried_button);
        allButton = view.findViewById(R.id.filter_all_button);
        all = false;

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all = true;
                listener.onFilterPressed(emotion, all);
                FilterFragment.this.dismiss();
            }
        });

        buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.filter_happy_button:
                        emotion = new Happy();
                        break;
                    case R.id.filter_sad_button:
                        emotion = new Sad();
                        break;
                    case R.id.filter_mad_button:
                        emotion = new Mad();
                        break;
                    case R.id.filter_loved_button:
                        emotion = new Loved();
                        break;
                    case R.id.filter_embarrassed_button:
                        emotion = new Embarrassed();
                        break;
                    case R.id.filter_tired_button:
                        emotion = new Tired();
                        break;
                    case R.id.filter_confident_button:
                        emotion = new Confident();
                        break;
                    case R.id.filter_worried_button:
                        emotion = new Worried();
                        break;
                    default:
                        throw new RuntimeException("Unknown emotion case");
                }
                listener.onFilterPressed(emotion, all);
                FilterFragment.this.dismiss();
            }
        };

        happyButton.setOnClickListener(buttonListener);
        sadButton.setOnClickListener(buttonListener);
        madButton.setOnClickListener(buttonListener);
        lovedButton.setOnClickListener(buttonListener);
        embarrassedButton.setOnClickListener(buttonListener);
        tiredButton.setOnClickListener(buttonListener);
        confidentButton.setOnClickListener(buttonListener);
        worriedButton.setOnClickListener(buttonListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Select Mood to Filter")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onFilterPressed(null, all);
                    }
                }).create();
    }
}
