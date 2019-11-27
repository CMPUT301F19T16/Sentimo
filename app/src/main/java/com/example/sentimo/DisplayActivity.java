package com.example.sentimo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sentimo.Fragments.InvalidDataWarningFragment;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for generating a large view of a photo
 */
public class DisplayActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button backButton;

    /**
     * Initialization and UI hookup
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_activity);

        Intent intent  = getIntent();
        String imageFileName = intent.getStringExtra("localPath");

        Bitmap myBitmap = null;
        myBitmap = BitmapFactory.decodeFile(imageFileName);

        imageView = findViewById(R.id.photo_image_view);
        imageView.setImageBitmap(myBitmap);

        backButton = findViewById(R.id.display_photo_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
