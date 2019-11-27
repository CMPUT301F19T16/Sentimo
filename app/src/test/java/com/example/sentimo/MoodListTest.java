package com.example.sentimo;

import org.junit.Assert;
import org.junit.Test;

public class MoodListTest {
    @Test
    public void reworkTests() {
        // Must rework commented out tests for new Mood class
        Assert.assertEquals(false, true);
    }

//    private List<Mood> mockMoodList(){
//        List<Mood> moodList = new ArrayList<>();
//        moodList.add(mockMood());
//        return moodList;
//    }

//    private Mood mockMood(){
//        TimeFormatter timef = new TimeFormatter();
//        try{
//            timef.setTimeFormat("01/01/2019", "03:07");
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
//        return new Mood(timef, new Happy(), "test reason", new AloneSituation(), false);
//    }
//
//    @Test
//    public void testAddMood(){
//        List<Mood> moods = mockMoodList();
//        Assert.assertEquals(1, moods.size());
//
//        TimeFormatter timef = new TimeFormatter();
//        try{
//            timef.setTimeFormat("01/02/2019", "03:08");
//        } catch (ParseException e){
//            e.printStackTrace();
//        }
//        Mood mood = new Mood(timef, new Sad(), "", new OnePersonSituation(), true);
//        moods.add(mood);
//
//        Assert.assertEquals(2, moods.size());
//    }
//
//    @Test
//    public void testRemoveMood(){
//        List<Mood> moods = mockMoodList();
//        TimeFormatter timef = new TimeFormatter();
//        try{
//            timef.setTimeFormat("02/05/2019", "12:12");
//        } catch (ParseException e){
//            e.printStackTrace();
//        }
//        Mood mood = new Mood(timef, new Tired(), "blah", new CrowdSituation(), false);
//        Mood mood2 = new Mood(timef, new Worried(), "", new SeveralPeopleSituation(), true);
//
//        moods.add(mood);
//        moods.add(mood2);
//
//        Assert.assertEquals(3, moods.size());
//
//        moods.remove(mood);
//
//        Assert.assertEquals(2, moods.size());
//
//        moods.remove(mood2);
//
//        Assert.assertEquals(1, moods.size());
//    }
}
