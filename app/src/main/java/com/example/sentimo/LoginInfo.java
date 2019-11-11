package com.example.sentimo;

import android.util.Patterns;

public class LoginInfo {
    public static final int PASSWORD_MIN = 6;
    private String username;
    private String password;

    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static InputErrorType validUserNamePassword(String username, String password) {
        // Email validation method inspired by: https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
        boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(username).matches();
        if (!isValidEmail) {
            return InputErrorType.LoginUsernameNotValidEmailError;
        }
        if (password.length() < PASSWORD_MIN) {
            return InputErrorType.LoginPasswordTooShortError;
        }
        return InputErrorType.DataValid;
    }

}
