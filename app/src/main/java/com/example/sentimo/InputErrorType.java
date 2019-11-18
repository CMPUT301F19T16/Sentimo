package com.example.sentimo;

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
    ImageDisplayError
}
