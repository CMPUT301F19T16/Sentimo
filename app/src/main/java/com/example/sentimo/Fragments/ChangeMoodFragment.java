package com.example.sentimo.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.sentimo.FirebaseListener;
import com.example.sentimo.DisplayActivity;
import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.FriendActivity;
import com.example.sentimo.InputErrorType;
import com.example.sentimo.MainActivity;
import com.example.sentimo.Mood;
import com.example.sentimo.R;
import com.example.sentimo.Situations.Situation;
import com.example.sentimo.TimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A class to display all the details of a mood. Different subclasses support working with
 * brand new moods, editing old moods, and displaying mood information alone.
 */
public abstract class ChangeMoodFragment extends DialogFragment implements SelectSituationFragment.SelectSituationListener, SelectMoodFragment.SelectMoodFragmentInteractionListener,
                                                                            DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {
    protected TextView dateTextView;
    protected TextView timeTextView;
    protected ImageView emojiImageView;
    protected Button emojiImageButton;
    protected EditText reasonEditText;
    protected Button reasonImageButton;
    protected ImageView reasonImageView;
    protected Button situationButton;
    protected CheckBox locationCheckBox;

    protected Mood initialMood;
    protected String localImagePath;
    protected String downloadedImagePath;


    final private int GALLERY_ACTIVITY_REQUEST_CODE = 71;
    final private int CAMERA_ACTIVITY_REQUEST_CODE = 1;

    final private int NO_BEHAVIOUR_AFTER_PERMISSION_REQUEST_CODE = 1;
    final private int GALLERY_PERMISSION_REQUEST_CODE = 2;
    final private int CAMERA_PERMISSION_REQUEST_CODE = 3;
    final private int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4;
    String filePathToUse = null;


    protected View.OnClickListener emotionClick;
    protected View.OnClickListener situationClick;
    protected View.OnClickListener dateClick;
    protected View.OnClickListener timeClick;
    protected View view;

    /**
     * Initialization for ChangeMoodFragment dialog
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        // Should add TextWatchers

        emotionClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectMoodFragment().show(getChildFragmentManager(), "SELECT_MOOD");
            }
        };

        situationClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectSituationFragment().show(getChildFragmentManager(), "SELECT_SITUATION");
            }
        };

        dateClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getChildFragmentManager(), "SELECT_DATE");
            }
        };

        timeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment().show(getChildFragmentManager(), "SELECT_TIME");
            }
        };

        sharedInitialization();
        subclassInitialization();

        AlertDialog.Builder builder = returnBuilder();
        AlertDialog dialog = builder.create();
        dialog.show();
        // Method for reassigning positive button clicker to avoid automatic dismissal found at
        // StackOverflow post:https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE).getText() != getString(R.string.friend_mood_display_fragment_positive_text)) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (localImagePath != null) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            return;
                        }
                    }
                    String date = dateTextView.getText().toString();
                    String time = timeTextView.getText().toString();
                    InputErrorType errorCode = isDataValid();
                    if (errorCode != InputErrorType.DataValid) {
                        displayWarning(errorCode);
                        return;
                    }
                    TimeFormatter timef = new TimeFormatter();
                    try {
                        timef.setTimeFormat(date, time);
                    } catch (ParseException e) {
                        return;
                    }
                    String reason = reasonEditText.getText().toString();
                    Location location = null;
                    if (locationCheckBox.isChecked()) {
                        location = subclassLocationReturnBehaviour();
                    }
                    Double longitude = null;
                    Double latitude = null;
                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }
                    Mood myMood = new Mood(timef, ChangeMoodFragment.this.initialMood.getEmotion(),
                            reason, ChangeMoodFragment.this.initialMood.getSituation(), longitude,
                            latitude, ChangeMoodFragment.this.initialMood.getOnlinePath());
                    callListener(myMood);
                    ChangeMoodFragment.this.dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setId(R.id.change_mood_fragment_positive_button);
        } else {
            // Disable submit button for display only view
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
        }

        return dialog;
    }

    /**
     * Method for receiving an Emotion back from an emotion selection fragment
     * @param emotion the Emotion to receive and process, if any
     */
    @Override
    public void EmotionReturned(Emotion emotion) {
        if (emotion != null) {
            initialMood.setEmotion(emotion);
            emojiImageButton.setText(initialMood.getEmotion().getName());
            emojiImageButton.setVisibility(View.INVISIBLE);
            emojiImageView.setVisibility(View.VISIBLE);
            emojiImageView.setImageResource(emotion.getImage());
            emojiImageView.setBackgroundColor(Color.parseColor(emotion.getColour()));
        }
    }

    /**
     * Method for receiving a Situation back from a situation selection fragment
     * @param situation the Situation to receive and process, if any
     */
    @Override
    public void SituationReturned(Situation situation) {
        initialMood.setSituation(situation);
        if (situation != null) {
            situationButton.setText(situation.getName());
        } else {
            situationButton.setText(R.string.no_situation_text);
        }
    }

    @Override
    public void DateReturned(int year, int month, int day) {
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.CANADA);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        dateTextView.setText(df.format(cal.getTime()));
    }

    @Override
    public void TimeReturned(int hourOfDay, int minute) {
        SimpleDateFormat df = new SimpleDateFormat("h:mm a", Locale.CANADA);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        timeTextView.setText(df.format(cal.getTime()));
    }

    /**
     *Shared initialization between subclasses
     *Separate non-constructor function required to allow hookup of UI before initialization
     */
    private void sharedInitialization() {
        localImagePath = null;

        if (ChangeMoodFragment.this instanceof AddMoodFragment) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.add_mood_fragment, null);
        } else if (ChangeMoodFragment.this instanceof EditMoodFragment) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_mood_fragment, null);
        } else if (ChangeMoodFragment.this instanceof FriendMoodDisplayFragment) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.add_mood_fragment, null);
        } else {
            throw new RuntimeException("CHANGE MOOD FRAGMENT RECEIVED UNKNOWN SUBCLASS");
        }

        dateTextView = view.findViewById(R.id.date_text);
        timeTextView = view.findViewById(R.id.time_text);
        emojiImageView = view.findViewById(R.id.emotion_image);
        emojiImageButton = view.findViewById(R.id.emotion_button);
        reasonEditText = view.findViewById(R.id.reason_editText);
//        reasonImageButton = view.findViewById(R.id.reason_image_button);
        reasonImageView = view.findViewById(R.id.reason_image);
        situationButton = view.findViewById(R.id.situation_button);
        locationCheckBox = view.findViewById(R.id.location_checkbox);

        if (initialMood.getOnlinePath() != null) {
            downloadAndSetThumbnail();
        }

        emojiImageButton.setOnClickListener(emotionClick);
        emojiImageView.setOnClickListener(emotionClick);

        situationButton.setOnClickListener(situationClick);

        dateTextView.setOnClickListener(dateClick);
        timeTextView.setOnClickListener(timeClick);

        reasonImageButton = view.findViewById(R.id.reason_image_button);
        reasonImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        reasonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPhotoForMood();
            }
        });
    }

    /**
     * Method for displaying menu to choose method of image selection
     */
    private void selectImage() {
        // Method inspired by this Stack Overflow post: https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
        AlertDialog.Builder menuDialogBuilder = new AlertDialog.Builder((MainActivity)getContext());
        menuDialogBuilder.setTitle("Select Photo");
        ArrayList<String> tempMenuOptions = new ArrayList<>();
        tempMenuOptions.add(getString(R.string.take_picture_option));
        tempMenuOptions.add(getString(R.string.select_picture_gallery_option));
        if (initialMood.getOnlinePath() != null) { tempMenuOptions.add(getString(R.string.use_online_picture_option)); }
        final String[] menuOptionsTitles = tempMenuOptions.toArray(new String[tempMenuOptions.size()]);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }
        menuDialogBuilder.setItems(menuOptionsTitles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (menuOptionsTitles[item].equals(getString(R.string.select_picture_gallery_option))) {
                    //Photo selection launch inspired by: https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, GALLERY_PERMISSION_REQUEST_CODE);
                        return;
                    }
                    launchGalleryIntent();
                } else if (menuOptionsTitles[item].equals(getString(R.string.take_picture_option))) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                        return;
                    }
                    launchCameraIntent();
                } else if (menuOptionsTitles[item].equals(getString(R.string.use_online_picture_option))) {
                    if (initialMood.getOnlinePath() != null) {
                        localImagePath = null;
                        downloadAndSetThumbnail();
                    }
                }
            }
        });
        menuDialogBuilder.show();
    }

    // Data processing of returned image partially inspired by: https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
    /**
     * Handles activity result returns from the photo selection activity (either from gallery or
     * from camera)
     * @param requestCode the type of request
     * @param resultCode status code for result return
     * @param data data returned from the activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_ACTIVITY_REQUEST_CODE && resultCode == -1 && data != null && data.getData() != null ) {
            String contentImagePath = data.getData().toString();
            Uri myUri = Uri.parse(contentImagePath);
            localImagePath = getAbsolutePathFromContentPathUri(myUri);
            setThumbnail(localImagePath);
        } else if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE && resultCode == -1){
            localImagePath = filePathToUse;
            if (localImagePath != null) {
                setThumbnail(localImagePath);
            }
        } else {
            Log.e("Log", "Activity Result case not handled");
        }
    }


    /**
     * Method for chaining further actions after permission request granted
     * @param requestCode The internal code for the type of request
     * @param permissions Permissions involved
     * @param grantResults Result of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                launchCameraIntent();
            } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
                launchGalleryIntent();
            } else if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
                selectImage();
            }
        }
    }

    /**
     * Launches activity to select photo from gallery
     */
    private void launchGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Photograph for Reason"), GALLERY_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Launches activity to take picture for photo with camera
     */
    private void launchCameraIntent() {
        File photoFile = createImageFile();
        if (photoFile != null) {
            Uri photoToUri = FileProvider.getUriForFile(getContext(), "com.example.sentimo.fileprovider", photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoToUri);
            startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
        }
    }

    //Method taken from Google documentation found here: https://developer.android.com/training/camera/photobasics
    /**
     * Creates a unique file path for an image to be saved
     * @return Returns a file for the image
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            Log.d("Log", "EXCEPTION FOR FILE CREATION");
        }

        // Save a file: path for use with ACTION_VIEW intents
        filePathToUse = image.getAbsolutePath();
        return image;
    }

    /**
     * Displays a warning for invalid date of type specified by warningType
     * @param warningType the code for the type of warning to display
     */
    public void displayWarning(InputErrorType warningType) {
        new InvalidDataWarningFragment(warningType).show(getChildFragmentManager(), null);
    }

    /**
     * Checks if data is valid, returns and appropriate data invalid code if not
     * @return integer code indication type of data invalidity, or 0 if data valid
     */
    public InputErrorType isDataValid() {
        if (initialMood.getEmotion() == null) {
            return InputErrorType.CMFNullMoodError;
        }
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();
        TimeFormatter timef = new TimeFormatter();
        try {
            timef.setTimeFormat(date, time);
        } catch (ParseException e) {
            return InputErrorType.CMFTimeParseError;
        }
        String reason = reasonEditText.getText().toString();
        if (reason.length() > 20) {
            return InputErrorType.CMFReasonTooLongError;
        }
        int spaceCount = 0;
        for (int i = 0; i < reason.length(); i++) {
            if (reason.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        if (spaceCount > 2) {
            return InputErrorType.CMFReasonTooManyWordsError;
        }
        if (locationCheckBox.isChecked() & ChangeMoodFragment.this instanceof AddMoodFragment) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return InputErrorType.CMFNoLocationPermission;
            }

        }
        if ((initialMood.getOnlinePath() != null || localImagePath != null) && (reasonEditText.getText().toString().length() != 0)) {
            return InputErrorType.CMFPictureAndReasonError;
        }
        return InputErrorType.DataValid;
    }


    /**
     * Launches an activity to display a large image of the current photo associated with the Mood
     */
    public void displayPhotoForMood() {
        if (localImagePath != null) {
            displayLocalImage(localImagePath);
        } else if (initialMood.getOnlinePath() != null) {
            // Start progress bar with timeout
            downloadAndSetPath(new FirebaseListener() {
                @Override
                public void onSuccess() {
                    displayLocalImage(downloadedImagePath);
                }

                @Override
                public void onFailure() {

                }
            });
            // End progress bar with timeout
        }
    }

    /**
     * Downloads a photo from Firebase Storage, if no local copy exists, and calls the provided
     * listener for further action after download
     * @param listener
     */
    public void downloadAndSetPath(FirebaseListener listener) {
        // Path set in Database class download method before calling onSuccess listener
        // Only downloads if local copy not already present
        if (downloadedImagePath != null) {
            listener.onSuccess();
        } else {
            if (ChangeMoodFragment.this instanceof FriendMoodDisplayFragment) {
                FriendActivity act = (FriendActivity)getContext();
                act.database.downloadPhoto(initialMood.getOnlinePath(), this, listener);
            } else {
                MainActivity act = (MainActivity) getContext();
                act.database.downloadPhoto(initialMood.getOnlinePath(), this, listener);
            }
        }
    }

    /**
     * Downloads a photo associated with the Mood from Firebase storage and sets it as the thumbnail
     */
    public void downloadAndSetThumbnail() {
        downloadAndSetPath(new FirebaseListener() {
            @Override
            public void onSuccess() {
                setThumbnail(downloadedImagePath);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * Sets the current local filepath for an image downloaded from Firebase storage
     * @param filepath path to be set as the downloaded image path
     */
    public void setDownloadedImagePath(String filepath) {
        this.downloadedImagePath = filepath;
    }

    /**
     * Sets the thumbnail image to the photo currently associated with the Mood
     * @param localPath path to copy of photo stored locally
     */
    public void setThumbnail(String localPath) {
        Bitmap myBitmap = BitmapFactory.decodeFile(localPath);
        reasonImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        reasonImageView.setImageBitmap(myBitmap);
        reasonImageView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    /**
     * Displays photo for provided local path
     * @param localPath path to copy of photo stored locally
     */
    public void displayLocalImage(String localPath) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        } else {
            Intent intent = new Intent(getActivity(), DisplayActivity.class);
            intent.putExtra("localPath", localPath);
            getActivity().startActivity(intent);
        }
    }

    // Content link parsing method taken from: https://stackoverflow.com/questions/2789276/android-get-real-path-by-uri-getpath/9989900

    /**
     * Converts a local content link to an absolute file path
     * @param contentURI content link to be converted
     * @return absolute file path from the converted content link
     */
    private String getAbsolutePathFromContentPathUri(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * Initialization specific to subclass
     * Require separate function rather than constructor for ordering reasons
     */
    protected abstract void subclassInitialization();

    /**
     * Listener for dismissing back to activity with a created Mood
     * @param mood: the mood created by the ChangeMoodFragment
     */
    protected abstract void callListener(Mood mood);

    /**
     * Returns a builder for the AlertDialog specific to the subclass
     * @return
     */
    protected abstract AlertDialog.Builder returnBuilder();


    /**
     * Returns the location for the mood to be created
     * @return
     */
    protected abstract Location subclassLocationReturnBehaviour();
}
