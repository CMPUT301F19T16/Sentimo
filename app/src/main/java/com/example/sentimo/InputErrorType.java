package com.example.sentimo;

/**
 * Error types for the application
 */
public enum InputErrorType {
    DataValid,
    CMFNullMoodError,
    CMFTimeParseError,
    CMFReasonTooLongError,
    CMFReasonTooManyWordsError,
    CMFPictureAndReasonError,
    CMFNoLocationPermission,
    CMFPhotoReturnError,
    LoginPasswordTooShortError,
    LoginUsernameTooShortError,
    EmailNotValidError,
    ImageDisplayError,
    CMFNoCameraPermission
}
