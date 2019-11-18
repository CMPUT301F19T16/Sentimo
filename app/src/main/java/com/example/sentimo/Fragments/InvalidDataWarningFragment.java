package com.example.sentimo.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sentimo.InputErrorType;
import com.example.sentimo.LoginInfo;
import com.example.sentimo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

/**
 * This class displays an invalid data warning message, customized to the received
 * errot type
 */
public class InvalidDataWarningFragment extends DialogFragment {
    InputErrorType warningType;

    /**
     * Constructor for setting warning type
     * @param warningType warning type to display
     */
    public InvalidDataWarningFragment(InputErrorType warningType) {
        this.warningType = warningType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.invalid_data_warning_fragment, null);
        String warningMessage = null;
        switch (warningType) {
            case CMFNullMoodError:
                warningMessage = getString(R.string.warning_CMFNullMoodError);
                break;
            case CMFTimeParseError:
                warningMessage = getString(R.string.warning_CMFTimeParseError);
                break;
            case CMFReasonTooLongError:
                warningMessage = getString(R.string.warning_CMFReasonTooLongError);
                break;
            case CMFReasonTooManyWordsError:
                warningMessage = getString(R.string.warning_CMFReasonTooManyWordsError);
                break;
            case CMFPictureAndReasonError:
                warningMessage = getString(R.string.warning_CMFPictureAndReasonError);
                break;
            case LoginPasswordTooShortError:
                warningMessage = getString(R.string.warning_LoginPasswordTooShortError);
                break;
            case LoginUsernameTooShortError:
                warningMessage = getString(R.string.warning_LoginUsernameTooShortError);
                break;
            case CMFNoLocationPermission:
                warningMessage = getString(R.string.warning_CMFNoLocationPermission);
                break;
            case CMFPhotoReturnError:
                warningMessage = getString(R.string.warning_CMFPhotoReturnError);
                break;
            case ImageDisplayError:
                warningMessage = getString(R.string.warning_ImageDisplayError);
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
                        if (warningType == InputErrorType.CMFNoLocationPermission) {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        }
                    }
                }).create();
    }
}
