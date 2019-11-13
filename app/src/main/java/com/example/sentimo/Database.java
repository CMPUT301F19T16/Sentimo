package com.example.sentimo;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class for keeping track of moods in a database including
 * adding, editing, and deleting
 */
public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private String username;
    private ArrayList<Mood> moodHistory;
    private DatabaseListener listener;

    Database(String username) {
        this.username = username;
        this.moodHistory = new ArrayList<>();
    }

    /**
     * Returns the username of the user.
     * @return
     *  Returns a string that represents the username.
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Add a mood to the history
     *
     * @param mood mood to be added
     */
    public void addMood(Mood mood) {
        getUserMoods().document(Integer.toString(mood.hashCode())).set(mood);
    }

    /**
     * Delete a mood from history
     *
     * @param mood mood to be deleted
     */
    public void deleteMood(Mood mood) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete();
    }

    /**
     * get the collection reference of user's mood list
     * @return
     *      the reference pointing to user's moods
     */
    public CollectionReference getUserMoods() {
        return users.document(this.username).collection("moods");
    }

    /**
     * get mood list
     * @return
     *      user's mood list
     */
    public ArrayList<Mood> getMoodHistory() {
        return moodHistory;
    }

    /**
     * set the listener after successfully read from cloud
     * @param listener
     *      custom listener called after reading from cloud successfully
     */
    public void setListener(DatabaseListener listener) {
        this.listener = listener;
    }

    /**
     * listen to the mood list from cloud, sorted by reverse chronological order
     */
    public void addMoodListener() {
        CollectionReference userMoods = this.getUserMoods();
        userMoods.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                moodHistory.clear();
                List<DocumentSnapshot> data;
                if (queryDocumentSnapshots != null) {
                    data = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : data) {
                        Mood mood = d.toObject(Mood.class);
                        moodHistory.add(mood);
                    }
                    Collections.sort(moodHistory, Collections.<Mood>reverseOrder());
                    listener.onSuccess();
                }
            }
        });
    }
}
