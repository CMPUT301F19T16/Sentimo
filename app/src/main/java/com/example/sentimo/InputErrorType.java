package com.example.sentimo;

public enum InputErrorType {
    DataValid,
    CMFNullMoodError,
    CMFTimeParseError,
    CMFReasonTooLongError,
    CMFReasonTooManyWordsError,
    LoginPasswordTooShortError,
    LoginUsernameNotValidEmailError
}
