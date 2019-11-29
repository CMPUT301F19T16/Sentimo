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

/**
 * This class represents the action that happens when the user presses the map button
 * on the main screen. It is a fragment that prompts the user to pick which of the
 * maps they want to few, either their map (My Map) or their friends' map (Friend Map).
 * This class also contains an interface for the MainActivity to implement which
 * forces the MainActivity to call the DisplayMapActivity when one of the buttons are pressed.
 */
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

    /**
     * This function tells the function what should happen when a user presses on the buttons
     * on the fragment. It takes the savedInstanceState as an argument so that the function
     * understands the context and can set up what it needs to do appropriately. If the "My Map"
     * button is pressed, the fragment automatically closes and MainActivity passes the "My Map"
     * string to the DisplayMapActivity. Vice versa for the "Friend Map" button. If the user
     * presses cancel, the fragment closes and the user is returned to the MainActivity screen.
     * @param savedInstanceState
     * @return
     */
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
