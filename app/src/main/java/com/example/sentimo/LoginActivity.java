package com.example.sentimo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sentimo.Fragments.InvalidDataWarningFragment;

/**
 * Class for displaying the login page and taking username and password
 * and sending it to the Firebase database for authentication
 */

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginSubmitButton;
    private Button signupButton;
    private Auth auth;
    private boolean allowLogin = true;
    private Button resetEmailButton;

    /**
     * Initial activity setup
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        auth = new Auth(getApplicationContext());
        if (auth.isLogin()) {
            auth.reloadUser();
            finish();
        }
        usernameEditText = findViewById(R.id.Username_LS_editText);
        passwordEditText = findViewById(R.id.Password_LS_editText);
        loginSubmitButton = findViewById(R.id.button_login);
        signupButton = findViewById(R.id.button_sign_upLoginScreen);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowLogin)
                    login();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignup();
            }
        });

        resetEmailButton = findViewById(R.id.reset_email_button);
        resetEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchResetEmailDialog();
            }
        });
    }


    /**
     * Validates entered username and password and login if a valid username and password combination is provided,
     * otherwise displays a warning.
     */
    public void login() {
        allowLogin = false;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        InputErrorType warningType = LoginInfo.validUserNamePassword(username, password);
        if (warningType != InputErrorType.DataValid) {
            displayWarning(warningType);
            allowLogin = true;
            return;
        }
        final Auth auth = new Auth(getApplicationContext());
        auth.loginUser(new LoginInfo(username, password), new FirebaseListener() {
            @Override
            public void onSuccess() {
                auth.reloadUser();
                finish();
            }

            @Override
            public void onFailure() {
                allowLogin = true;
                Toast.makeText(getApplicationContext(), "Authorization failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Displays a warning message specific to the warningCode
     *
     * @param warningCode The code for the warning message to be printed, specific to the data issue
     */
    private void displayWarning(InputErrorType warningCode) {
        new InvalidDataWarningFragment(warningCode).show(getSupportFragmentManager(), null);
    }


    /**
     * Launches the signup page for new users to input a username and password for a new account
     */
    private void launchSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    /**
     * Method to launch reset email dialog
     */
    private void launchResetEmailDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        final EditText resetEmailEditText = new EditText(this);
        resetEmailEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(resetEmailEditText);
        alert.setMessage("Please enter the email for the account to reset.");
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = resetEmailEditText.getText().toString();
                // Email validation inspired by this Stack Overflow post: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
                if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    auth.sendResetEmail(email, new FirebaseListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(LoginActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Not a valid email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }
}
