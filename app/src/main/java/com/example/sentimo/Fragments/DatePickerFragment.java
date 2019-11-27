package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * A class that represents a fragment for a DatePicker dialog for when the user clicks on the text of
 * the date in the add mood or edit mood fragments.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerListener listener;

    /**
     * A function that is called when the dialog is created.
     * It gets the current year, month, and day and creates
     * a DatePickerDialog with this information.
     * @param savedInstanceState
     * @return Dialog
     *          An instance of DatePickerDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, year, month, day);
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
        listener = (DatePickerListener) getParentFragment();
    }

    /**
     * A function that is called when the date is set from the DatePickerDialog.
     * @param view
     * @param year
     *          The year that was set.
     * @param month
     *          The month that was set.
     * @param day
     *          The day that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.DateReturned(year, month, day);
    }

    /**
     * This is an interface that is implemented by the listener of the fragment.
     */
    public interface DatePickerListener {
        void DateReturned(int year, int month, int day);
    }

}