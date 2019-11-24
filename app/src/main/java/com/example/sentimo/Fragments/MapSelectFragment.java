package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sentimo.R;

public class MapSelectFragment extends DialogFragment{

    private Button myMapButton;
    private Button friendMapButton;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onMapSelected(String button);
    }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.map_select_fragment, null);

        myMapButton = view.findViewById(R.id.my_map_button);
        friendMapButton = view.findViewById(R.id.friend_map_button);

        myMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMapSelected(getString(R.string.my_map));
                MapSelectFragment.this.dismiss();
            }
        });

        friendMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMapSelected(getString(R.string.friend_map));
                MapSelectFragment.this.dismiss();
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Choose which map to view")
                .setNegativeButton("Cancel", null).create();
    }
}
