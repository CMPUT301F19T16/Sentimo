package com.example.sentimo;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private ArrayList<Mood> moodHistory;
    private String username;

    Database() {
        moodHistory = new ArrayList<>();
    }

    Database(String username) {
        moodHistory = new ArrayList<>();
        this.username = username;
        getAllMoods();
    }

    /**
     * Read data from firestore and save it to local array list
     */
    private void getAllMoods() {
        CollectionReference userMoods = getUserMoods();
        userMoods.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodHistory.clear();
                List<DocumentSnapshot> data = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : data) {
                    Mood mood = d.toObject(Mood.class);
                    moodHistory.add(mood);
                }
            }
        });
    }

    public ArrayList<Mood> getMoodHistory() {
        return moodHistory;
    }

    public void addMood(Mood mood) {
        getUserMoods().document(Integer.toString(mood.hashCode())).set(mood);
    }

    public void deleteMood(Mood mood) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete();
    }

    private CollectionReference getUserMoods() {
        return users.document(this.username).collection("moods");
    }

}
