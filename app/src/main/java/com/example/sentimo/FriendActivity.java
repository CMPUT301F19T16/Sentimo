package com.example.sentimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentimo.Fragments.EditMoodFragment;
import com.example.sentimo.Fragments.FriendMoodDisplayFragment;
import com.example.sentimo.Fragments.FriendSearchFragment;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity implements FriendSearchFragment.OnFragmentInteractionListener{

    private ArrayList<Mood> friendMoodDataList;
    private ArrayAdapter<Mood> friendMoodAdapter;
    private Button backButton;
    private Button searchButton;
    private Button friendRequstButton;
    private ListView friendListView;
    public Database database;
    private Auth auth;
    private ArrayList<String> userFollowing;
    private ArrayList<Mood> tempMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        auth = new Auth(getApplicationContext());
        database = new Database(auth.getActiveUsername());

        userFollowing = database.getUserFollowing();

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
        auth = new Auth(getApplicationContext());

        // require user login
        if (!auth.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        friendMoodDataList = database.getSharedMood();
        friendMoodAdapter = new CustomFriendMoodList(this, friendMoodDataList);
        friendListView.setAdapter(friendMoodAdapter);

        fetchAllMoodsForFriends();

        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mood mood = friendMoodDataList.get(position);
                new FriendMoodDisplayFragment(mood).show(getSupportFragmentManager(), "FRIEND_MOOD");
//                new EditMoodFragment(mood, position).show(getSupportFragmentManager(), "EDIT_MOOD");
                // Implement display only edit mood type fragment so can see full details
            }
        });

        friendRequstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendActivity.this, FriendRequestActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     *  search if the username exist. If the username exists, add it to the follow list if it's
     *  not in the list or as same as the current user's username
     * @param username
     *      username entered
     */
    @Override
    public void onSearchPressed(final String username){
        // Should check if user exists in the Sentimo database.
        // If not, displays a fragment telling the user that it doesn't exist.
        database.fetchFollowList(new DatabaseListener() {
            @Override
            public void onSuccess() {
                database.isUserExist(new DatabaseListener() {
                    @Override
                    public void onSuccess() {
                        userFollowing = database.getUserFollowing();
                        if (username.equals(auth.getActiveUsername())) {
                            Toast.makeText(FriendActivity.this, "Cannot follow yourself", Toast.LENGTH_SHORT).show();
                        } else if (userFollowing.contains(username))
                            Toast.makeText(FriendActivity.this, "Already Followed", Toast.LENGTH_SHORT).show();
                        else {
                            database.sendRequest(username, new DatabaseListener() {
                                @Override
                                public void onSuccess() {
                                    database.setFollowList(username, new DatabaseListener() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(FriendActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(FriendActivity.this, "Fail to send request", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(FriendActivity.this, "Fail to send request", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(FriendActivity.this, "User not exist", Toast.LENGTH_SHORT).show();
                    }
                }, username);
            }

            @Override
            public void onFailure() {
                Toast.makeText(FriendActivity.this, "Failed to connect to cloud", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAllMoodsForFriends() {
        database.getSharedMoodList(new DatabaseListener() {
            @Override
            public void onSuccess() {
                friendMoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                Toast.makeText(FriendActivity.this, "Please check your Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
