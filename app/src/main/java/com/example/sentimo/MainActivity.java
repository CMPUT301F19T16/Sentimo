package com.example.sentimo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.ArrayList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AddMoodFragment.AddMoodListener,
                                                               EditMoodFragment.EditMoodListener {

    private ListView moodList;
    private ArrayList<Mood> moodDataList;
    private ArrayAdapter<Mood> moodAdapter;
    private Button addButton;
    private Button mapButton;
    private Button friendButton;
    private Database database;

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

    }

    @Override
    public void onDonePressed(Mood mood){
        database.addMood(mood);
    }

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
