package com.example.sentimo;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Emotions.EmotionType;
import com.example.sentimo.Situations.Situation;
import com.example.sentimo.Situations.SituationType;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MoodTest {

    private Mood mockMood() {
        TimeFormatter timef = new TimeFormatter();
        try {
            timef.setTimeFormat("Nov 12, 2019", "6:08 PM");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Mood(timef, new Emotion(EmotionType.Happy), "Reason",
                          new Situation(SituationType.Alone), 53.5, 113.5,
                "images/54654/...");
    }


    @Test
    public void testConstructor() {
        Mood mood = mockMood();
        assertNotNull(mood);
    }

    @Test
    public void testSetTime() {
        Mood mood = mockMood();
        TimeFormatter timef = new TimeFormatter();
        try {
            timef.setTimeFormat("Nov 28, 2019", "6:59 PM");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mood.setTime(timef);

        assertEquals(mood.getTime().getDateString(), "Nov 28, 2019");
        assertEquals(mood.getTime().getTimeString(), "6:59 PM");
    }

    @Test
    public void testSetEmotion() {
        Mood mood = mockMood();
        Emotion e = new Emotion(EmotionType.Mad);
        mood.setEmotion(e);
        assertEquals(mood.getEmotion(), e);
    }

    @Test
    public void testSetReason() {
        Mood mood = mockMood();
        mood.setReason("a new reason");
        assertEquals(mood.getReason(), "a new reason");
    }

    @Test
    public void testSetSituation() {
        Mood mood = mockMood();
        Situation s = new Situation(SituationType.SeveralPeople);
        mood.setSituation(s);
        assertEquals(mood.getSituation(), s);
    }

    @Test
    public void testSetLongitudeLatitude(){
        Mood mood = mockMood();
        Double longitude = 50.5;
        Double latitude = 110.5;
        mood.setLongitudeLatitude(longitude, latitude);
        assertEquals(mood.getLongitude(), longitude);
        assertEquals(mood.getLatitude(), latitude);
    }

    @Test
    public void testSetOnlinePath(){
        Mood mood = mockMood();
        // Since this is a string, it really shouldn't matter what it is, as long as
        // the function can actually set it properly
        String onlinePath = "images/...";
        mood.setOnlinePath(onlinePath);
        assertEquals(mood.getOnlinePath(), onlinePath);
    }

    @Test
    public void TestSetUsername(){
        Mood mood = mockMood();
        String username = "new_username";
        mood.setUsername(username);
        assertEquals(mood.getUsername(), username);
    }

    @Test
    public void TestMoodEquals(){
        Mood mood1 = mockMood();
        assertEquals(mood1.equals(mood1), true);
    }

}
