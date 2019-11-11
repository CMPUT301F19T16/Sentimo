package com.example.sentimo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sentimo.Fragments.InvalidDataWarningFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for getting username and passwords to signup new users
 * and create a segment in the Firebase database for them
 */
public class SignupActivity extends AppCompatActivity {
    Button submitSignup;
    EditText usernameEditText;
    EditText passwordEditText;

    /**
     * Initialization of Activity and UI hookup
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);

        submitSignup = findViewById(R.id.submit_signup_button);
        usernameEditText = findViewById(R.id.Username_SP_editText);
        passwordEditText = findViewById(R.id.Password_SP_editText);

        submitSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSignupInfo();
            }
        });
    }

    /**
     * Return LoginInfo with field values for username and password if they are valid inputs
     * Else, return null
     * @return  LoginInfo with the username and password field values, or null if invalid
     */
    private LoginInfo returnSignupInfo() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        InputErrorType warningType = LoginInfo.validUserNamePassword(username, password);
        if (warningType == InputErrorType.DataValid) {

        } else {
            displayWarning(warningType);
            return null;
        }
        return new LoginInfo(username, password);
    }

    /**
     * Displays a warning message for invalid data based on the type of data issue
     * @param warningType The type of data issue for the warning
     */
    private void displayWarning(InputErrorType warningType) {
        new InvalidDataWarningFragment(warningType).show(getSupportFragmentManager(), null);
    }
}
