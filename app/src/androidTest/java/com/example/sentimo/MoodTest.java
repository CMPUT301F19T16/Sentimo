package com.example.sentimo;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MoodTest {
    @Test
    public void reworkTests() {
        // Must rework commented out tests for new Mood class
        Assert.assertEquals(false, true);
    }

//    private Mood mockMood() {
//        TimeFormatter timef = new TimeFormatter();
//        try {
//            timef.setTimeFormat("06/11/2019", "06:08");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return new Mood(timef, new Happy(), "test reason", new AloneSituation(), true);
//    }
//
//
//    @Test
//    public void testConstructor() {
//        Mood mood = mockMood();
//        assertNotNull(mood);
//    }
//
//    @Test
//    public void testSetTime() {
//        Mood mood = mockMood();
//        TimeFormatter timef = new TimeFormatter();
//        try {
//            timef.setTimeFormat("07/11/2019", "02:30");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        mood.setTime(timef);
//
//        assertEquals(mood.getTime().getDateString(), "07/11/2019");
//        assertEquals(mood.getTime().getTimeString(), "02:30");
//    }
//
//    @Test
//    public void testSetEmotion() {
//        Mood mood = mockMood();
//        Emotion e = new Sad();
//        mood.setEmotion(e);
//        assertEquals(mood.getEmotion(), e);
//    }
//
//    @Test
//    public void testSetReason() {
//        Mood mood = mockMood();
//        mood.setReason("a new reason");
//        assertEquals(mood.getReason(), "a new reason");
//    }
//
//    @Test
//    public void testSetSituation() {
//        Mood mood = mockMood();
//        Situation s = new CrowdSituation();
//        mood.setSituation(s);
//        assertEquals(mood.getSituation(), s);
//    }
//
//    @Test
//    public void testSetLocationPermission() {
//        Mood mood = mockMood();
//        mood.setLocationPermission(false);
//        assertEquals(mood.getLocationPermission(), false);
//    }
//
}
