package com.example.sentimo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;

import java.util.ArrayList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sentimo.Fragments.AddMoodFragment;
import com.example.sentimo.Fragments.EditMoodFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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
                                                               EditMoodFragment.EditMoodListener {

    private ListView moodList;
    private ArrayList<Mood> moodDataList;
    private ArrayAdapter<Mood> moodAdapter;
    private Button addButton;
    private Button mapButton;
    private Button friendButton;
    private Database database;

    /**
     * This is function that represents what happens when the Main Screen is
     * first created. It makes sure that each of the buttons of the Main Screen
     * have their own listeners and that the ListView has the Moods of the user.
     * Currently,
     *      Allows the user to create a new mood by pressing the add button,
     *      Allows the user to edit a previously created mood by pressing on
     *      one of the moods in their ListView,
     *      and Allows the user to delete the moods that they wish by pressing
     *      and holding on one of the moods in their ListView and selecting "yes"
     *      when prompted
     *
     * @param savedInstanceState
     *      This is the state of the Main Screen when created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moodList = findViewById(R.id.mood_list);
        addButton = findViewById(R.id.add_button);
        mapButton = findViewById(R.id.map_button);
        friendButton = findViewById(R.id.friend_button);

        moodDataList = new ArrayList<>();
        // TODO: Read username from login
        database = new Database("Testing");
        moodAdapter = new CustomMoodList(this, moodDataList);
        moodList.setAdapter(moodAdapter);

        addMoodListener();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMoodFragment().show(getSupportFragmentManager(), "ADD_MOOD");
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
                    }
                });
                alert.setNegativeButton("NO", null);
                alert.show();
                return true;
            }
        });

    }

    /**
     * This is the function that is required for the addMoodFragment.
     * Takes a mood and adds it to the database.
     * Should then show up on the ListView.
     * @param mood
     *      This is the mood that the user created in the addMoodFragment.
     */
    @Override
    public void onDonePressed(Mood mood){
        database.addMood(mood);
    }

    /**
     * This ist the function that is required for the editMoodFragment.
     * Takes a mood previously in the ListView and updates it by deleting
     * the old mood in the MoodList and replaces it with a new mood.
     * Should then show up on the ListView.
     * @param mood
     *      This is the mood that the user edited from a previously known mood.
     * @param position
     *      This is the position of the old mood in the Mood List.
     */
    @Override
    public void onConfirmEditPressed(Mood mood, int position){
        Mood oldMood = moodAdapter.getItem(position);
        database.deleteMood(Objects.requireNonNull(oldMood));
        database.addMood(mood);
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
