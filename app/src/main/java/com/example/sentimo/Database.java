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
import java.util.List;

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
        getAllMoods();
    }

    /**
     * Read data from firestore and save it to local array list
     */
    private void getAllMoods() {
        CollectionReference userMoods = getUserMoods();
        userMoods.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> data = task.getResult().getDocuments();
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
        getAllMoods();
    }

    public void deleteMood(Mood mood) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete();
        getAllMoods();
    }

    private CollectionReference getUserMoods() {
        return users.document(this.username).collection("moods");
    }

}
