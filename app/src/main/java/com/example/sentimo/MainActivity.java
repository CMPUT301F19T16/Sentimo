package com.example.sentimo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;

import java.util.ArrayList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sentimo.Fragments.AddMoodFragment;
import com.example.sentimo.Fragments.EditMoodFragment;

public class MainActivity extends AppCompatActivity implements AddMoodFragment.AddMoodListener,
                                                               EditMoodFragment.EditMoodListener {

    private ListView moodList;
    private ArrayList<Mood> moodDataList;
    private ArrayAdapter<Mood> moodAdapter;
    private Button addButton;
    private Button mapButton;
    private Button friendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moodList = findViewById(R.id.mood_list);
        addButton = findViewById(R.id.add_button);
        mapButton = findViewById(R.id.map_button);
        friendButton = findViewById(R.id.friend_button);

        moodDataList = new ArrayList<>();
        moodAdapter = new CustomMoodList(this, moodDataList);
        moodList.setAdapter(moodAdapter);

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
        moodAdapter.add(mood);
    }

    @Override
    public void onConfirmEditPressed(Mood mood, int position){
        Mood oldMood = moodAdapter.getItem(position);
        oldMood.setDate(mood.getDate());
        oldMood.setTime(mood.getTime());
        oldMood.setEmotion(mood.getEmotion());
        oldMood.setReason(mood.getReason());
        oldMood.setSituation(mood.getSituation());
        oldMood.setLocationPermission(mood.getLocationPermission());
        moodList.setAdapter(moodAdapter);
    }
}
