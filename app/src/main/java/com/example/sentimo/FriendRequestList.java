package com.example.sentimo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom list for displaying the most recent Moods of followed friends
 */
public class FriendRequestList extends ArrayAdapter<String> {
    private ArrayList<String> requests;
    private Context context;

    /**
     * Constructor to make a list for provided requests
     * @param context Application context
     * @param requests Currently pending requests
     */
    public FriendRequestList(Context context, ArrayList<String> requests){
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }

    /**
     * Method to generate views for the cells of the list view
     * @param position Position of cell to generate
     * @param convertView
     * @param parent Parent of the list view cell
     * @return
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.friend_request_cell, parent, false);
        }

        String username = requests.get(position);
        TextView usernameTextView = view.findViewById(R.id.request_name_textview);

        usernameTextView.setText(username);
        return view;
    }
}
