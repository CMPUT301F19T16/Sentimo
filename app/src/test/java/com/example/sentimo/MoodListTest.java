package com.example.sentimo;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Emotions.EmotionType;
import com.example.sentimo.Situations.Situation;
import com.example.sentimo.Situations.SituationType;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MoodListTest {


    private List<Mood> mockMoodList(){
        List<Mood> moodList = new ArrayList<>();
        moodList.add(mockMood());
        return moodList;
    }

    private Mood mockMood(){
        TimeFormatter timef = new TimeFormatter();
        try{
            timef.setTimeFormat("Nov 28, 2019", "7:21 PM");
        }catch (ParseException e){
            e.printStackTrace();
        }
        return new Mood(timef, new Emotion(EmotionType.Happy), "test reason", new Situation(SituationType.Alone),
                        53.5, 113.5, null);
    }

    @Test
    public void testAddMood(){
        List<Mood> moods = mockMoodList();
        Assert.assertEquals(1, moods.size());

        TimeFormatter timef = new TimeFormatter();
        try{
            timef.setTimeFormat("Nov 29, 2019", "3:08 AM");
        } catch (ParseException e){
            e.printStackTrace();
        }
        Mood mood = new Mood(timef, new Emotion(EmotionType.Mad), null,
                            new Situation(SituationType.SeveralPeople), null,
                    null, "images/123456/...");
        moods.add(mood);

        Assert.assertEquals(2, moods.size());
    }

    @Test
    public void testRemoveMood(){
        List<Mood> moods = mockMoodList();
        TimeFormatter timef = new TimeFormatter();
        try{
            timef.setTimeFormat("Feb 26, 2019", "12:12 AM");
        } catch (ParseException e){
            e.printStackTrace();
        }
        Mood mood = new Mood(timef, new Emotion(EmotionType.Tired), "blah", null,
                    50.3, 112.5, null);
        Mood mood2 = new Mood(timef, new Emotion(EmotionType.Worried), null, null,
                    null, null, null);

        moods.add(mood);
        moods.add(mood2);

        Assert.assertEquals(3, moods.size());

        moods.remove(mood);

        Assert.assertEquals(2, moods.size());

        moods.remove(mood2);

        Assert.assertEquals(1, moods.size());
    }
}
