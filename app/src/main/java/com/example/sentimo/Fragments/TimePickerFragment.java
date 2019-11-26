package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimePickerListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TimePickerListener) getParentFragment();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.TimeReturned(hourOfDay, minute);
    }

    public interface TimePickerListener {
        void TimeReturned(int hourOfDay, int minute);
    }

}