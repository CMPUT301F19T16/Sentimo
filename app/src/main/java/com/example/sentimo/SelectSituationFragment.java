package com.example.sentimo;

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

public class SelectSituationFragment extends DialogFragment {
    Button aloneButton;
    Button onePersonButton;
    Button severalPeopleButton;
    Button crowdButton;
    View view;
    SelectSituationListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        this.view = LayoutInflater.from(getActivity()).inflate(R.layout.select_situation, null);

        aloneButton = view.findViewById(R.id.aloneButton);
        onePersonButton = view.findViewById(R.id.onePersonButton);
        severalPeopleButton = view.findViewById(R.id.severalPeopleButton);
        crowdButton = view.findViewById(R.id.crowdButton);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Mood")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.SituationReturned(null);
                    }
                }).create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SelectSituationListener)getParentFragment();
    }

    public interface SelectSituationListener {
        public void SituationReturned(Situation situation);
    }
}
