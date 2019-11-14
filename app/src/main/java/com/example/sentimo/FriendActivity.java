package com.example.sentimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.sentimo.Fragments.FriendSearchFragment;

public class FriendActivity extends AppCompatActivity implements FriendSearchFragment.OnFragmentInteractionListener{

    private Button backButton;
    private Button searchButton;
    private Button friendRequstButton;
    private ListView friendListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        backButton = findViewById(R.id.friend_back_button);
        searchButton = findViewById(R.id.friend_search_button);
        friendRequstButton = findViewById(R.id.friend_request_button);
        friendListView = findViewById(R.id.friend_list);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FriendSearchFragment().show(getSupportFragmentManager(), "FRIEND_SEARCH");
            }
        });
    }

    @Override
    public void onSearchPressed(String username){
        // Should check if user exists in the Sentimo database.
        // If not, displays a fragment telling the user that it doesn't exist.
    }
}
