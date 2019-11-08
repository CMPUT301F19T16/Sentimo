package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sentimo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InvalidDataWarningFragment extends DialogFragment {
    int warningType;

    public InvalidDataWarningFragment(int warningType) {
        this.warningType = warningType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.invalid_data_warning_fragment, null);
        String warningMessage = null;
        switch (warningType) {
            case 1:
                warningMessage = "No emotion selected.";
                break;
            case 2:
                warningMessage = "Invalid date or time.";
                break;
            case 3:
                warningMessage = "Reason longer than 20 characters";
                break;
            case 4:
                warningMessage = "More than 3 words";
                break;
            default:
                new RuntimeException("Warning Type Not Supported");
        }
        ((TextView)(view.findViewById(R.id.warning_text))).setText(warningMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Data Warning")
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
    }
}
