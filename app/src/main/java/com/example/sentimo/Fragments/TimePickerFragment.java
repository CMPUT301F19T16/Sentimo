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

/**
 * A class that represents a fragment for a TimePicker dialog for when the user clicks on the text of
 * the time in the add mood or edit mood fragments.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimePickerListener listener;

    /**
     * A function that is called when the dialog is created.
     * It gets the current hour and minute of the day and creates
     * a TimePickerDialog with this information.
     * @param savedInstanceState
     * @return Dialog
     *          An instance of TimePickerDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Tells the Dialog fragment what is the context of this fragment and
     * what is the listener of the fragment.
     * @param context
     *      Context object that represents the state and attributes of the
     *      listener and the fragment.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TimePickerListener) getParentFragment();
    }

    /**
     * A function that is called when the time is set from the TimePickerDialog.
     * @param view
     * @param hourOfDay
     *          The hour in 24 hour time that was set.
     * @param minute
     *          The minute that was set.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.TimeReturned(hourOfDay, minute);
    }

    /**
     * This is an interface that is implemented by the listener of the fragment.
     */
    public interface TimePickerListener {
        void TimeReturned(int hourOfDay, int minute);
    }

}