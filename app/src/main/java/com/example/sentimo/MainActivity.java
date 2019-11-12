package com.example.sentimo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Fragments.AddMoodFragment;
import com.example.sentimo.Fragments.EditMoodFragment;
import com.example.sentimo.Fragments.FilterFragment;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This is the Main Screen of the Sentimo app. Has a ListView that displays
 * the users current Moods and the dates, times, and emotions attached to them.
 * Has several buttons that allow the user to add and edit their moods, see
 * visually where these moods took place, and view their friends and their moods.
 * It also allows the user to delete their moods if they press and hold on one
 * of the moods in their ListView and select "yes" when prompted if they want
 * to delete their selected mood.
 * Currently, this is the first screen that shows up when the user opens the app,
 * but this will change later in development. Implements functions that have to
 * do with the add and edit mood fragments so that the Mood List gets changed and
 * updated automatically. This class uses FireStore to store specific elements of
 * the moods so that they are not deleted each time the user logs on.
 */
public class MainActivity extends AppCompatActivity implements AddMoodFragment.AddMoodListener,
        EditMoodFragment.EditMoodListener,
        FilterFragment.OnFragmentInteractionListener {

    private ListView moodList;
    private ArrayList<Mood> moodDataList;
    private ArrayAdapter<Mood> moodAdapter;
    private ArrayList<Mood> partialDataList;
    private ArrayAdapter<Mood> partialAdapter;
    private Button addButton;
    private Button mapButton;
    private Button friendButton;
    private Button filterButton;
    private Database database;
    private Auth auth;

    /**
     * This is function that represents what happens when the Main Screen is
     * first created. It makes sure that each of the buttons of the Main Screen
     * have their own listeners and that the ListView has the Moods of the user.
     * Currently,
     * Allows the user to create a new mood by pressing the add button,
     * Allows the user to edit a previously created mood by pressing on
     * one of the moods in their ListView,
     * and Allows the user to delete the moods that they wish by pressing
     * and holding on one of the moods in their ListView and selecting "yes"
     * when prompted
     *
     * @param savedInstanceState This is the state of the Main Screen when created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = new Auth(getApplicationContext());

        // require user login
        if (!auth.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        moodList = findViewById(R.id.mood_list);
        addButton = findViewById(R.id.add_button);
        mapButton = findViewById(R.id.map_button);
        friendButton = findViewById(R.id.friend_button);
        filterButton = findViewById(R.id.filter_button);

        moodDataList = new ArrayList<>();
        partialDataList = new ArrayList<>();

        database = new Database(auth.getActiveUsername());
        moodAdapter = new CustomMoodList(this, moodDataList);
        partialAdapter = new CustomMoodList(this, partialDataList);
        moodList.setAdapter(moodAdapter);


        addMoodListener();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMoodFragment().show(getSupportFragmentManager(), "ADD_MOOD");
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterFragment().show(getSupportFragmentManager(), "FILTER_LIST");
            }
        });

        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mood mood = moodAdapter.getItem(position);
                new EditMoodFragment(mood, position).show(getSupportFragmentManager(), "EDIT_MOOD");
            }
        });

        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("Are you sure you want to delete this Mood?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Mood mood = moodAdapter.getItem(position);
                        database.deleteMood(mood);
                        moodDataList.remove(mood);
                        moodAdapter.notifyDataSetChanged();
                        if (moodDataList.size() == 0) {
                            filterButton.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                alert.setNegativeButton("NO", null);
                alert.show();
                return true;
            }
        });

    }

    /**
     * This is the function that is required for the AddMoodFragment.
     * Takes a mood and adds it to the database.
     * Should then show up on the ListView.
     *
     * @param mood This is the mood that the user created in the AddMoodFragment.
     */
    @Override
    public void onDonePressed(Mood mood) {
        database.addMood(mood);
        filterButton.setVisibility(View.VISIBLE);
    }

    /**
     * This is the function that is required for the EditMoodFragment.
     * Takes a mood previously in the ListView and updates it by deleting
     * the old mood in the MoodList and replaces it with a new mood.
     * Should then show up on the ListView.
     *
     * @param mood     This is the mood that the user edited from a previously known mood.
     * @param position This is the position of the old mood in the Mood List.
     */
    @Override
    public void onConfirmEditPressed(Mood mood, int position) {
        Mood oldMood = moodAdapter.getItem(position);
        database.deleteMood(Objects.requireNonNull(oldMood));
        database.addMood(mood);
    }

    /**
     * This is the function that is required for the FilterFragment.
     * Takes an emotion that the user wants to search for and a boolean that
     * represents if the all button was pressed. Makes sure to clear the partialDataList
     * before adding to it.
     * Goes through the moodDataList in search for moods with the emotion
     * matching the emotion that the user wanted to filter for and adds these
     * moods to the partialDataList.
     *
     * @param emotion The emotion that represents what the user wants
     *                the ListView to be filtered for.
     * @param all     A boolean value that represents if the all button was pressed. If
     *                so, should return the user back to the no-filtered moodDataList.
     */
    @Override
    public void onFilterPressed(Emotion emotion, Boolean all) {
        if (all == true) {
            // get old list back
            partialDataList.clear();
            moodList.setAdapter(moodAdapter);
        } else if (emotion != null) {
            // make new list
            // set moodList
            partialDataList.clear();
            String name = emotion.getName();
            String oldName;
            for (int i = 0; i < moodDataList.size(); i++) {
                oldName = moodDataList.get(i).getEmotion().getName();
                if (oldName.equals(name)) {
                    partialDataList.add(moodDataList.get(i));
                }
            }
            moodList.setAdapter(partialAdapter);
        }
    }

    private void addMoodListener() {
        CollectionReference userMoods = database.getUserMoods();
        userMoods.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodDataList.clear();
                List<DocumentSnapshot> data;
                if (queryDocumentSnapshots != null) {
                    data = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : data) {
                        Mood mood = d.toObject(Mood.class);
                        moodDataList.add(mood);
                    }
                    Collections.sort(moodDataList, Collections.<Mood>reverseOrder());
                    moodAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
