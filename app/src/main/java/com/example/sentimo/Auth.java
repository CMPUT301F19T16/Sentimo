package com.example.sentimo;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Auth {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Boolean isLogin() {
        return mAuth.getCurrentUser() == null;
    }

    public void createUser(final LoginInfo logininfo, final String email) {
        mAuth.createUserWithEmailAndPassword(email, logininfo.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // create user in database
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
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
}
