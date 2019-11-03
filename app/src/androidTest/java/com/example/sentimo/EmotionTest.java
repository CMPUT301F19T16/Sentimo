package com.example.sentimo;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Emotions.Happy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for creating emotions. Makes sure that creating an emotion object
 *  is correct and that it can perform its methods.
 */

public class EmotionTest {

    private Emotion mockEmotion(){
        return new Happy();
    }

    @Test
    public void testGetMethods(){
        Emotion e = mockEmotion();
        assertEquals("#23E775", e.getColour());
        assertEquals("Happy", e.getName());
    }

}
