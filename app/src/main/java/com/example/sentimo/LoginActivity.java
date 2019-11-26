package com.example.sentimo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
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
                login();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignup();
            }
        });

    }


    /**
     * Validates entered username and password and login if a valid username and password combination is provided,
     * otherwise displays a warning.
     */
    public void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        InputErrorType warningType = LoginInfo.validUserNamePassword(username, password);
        if (warningType != InputErrorType.DataValid) {
            displayWarning(warningType);
            return;
        }
        Auth auth = new Auth(getApplicationContext());
        auth.loginUser(new LoginInfo(username, password));

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
}
