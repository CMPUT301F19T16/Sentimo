package com.example.sentimo;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference users = db.collection("users");
    private HashMap<Integer, Mood> moodHistory;


    public void getAllMoods() {
        CollectionReference userMoods = getUserMoods();
        userMoods.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        moodHistory.put(Integer.valueOf(document.getId()), (Mood) document.getData().get(document.getId()));
                    }
                } else {

                }
            }
        });

    }

    public void addMood(Mood mood) {
        getAllMoods();
        moodHistory.put(mood.hashCode(), mood);
//        getUserMoods().add(moodHistory);
    }

    public void deleteMood(Mood mood) {
        CollectionReference userMoods = getUserMoods();
    }

    private CollectionReference getUserMoods() {
//        TODO: Implement get by username
        return users.document("sentimoUser").collection("moods");
    }

}
