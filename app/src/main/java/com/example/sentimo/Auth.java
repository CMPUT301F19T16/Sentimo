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

public class Auth {
    private Context context;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    public Auth(Context context) {
        this.context = context;
        user = mAuth.getCurrentUser();
    }

    public Boolean isLogin() {
        return user != null;
    }

    public String getActiveUsername() {

        if (user != null)
            return user.getDisplayName();

        else
            return null;
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
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(logininfo.getUsername())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(context, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);
                                            }
                                        }
                                    });


                        } else {
                            Toast.makeText(context, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(context, "Username does not exist",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, loginInfo.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                } else {

                                    Toast.makeText(context, "Authorization failed.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }
        });

    }

    public void logoutUser() {
        mAuth.signOut();
    }
}
