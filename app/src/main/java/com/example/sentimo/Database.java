package com.example.sentimo;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private ArrayList<Mood> moodHistory;
    private HashMap<String, Mood> moodHash;
    private String username;

    Database() {
        moodHistory = new ArrayList<>();
        moodHash = new HashMap<>();
    }

    Database(String username) {
        moodHistory = new ArrayList<>();
        moodHash = new HashMap<>();
        this.username = username;
    }

    public void getAllMoods() {
        // TODO: read data from database
        CollectionReference userMoods = getUserMoods();
        userMoods.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Get List", "inside!!!!!!!!!!!!!!!!!");
                    }
                } else {
                    Log.d("Get List", "Error getting documents: ");
                }
            }
        });
    }

    public ArrayList<Mood> getMoodHistory() {
        Log.d("Get History", "not successful or empty    " + moodHistory.size());
        return moodHistory;
    }

    public void addMood(Mood mood) {
        getUserMoods().document(Integer.toString(mood.hashCode())).set(mood);
        getAllMoods();
    }

    public void deleteMood(Mood mood) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete();
    }

    private CollectionReference getUserMoods() {
//        TODO: Implement get by username
        return users.document(this.username).collection("moods");
    }

}
