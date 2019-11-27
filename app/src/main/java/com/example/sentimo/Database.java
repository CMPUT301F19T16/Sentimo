package com.example.sentimo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.sentimo.Fragments.ChangeMoodFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
    private ArrayList<String> userFollowing;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private ListenerRegistration moodHistoryReg;
    private ArrayList<Mood> sharedMoodHistory;
    private ListenerRegistration sharedMoodListenerReg;
    private ArrayList<String> pendingRequestsList;
    private ArrayList<String> allowedFriendList;
    private String downloadedImagePath = null;

    /**
     * Constructor for a provided user
     * @param username user for Database instance
     */
    Database(String username) {
        this.username = username;
        this.moodHistory = new ArrayList<>();
        this.userFollowing = new ArrayList<>();
        this.sharedMoodHistory = new ArrayList<>();
        this.sharedMoodListenerReg = null;
        this.pendingRequestsList = new ArrayList<>();
        this.allowedFriendList = new ArrayList<>();
    }

    /**
     * Returns the username of the user.
     *
     * @return Returns a string that represents the username.
     */
    public String getUsername() {
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
    public void deleteMoodAndPicture(final Mood mood) {
        deleteMoodOnly(mood, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mood.getOnlinePath() != null) {
                    deletePhoto(mood.getOnlinePath());
                }
            }
        });
    }

    /**
     * Delete the provided Mood from Firebase Database
     * @param mood the mood to be deleted
     */
    public void deleteMoodOnly(Mood mood, final OnSuccessListener listener) {
        CollectionReference userMoods = getUserMoods();
        String hashcode = Integer.toString(mood.hashCode());
        userMoods.document(hashcode).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("success", "Mood deleted. Success listener to be called.");
                        listener.onSuccess(aVoid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Failure", "Mood not deleted.");
                    }
                });
    }

    /**
     * Delete a photo in Firebase Storage for the given path
     * @param onlinePath
     */
    public void deletePhoto(String onlinePath) {
        StorageReference storageLocation = firebaseStorage.getReference(onlinePath);
        storageLocation.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("success", "Deleted image.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Failure","Did not delete image.");
            }
        });
    }

    /**
     * get the collection reference of user's mood list
     *
     * @return the reference pointing to user's moods
     */
    public CollectionReference getUserMoods() {
        return users.document(this.username).collection("moods");
    }

    /**
     * get mood list
     *
     * @return user's mood list
     */
    public ArrayList<Mood> getMoodHistory() {
        return moodHistory;
    }

    /**
     * listen to the mood list from cloud, sorted by reverse chronological order
     */
    public void addMoodListener(final FirebaseListener moodListener) {
        CollectionReference userMoods = this.getUserMoods();
        moodHistoryReg = userMoods.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    if (moodHistory.size() > 0) {
                        Mood recentMood = moodHistory.get(0);
                        recentMood.setUsername(username);
                        users.document(username).collection("SharedMood").document("sharedMood").set(recentMood);
                    } else {
                        users.document(username).collection("SharedMood").document("sharedMood").delete();
                    }
                    if (moodListener != null)
                        moodListener.onSuccess();
                }
            }
        });
    }

    /**
     * Detach all listeners no longer needed when quit the activity
     */
    public void destroyListener() {
        if (moodHistoryReg != null)
            moodHistoryReg.remove();
    }

    /**
     * get a list of users who you already applied for following
     *
     * @return an array list of users who you already applied for following
     */
    public ArrayList<String> getUserFollowing() {
        return userFollowing;
    }

    /**
     * get a list of your friends' mood
     *
     * @return an array list of shared moods from users who give you permission
     */
    public ArrayList<Mood> getSharedMood() {
        return sharedMoodHistory;
    }

    /**
     * check if user exist
     *
     * @param listener custom listener
     * @param username the user being checked
     */
    public void isUserExist(final FirebaseListener listener, String username) {
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
     * get a list of users who you already applied for following
     */
    public void fetchFollowList(final FirebaseListener listener) {
        userFollowing.clear();
        users.document(this.username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userFollowing = (ArrayList<String>) documentSnapshot.get("followList");
                if (userFollowing == null)
                    userFollowing = new ArrayList<>();
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
     * add a user to the list of users who you already applied for following
     *
     * @param username the username added to the list
     */
    public void setFollowList(String username, final FirebaseListener listener) {
        users.document(this.username).update("followList", FieldValue.arrayUnion(username)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
     *
     * @param incompleteMood Mood to be added, without its onlinePath initially
     * @param stringPath     Local path to photo file to be uploaded
     * @return
     */
    // Upload method uses information from Google's Firebase storage upload example: https://firebase.google.com/docs/storage/android/upload-files
    public Uri addPhotoAndMood(final Mood incompleteMood, final String stringPath) {
        Uri localPath = Uri.fromFile(new File(stringPath));
        final StorageReference reference = firebaseStorage.getReference();
        long millisecondTime = System.currentTimeMillis();
        final StorageReference storageLocation = reference.child("images/" + incompleteMood.hashCode() + ":" + getUsername() + ":" + millisecondTime);
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
                    Log.d("success", "Photo sucessfully uploaded.");
                    Mood completeMood = new Mood(incompleteMood);
                    completeMood.setOnlinePath(storageLocation.getPath());
                    addMood(completeMood);
                } else {
                    Log.e("Photo upload error", stringPath);
                }
            }
        });
        return null;
    }


    /**
     * Method for downloading a photo file from Firebase Storage
     * @param onlinePath The path to the photo to be downloaded
     * @param myContext The context for the file storage area to save the photo to
     * @param listener The listener to handle on success and failure results
     */
    // Uses elements of Google's Firebase Storage examples found here: https://firebase.google.com/docs/storage/web/download-files
    // Inspired by elements of StackOverflow post: https://stackoverflow.com/questions/39905719/how-to-download-a-file-from-firebase-storage-to-the-external-storage-of-android
    public void downloadPhoto(String onlinePath, final Context myContext, final FirebaseListener listener) {
        StorageReference storedAt = firebaseStorage.getReference(onlinePath);

        File path = new File(myContext.getFilesDir(), "file_name");
        if (!path.exists()) {
            path.mkdirs();
        }

        String[] splitString = onlinePath.split("/");
        String filename = splitString[splitString.length - 1];
        final File newFile = new File(path, filename);

        storedAt.getFile(newFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Database.this.downloadedImagePath = newFile.getPath();
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Failure","Failed to download photo.");
                listener.onFailure();
            }
        });
    }

    /**
     * get shared mood list from friends who give permission
     *
     * @param listener custom listener
     */
    public void getSharedMoodList(final FirebaseListener listener) {
        fetchAllowedFriendList(new FirebaseListener() {
            @Override
            public void onSuccess() {
                if (allowedFriendList.size() > 0) {
                    db.collectionGroup("SharedMood").whereIn("username", allowedFriendList).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            sharedMoodHistory.clear();
                            List<DocumentSnapshot> data;
                            if (queryDocumentSnapshots != null) {
                                data = queryDocumentSnapshots.getDocuments();

                                for (DocumentSnapshot d : data) {
                                    Mood mood = d.toObject(Mood.class);
                                    sharedMoodHistory.add(mood);
                                }
                                Collections.sort(sharedMoodHistory, Collections.<Mood>reverseOrder());
                                listener.onSuccess();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onFailure();
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                listener.onFailure();
            }
        });
    }

    /**
     * get all pending requests
     *
     * @param listener custom listener
     */
    public void fetchPendingRequests(final FirebaseListener listener) {
        users.document(this.username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pendingRequestsList.clear();
                ArrayList<String> pendingReq = (ArrayList<String>) documentSnapshot.get("pendingRequests");
                if (pendingReq != null)
                    pendingRequestsList.addAll((pendingReq));
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
     * send follow request to a user
     *
     * @param username the user you send the request to
     * @param listener custom listener
     */
    public void sendRequest(String username, final FirebaseListener listener) {
        users.document(username).update("pendingRequests", FieldValue.arrayUnion(this.username)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
     * get the pending request array list
     *
     * @return pending requests array list
     */
    public ArrayList<String> getPendingRequests() {
        return this.pendingRequestsList;
    }

    /**
     * accept others' following request
     *
     * @param username the user you accept the request from
     * @param listener custom listener
     */
    public void confirmFollowRequest(final String username, final FirebaseListener listener) {
        users.document(username).update("allowedFollowingUsers", FieldValue.arrayUnion(this.username)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                removeFollowRequest(username, listener);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure();
            }
        });
    }

    private void removeFollowRequest(String username, final FirebaseListener listener) {
        users.document(this.username).update("pendingRequests", FieldValue.arrayRemove(username)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        });
    }

    /**
     * get users who give you permission on following
     *
     * @return an array list of users who give you permission on following
     */
    public ArrayList<String> getAllowedFriendList() {
        return allowedFriendList;
    }

    /**
     * get all users who give you permission on following
     *
     * @param listener custom listener
     */
    public void fetchAllowedFriendList(final FirebaseListener listener) {
        users.document(this.username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                allowedFriendList.clear();
                ArrayList<String> allowedReq = (ArrayList<String>) documentSnapshot.get("allowedFollowingUsers");
                if (allowedReq != null)
                    allowedFriendList.addAll((allowedReq));
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
     * Gettere for downloadImagePath
     * @return
     */
    public String getDownloadedImagePath() {
        return downloadedImagePath;
    }

    /**
     * Setter for downloadedImagePath
     * @param downloadedImagePath
     */
    public void setDownloadedImagePath(String downloadedImagePath) {
        this.downloadedImagePath = downloadedImagePath;
    }
}
