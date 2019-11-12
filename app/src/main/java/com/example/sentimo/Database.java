package com.example.sentimo;

import android.widget.BaseAdapter;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.*;

/**
 * A class for keeping track of moods in a database including
 * adding, editing, and deleting
 */
public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private String username;
    private BaseAdapter mAdapter;

    Database(String username) {
        this.username = username;
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
     * Set a adapter to be notified when the data changed
     *
     * @param mAdapter adapter to be notified
     */
    public void setAdapter(BaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public CollectionReference getUserMoods() {
        return users.document(this.username).collection("moods");
    }

}
