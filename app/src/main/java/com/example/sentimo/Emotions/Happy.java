package com.example.sentimo.Emotions;

import com.example.sentimo.R;

/**
 * This is a class that creates a Happy emotion
 * Extends the Emotion superclass and passes it specific parameters
 */
public class Happy extends Emotion {
    public Happy(){
        super("Happy", "#23E775", R.drawable.happy);
    }
}
