package com.example.sentimo;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class DisplayMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mapName;
    private Database database;
    private Auth auth;
    private ArrayList<Mood> moods;
    private Mood mood;
    private ArrayList<Mood> moodLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();

        mapName = intent.getStringExtra("mapName");
        auth = new Auth(getApplicationContext());
        database = new Database(auth.getActiveUsername());
        moods = database.getMoodHistory();
        moodLocations = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        database.addMoodListener(new DatabaseListener() {
            @Override
            public void onSuccess() {
                moods = database.getMoodHistory();
                if(mapName.equals(getString(R.string.my_map))){
                    if(moods.isEmpty()) {
                        Toast.makeText(DisplayMapActivity.this, "No Moods", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < moods.size(); i++) {
                            mood = moods.get(i);
                            if (mood.getEmotion() != null && mood.getLongitude() != null && mood.getLatitude() != null) {
                                moodLocations.add(mood);
                            }
                        }
                        if (moodLocations.isEmpty()) {
                            Toast.makeText(DisplayMapActivity.this, "No Moods with Locations", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < moodLocations.size(); i++) {
                                mood = moodLocations.get(i);
                                Drawable image = getResources().getDrawable(mood.getEmotion().getImage());
                                Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
                                bitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, false);

                                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                                LatLng location = new LatLng(mood.getLatitude(), mood.getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .title(mood.getEmotion().getName()))
                                        .setIcon(icon);
                                if (i == moodLocations.size() - 1) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(DisplayMapActivity.this, "Couldn't get moods", Toast.LENGTH_SHORT);
            }
        });
    }
}
