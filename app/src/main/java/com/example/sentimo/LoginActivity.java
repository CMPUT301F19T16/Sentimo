package com.example.sentimo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sentimo.Fragments.InvalidDataWarningFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Class for displaying the login page and taking username and password
// and sending it to the Firebase database for authentication
public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginSubmitButton;
    private Button signupButton;
    private Button skipButton;

    /**
     * Initial activity setup
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        usernameEditText = findViewById(R.id.Username_LS_editText);
        passwordEditText = findViewById(R.id.Password_LS_editText);
        loginSubmitButton = findViewById(R.id.button_login);
        signupButton = findViewById(R.id.button_sign_upLoginScreen);
        skipButton = findViewById(R.id.skip_login_button);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLoginInfo();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignup();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipLogin();
            }
        });

//        skipLogin();

    }

    /**
     * Returns a LoginInfo object with the EditText field values for username and password
     * if the provided data is valid, otherwise return null
     * @return LoginInfo indicating the provided username and password field values, or null if data invalid
     */
    public void returnLoginInfo() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        InputErrorType warningType = LoginInfo.validUserNamePassword(username, password);
        if (warningType != InputErrorType.DataValid) {
            displayWarning(warningType);
            return;
        }
        Auth auth = new Auth();
        auth.loginUser(new LoginInfo(username, password));
    }

    /**
     * Displays a warning message specific to the warningCode
     * @param warningCode  The code for the warning message to be printed, specific to the data issue
     */
    private void displayWarning(InputErrorType warningCode) {
        new InvalidDataWarningFragment(warningCode).show(getSupportFragmentManager(), null);
    }

    private void skipLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the signup page for new users to input a username and password for a new account
     */
    private void launchSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
