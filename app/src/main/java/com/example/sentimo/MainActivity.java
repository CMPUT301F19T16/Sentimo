package com.example.sentimo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddMoodFragment.OnFragmentInteractionListener{

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

    }

    @Override
    public void onDonePressed(Mood mood){
        moodAdapter.add(mood);
    }
}
