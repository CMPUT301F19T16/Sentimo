package com.example.sentimo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private ArrayList<String> userFollowing;

    Database(String username) {
        this.username = username;
        this.moodHistory = new ArrayList<>();
        this.userFollowing = new ArrayList<>();
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
     * listen to the mood list from cloud, sorted by reverse chronological order
     */
    public void addMoodListener(final DatabaseListener moodListener) {
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
                    if (moodListener != null)
                        moodListener.onSuccess();
                }
            }
        });
    }

    public ArrayList<String> getUserFollowing() {
        return userFollowing;
    }

    /**
     * check if user exist
     * @param listener
     *      custom listener
     * @param username
     *      the user being checked
     */
    public void isUserExist(final DatabaseListener listener, String username) {
        users.document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (listener != null) {
                    if (documentSnapshot.exists()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                }
            }
        });
    }

    /**
     * get the follow list from cloud
     */
    public void getFollowList(final DatabaseListener listener) {
        users.document(this.username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userFollowing = (ArrayList<String>) documentSnapshot.get("followList");
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure();
            }
        });
    }

    /**
     * update follow list to cloud
     * @param list
     *      a list of username that the user  follows
     */
    public void setFollowList(ArrayList<String> list, final DatabaseListener listener) {
        users.document(this.username).update("followList", list).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure();
            }
        });
    }
}
