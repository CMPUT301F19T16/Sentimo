package com.example.sentimo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FriendRequestActivity extends AppCompatActivity {
    private ArrayList<String> pendingRequestsList;
    private ArrayAdapter<String> requestsAdapter;
    private ListView requestListView;
    private Database database;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        auth = new Auth(getApplicationContext());
        database = new Database(auth.getActiveUsername());

        auth = new Auth(getApplicationContext());

        // require user login
        if (!auth.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        requestListView = findViewById(R.id.friend_request_listview);
        pendingRequestsList = database.getPendingRequests();
        requestsAdapter = new FriendRequestList(this, pendingRequestsList);
        requestListView.setAdapter(requestsAdapter);

        fetchAllRequests();

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(FriendRequestActivity.this);
                alert.setMessage("Are you sure you want to authorize this user?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmFollowRequest(pendingRequestsList.get(position));
                    }
                });
                alert.setNegativeButton("NO", null);
                alert.show();
            }
        });
    }

    private void fetchAllRequests() {
        database.fetchPendingRequests(new DatabaseListener() {
            @Override
            public void onSuccess() {
                pendingRequestsList = database.getPendingRequests();
                requestsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                Toast.makeText(FriendRequestActivity.this, "Failed to fetch requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmFollowRequest(final String username) {
        database.confirmFollowRequest(username, new DatabaseListener() {
            @Override
            public void onSuccess() {
                fetchAllRequests();
            }

            @Override
            public void onFailure() {
                Toast.makeText(FriendRequestActivity.this, "Failed to approve request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
