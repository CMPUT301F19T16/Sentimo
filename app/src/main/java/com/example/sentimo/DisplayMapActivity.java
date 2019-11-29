package com.example.sentimo;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * This class represents the Activity displayed after the user decides which
 * map they want to few from the MapFragment that is created after the user presses on the
 * map button on the MainScreen. The class displays the google map that holds the locations
 * of the moods that have locations in the user's own list, or the user's friends' moods. The
 * class implements the OnMapReadyCallback interface that forces the class to have the onMapReady
 * function. This class displays the moods once connected to the Firebase database.
 */
public class DisplayMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mapName;
    private Database database;
    private Auth auth;
    private ArrayList<Mood> moods;
    private Mood mood;
    private ArrayList<Mood> moodLocations;

    /**
     * This function is called when the Activity is first created. It makes sure to set
     * important values accordingly like the mapName and auth of the user. It also calls the
     * getMapAsync function that tells the Activity when the map is ready. Takes the
     * savedInstanceState as it's parameter which tells the onCreate method the
     * circumstances of which the Activity was called.
     * @param savedInstanceState
     *  The context of the activity.
     */
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
     *      * Manipulates the map once available.
     *      * This callback is triggered when the map is ready to be used.
     *      * This is where we can add markers or lines, add listeners or move the camera.
     *      * If Google Play services is not installed on the device, the user will be prompted to install
     *      * it inside the SupportMapFragment. This method will only be triggered once the user has
     *      * installed Google Play services and returned to the app.
     *  This function is connected to the Firestore Database so that it only works when
     *  the database can reach all the necessary moods. After the database is connected, this
     *  function calls the drawMap function if the moods are not empty.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mapName.equals(getString(R.string.my_map))) {
            database.addMoodListener(new FirebaseListener() {
                @Override
                public void onSuccess() {
                    moods = database.getMoodHistory();
                    if(!moods.isEmpty()){
                        drawMap(moods);
                    }
                }
                @Override
                public void onFailure() {
                }
            });
        }
        else if (mapName.equals(getString(R.string.friend_map))){
            database.getSharedMoodList(new FirebaseListener() {
                @Override
                public void onSuccess() {
                    moods = database.getSharedMood();
                    if(!moods.isEmpty()){
                        drawMap(moods);
                    }
                }

                @Override
                public void onFailure() {
                }
            });
        }
    }

    /**
     * This function is necessary so that the database that is created for this
     * activity does not run in the MainActivity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        database.destroyListener();
        finish();
    }

    /**
     * This function takes the moods given to it by the database and applies them to the
     * googleMap. It first checks if the moods given have locations, and it only draws the moods
     * if they do have locations. It sets each mood to the location and attaches the mood's
     * emoji to that marker. If the Friend Map is to be drawn, the function makes the title of
     * each mood the friend that had the mood (can be shown by pressing on the marker). If
     * the My Map is to be drawn, the title is the name of the emotion. The camera is moved to
     * the last mood added. If the camera is in an area where there is no mood (defaults to
     * northern Africa), then all moods given to the function had no locations.
     * @param moods
     *  an ArrayList of moods that are the moods connected to the FireStore database.
     *  Either the moods of the user's friends, or the user's moods themselves.
     */
    private void drawMap(ArrayList<Mood> moods){
        for (int i = 0; i < moods.size(); i++) {
            mood = moods.get(i);
            if (mood.getEmotion() != null && mood.getLongitude() != null && mood.getLatitude() != null) {
                moodLocations.add(mood);
            }
        }
        if (!moodLocations.isEmpty()) {
            for (int i = 0; i < moodLocations.size(); i++) {
                mood = moodLocations.get(i);
                Drawable image = getResources().getDrawable(mood.getEmotion().getImage());
                Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, false);

                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                LatLng location = new LatLng(mood.getLatitude(), mood.getLongitude());
                MarkerOptions marker = new MarkerOptions();
                marker.position(location);
                if (mapName.equals(getString(R.string.my_map))){
                    marker.title(mood.getEmotion().getName());
                } else if (mapName.equals(getString(R.string.friend_map))){
                    marker.title(mood.getUsername());
                }
                mMap.addMarker(marker)
                        .setIcon(icon);
                if (i == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                }
            }
        }
    }

    /**
     * This function returns the number of moods that were displayed in the map. This function
     * is currently only used in the testing of the DisplayMapActivity.
     * @return
     *  Returns an integer that represents the number of moods being Displayed.
     */
    public int getNumber(){
        return this.moodLocations.size();
    }
}
