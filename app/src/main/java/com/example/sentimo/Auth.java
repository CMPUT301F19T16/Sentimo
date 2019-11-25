package com.example.sentimo;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A class that handles user authentication
 */

public class Auth {
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    /**
     * Create a new Auth object
     *
     * @param context the application context, which will be used for redirecting into main activity
     */
    public Auth(Context context) {
        this.context = context;
        user = mAuth.getCurrentUser();
    }

    /**
     * Displays the main activity when the current user is valid
     */
    public void reloadUser() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Check if a user has login
     *
     * @return the current user is valid
     */
    public Boolean isLogin() {
        return user != null;
    }

    /**
     * Get the username of the active user
     *
     * @return username of the active user if a user has logged in, otherwise null
     */
    public String getActiveUsername() {

        if (isLogin())
            return user.getDisplayName();

        else
            return null;
    }

    /**
     * Create a user using the given combination
     *
     * @param logininfo username and password of the new user
     * @param email     email of the new user
     */
    public void createUser(final LoginInfo logininfo, final String email) {
        db.collection("users").document(logininfo.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Toast.makeText(context, "Username exists",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, logininfo.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // create user in database
                                    HashMap<String, String> userDocument = new HashMap<>();
                                    userDocument.put("email", email);
                                    db.collection("users").document(logininfo.getUsername()).set(userDocument);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(logininfo.getUsername())
                                            .build();

                                    if (user != null)
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            reloadUser();
                                                        }
                                                    }
                                                });
                                } else {
                                    Toast.makeText(context, "Registration failed. Email may be registered.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    /**
     * Logs in with given username and password if valid
     *
     * @param loginInfo username and password of a user
     */
    public void loginUser(final LoginInfo loginInfo) {
        db.collection("users").document(loginInfo.getUsername()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String email = documentSnapshot.getString("email");
                if (email == null) {
                    Toast.makeText(context, "Username does not exist",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, loginInfo.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    reloadUser();
                                } else {

                                    Toast.makeText(context, "Authorization failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    /**
     * Sign out the current user
     */
    public void logoutUser() {
        mAuth.signOut();
    }
}
