package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, year, month, day);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DatePickerListener) getParentFragment();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        listener.DateReturned(year, month, day);
    }

    public interface DatePickerListener {
        void DateReturned(int year, int month, int day);
    }

}