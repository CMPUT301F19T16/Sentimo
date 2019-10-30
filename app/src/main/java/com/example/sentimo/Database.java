package com.example.sentimo;

import android.widget.BaseAdapter;

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
    private BaseAdapter mAdapter;

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
                if (mAdapter != null) mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Return an dynamic array of moods
     * @return Array of moods
     */
    public ArrayList<Mood> getMoodHistory() {
        return moodHistory;
    }

    /**
     * add a moood to the history
     * @param mood
     *      mood to be added
     */
    public void addMood(Mood mood) {
        getUserMoods().document(Integer.toString(mood.hashCode())).set(mood);
    }

    /**
     * Delete a mood from history
     * @param mood
     *      mood to be deleted
     */
    public void deleteMood(Mood mood) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete();
    }

    /**
     * set a adapter to be notified when the data changed
     * @param mAdapter
     *      adapter to be notified
     */
    public void setAdapter(BaseAdapter mAdapter) { this.mAdapter = mAdapter; }

    private CollectionReference getUserMoods() {
        return users.document(this.username).collection("moods");
    }

}
