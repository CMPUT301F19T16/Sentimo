package com.example.sentimo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sentimo.Fragments.ChangeMoodFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
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
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

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
    public void deleteMoodAndPicture(Mood mood) {
        deleteMoodOnly(mood);
        if (mood.getOnlinePath() != null) {
            deletePhoto(mood.getOnlinePath());
        }
    }

    public void deleteMoodOnly(Mood mood) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete();
    }

    public void deletePhoto(String onlinePath) {
        StorageReference storageLocation = firebaseStorage.getReference(onlinePath);
        storageLocation.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SUCCESS","DELETED IMAGE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SUCCESS","DELETED IMAGE");
            }
        });
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

    /**
     * Method for adding a Mood and corresponding photo
     * @param incompleteMood Mood to be added, without its onlinePath initially
     * @param stringPath Local path to photo file to be uploaded
     * @return
     */
    // Upload method uses information from Google's Firebase storage upload example: https://firebase.google.com/docs/storage/android/upload-files
    public Uri addPhotoAndMood(final Mood incompleteMood, final String stringPath) {
//        Uri localPath = Uri.parse(stringPath);

        Uri localPath = Uri.fromFile(new File(stringPath));
        final StorageReference reference = firebaseStorage.getReference();
        final StorageReference storageLocation = reference.child("images/" + incompleteMood.hashCode());
        UploadTask uploadTask = storageLocation.putFile(localPath);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageLocation.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("SUCCESS", storageLocation.getPath());
                    Mood completeMood = new Mood(incompleteMood);
                    completeMood.setOnlinePath(storageLocation.getPath());
                    addMood(completeMood);
                }
                else {
                    Log.e("Photo upload error", stringPath);
                }
            }
        });
        return null;
    }

    /**
     * Method for downloading a photo file from Firebase Storage
     * @param onlinePath Database path to the file to be downloaded
     */
    // Uses elements of Google's Firebase Storage examples found here: https://firebase.google.com/docs/storage/web/download-files
    // Inspired by elements of StackOverflow post: https://stackoverflow.com/questions/39905719/how-to-download-a-file-from-firebase-storage-to-the-external-storage-of-android
    public void downloadPhoto(String onlinePath, final ChangeMoodFragment changeMoodFragment, final DatabaseListener listener) {
        StorageReference storedAt = firebaseStorage.getReference(onlinePath);

        final MainActivity mainActivity = (MainActivity)changeMoodFragment.getActivity();
        File path = new File(mainActivity.getApplicationContext().getFilesDir(), "file_name");
        if (!path.exists()) {
            path.mkdirs();
        }

        String[] splitString = onlinePath.split("/");
        String filename = splitString[splitString.length - 1];
        final File newFile = new File(path, filename);

        storedAt.getFile(newFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("download path is", newFile.getPath());
                changeMoodFragment.setDownloadedImagePath(newFile.getPath());
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FAILURE","FAILURE");
                listener.onFailure();
            }
        });
    }
}
