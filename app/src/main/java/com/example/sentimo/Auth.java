package com.example.sentimo;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Auth {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Boolean isLogin() {
        return mAuth.getCurrentUser() == null;
    }

    private String getUserEmail(String username) {
        DocumentSnapshot userDocument = db.collection("users").document(username).get().getResult();
        if (userDocument == null)
            return "";
        return userDocument.getString("email");
    }

    public void createUser(final LoginInfo logininfo, final String email) {
        mAuth.createUserWithEmailAndPassword(email, logininfo.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // create user in database
                            HashMap<String, String> userDocument = new HashMap<>();
                            userDocument.put("email", email);
                            db.collection("users").document(logininfo.getUsername()).set(userDocument);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createUser", "createUserWithEmail:failure", task.getException());


                        }

                    }
                });
    }

    public void loginUser(final LoginInfo loginInfo) {
        db.collection("users").document(loginInfo.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String email = documentSnapshot.getString("email");
                if (email == null) {
//                    TODO: warn username does not exist
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, loginInfo.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("loginUser", "signInWithEmail:success");

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("loginUser", "signInWithEmail:failure", task.getException());
//                                     TODO: warn login failure

                                }

                             
                            }
                        });
            }
        });

    }
}
