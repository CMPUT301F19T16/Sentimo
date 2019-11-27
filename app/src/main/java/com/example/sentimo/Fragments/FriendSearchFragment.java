package com.example.sentimo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sentimo.R;

/**
 * A fragment to allow a user to search for and add friends
 */
public class FriendSearchFragment extends DialogFragment {

    private EditText friendSearchField;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onSearchPressed(String name);
    }

    /**
     * Attach behaviour for fragment
     * @param context The context that will implement the OnFragmentInteractionListener
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Initialization behaviour for the fragment
     * @param savedInstanceState Information passed to the fragment for use in initialization
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.friend_search_layout, null);

        friendSearchField = view.findViewById(R.id.friend_search_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Back", null)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = friendSearchField.getText().toString();
                        if (!username.isEmpty()){
                            listener.onSearchPressed(username);
                        }
                    }
                }).create();
    }
}
