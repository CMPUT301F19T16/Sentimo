package com.example.sentimo;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Emotions.EmotionType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for creating emotions. Makes sure that creating an emotion object
 *  is correct and that it can perform its methods.
 */

public class EmotionTest {

    private Emotion mockEmotion(){
        return new Emotion(EmotionType.Happy);
    }

    @Test
    public void testEmotionConstructor(){
        Emotion emotion = mockEmotion();
        assertNotNull(emotion);
    }

    @Test
    public void getEmotionName(){
        Emotion emotion = mockEmotion();
        assertEquals("Happy", emotion.getName());
    }

    @Test
    public void getEmotionColour(){
        Emotion emotion = mockEmotion();
        assertEquals("#23E775", emotion.getColour());
    }

    @Test
    public void getEmotionImage(){
        Emotion emotion = mockEmotion();
        assertEquals(R.drawable.happy, emotion.getImage());
    }

}
