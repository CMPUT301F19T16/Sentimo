package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.sentimo.R;
import com.example.sentimo.Situations.AloneSituation;
import com.example.sentimo.Situations.CrowdSituation;
import com.example.sentimo.Situations.OnePersonSituation;
import com.example.sentimo.Situations.SeveralPeopleSituation;
import com.example.sentimo.Situations.Situation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This is a class that represents the fragment that occurs when the user
 * presses the Situation button in either the add mood fragment or the edit
 * mood fragment.
 * Allows the user to select/deselect a situation associated with their mood.
 */
public class SelectSituationFragment extends DialogFragment {
    Button aloneButton;
    Button onePersonButton;
    Button severalPeopleButton;
    Button crowdButton;
    Button noSituationButton;
    View view;
    SelectSituationListener listener;

    /**
     * This is the function that represents what happens when the fragment
     * is created.
     * It makes sure that each of the situation buttons have their own
     * listeners and makes it so when you press a button, the fragment
     * closes automatically.
     * @param savedInstanceState
     *      This is the state of the add/edit mood fragment that calls this
     *      fragment.
     * @return
     *      Returns a builder that allows the user to pick a situation or cancel
     *      out of the fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        this.view = LayoutInflater.from(getActivity()).inflate(R.layout.select_situation, null);

        aloneButton = view.findViewById(R.id.aloneButton);
        onePersonButton = view.findViewById(R.id.onePersonButton);
        severalPeopleButton = view.findViewById(R.id.severalPeopleButton);
        crowdButton = view.findViewById(R.id.crowdButton);
        noSituationButton = view.findViewById(R.id.noSituationButton);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Situation situation = null;
                switch (id) {
                    case R.id.aloneButton:
                        situation = new AloneSituation();
                        break;
                    case R.id.onePersonButton:
                        situation = new OnePersonSituation();
                        break;
                    case R.id.severalPeopleButton:
                        situation = new SeveralPeopleSituation();
                        break;
                    case R.id.crowdButton:
                        situation = new CrowdSituation();
                        break;
                    case R.id.noSituationButton:
                        situation = null;
                        break;
                    default:
                        throw new RuntimeException("Unknown situation button");
                }
                listener.SituationReturned(situation);
                SelectSituationFragment.this.dismiss();
            }
        };

        aloneButton.setOnClickListener(buttonListener);
        onePersonButton.setOnClickListener(buttonListener);
        severalPeopleButton.setOnClickListener(buttonListener);
        crowdButton.setOnClickListener(buttonListener);
        noSituationButton.setOnClickListener(buttonListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Mood")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectSituationFragment.this.dismiss();
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
        listener = (SelectSituationListener)getParentFragment();
    }

    /**
     * This is an interface that is implemented by the listener of the fragment,
     * in this case, either the add mood fragment or the edit mood fragment.
     * It makes sure that the situation that is passed back is parsed and
     * that the appropriate actions are taken when this happens.
     */
    public interface SelectSituationListener {
        public void SituationReturned(Situation situation);
    }
}
