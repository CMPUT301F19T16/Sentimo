package com.example.sentimo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sentimo.Fragments.InvalidDataWarningFragment;

/**
 * Activity for getting username and passwords to signup new users
 * and create a segment in the Firebase database for them
 */
public class SignupActivity extends AppCompatActivity {
    private Button submitSignup;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Auth auth;
    private boolean allowLogin = true;

    /**
     * Initialization of Activity and UI hookup
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);

        auth = new Auth(getApplicationContext());
        if (auth.isLogin()) {
            auth.reloadUser();
            finish();
        }
        submitSignup = findViewById(R.id.submit_signup_button);
        usernameEditText = findViewById(R.id.Username_SP_editText);
        passwordEditText = findViewById(R.id.Password_SP_editText);
        emailEditText = findViewById(R.id.Email_SP_editText);

        submitSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowLogin) {
                    signup();
                }
            }
        });
    }


    /**
     * Create new account with given email, username and password and log in if successful,
     * otherwise displays a warning.
     */
    private void signup() {
        allowLogin = false;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        InputErrorType warningType = LoginInfo.validUserNamePassword(username, password);
        if (warningType != InputErrorType.DataValid) {
            displayWarning(warningType);
            allowLogin = true;
            return;
        }
        // TODO: Validate email
        auth.createUser(new LoginInfo(username, password), email, new FirebaseListener() {
            @Override
            public void onSuccess() {
                auth.reloadUser();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "Registration failed. Username or email may be registered.",
                        Toast.LENGTH_SHORT).show();
                allowLogin = true;
            }
        });
        if (auth.isLogin()) {
            auth.reloadUser();
            finish();
        }
    }

    /**
     * Displays a warning message for invalid data based on the type of data issue
     *
     * @param warningType The type of data issue for the warning
     */
    private void displayWarning(InputErrorType warningType) {
        new InvalidDataWarningFragment(warningType).show(getSupportFragmentManager(), null);
    }
}
