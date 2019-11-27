package com.example.sentimo.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.view.View;
import com.example.sentimo.Mood;
import com.example.sentimo.TimeFormatter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 *
 *The AddMoodFragment class is a fragment that creates a Mood object that is then sent to
 *the main screen.
 *This class is a modified version of the ListyCity demo in Lab 3 and uses a lot of resources
 *from the AddCityFragment class.
 *I don't know who wrote the ListyCity demo, but the Android Studio .zip file is on eclass at this URL
 *Url == https://eclass.srv.ualberta.ca/course/view.php?id=54165
 */
public class AddMoodFragment extends ChangeMoodFragment {
    private AddMoodListener listener;
    private FusedLocationProviderClient fusedLocationClient;
    private Location locationForListener;
    private LocationCallback locationCallback;


    public AddMoodFragment() {
        this.initialMood = new Mood();
    }



    /**
     * Subclass specific initialization (required to ensure called after UI hookup)
     */
    @Override
    protected void subclassInitialization() {
        emojiImageButton.setVisibility(View.VISIBLE);
        emojiImageView.setVisibility(View.INVISIBLE);

        TimeFormatter timef = new TimeFormatter();
        Date time = new Date();
        time.setTime(Calendar.getInstance().getTimeInMillis());
        timef.setTime(time);
        dateTextView.setText(timef.getDateString());
        timeTextView.setText(timef.getTimeString());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener( new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    AddMoodFragment.this.locationForListener = location;
                }
            }
        });

        // Adapted from Google's example found here: https://developer.android.com/training/location/receive-location-updates.html
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    locationForListener = location;
                    break;
                }
            }
        };
    }

    /**
     * Listener for activity calling AddMoodFragment to receive a mood back
     */
    public interface AddMoodListener {
        void onDonePressed(Mood newMood, String localPath);
    }

    /**
     * Attach behaviour, including hooking up the appropriate listener
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Attaching AddMoodListener from parent Activity
        if (context instanceof AddMoodListener) {
            this.listener = (AddMoodFragment.AddMoodListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement AddMoodListener");
        }
    }


    /**
     * Listener to return mood to parent activity
     * @param mood: the mood created by the ChangeMoodFragment
     */
    @Override
    public void callListener(Mood mood) {
        listener.onDonePressed(mood, localImagePath);
    }


    /**
     * Dialog builder specific to AddMoodFragment
     * @return the initialized AlertDialog.Builder
     */
    @Override
    protected AlertDialog.Builder returnBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Mood")
                .setNegativeButton("Back", null)
                // Positive Button behaviour set in super class to override default dismissal behaviour
                .setPositiveButton("Done", null);
    }

    /**
     * Resume behaviour, including reattaching the location client
     */
    @Override
    public void onResume() {
        super.onResume();
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * Pause behaviour, including de-attaching the location client
     */
    @Override
    public void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


    /**
     * Return location associated with current Mood
     * @return
     */
    @Override
    protected Location subclassLocationReturnBehaviour() {
        return locationForListener;
    }
}
