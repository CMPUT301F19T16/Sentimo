package com.example.sentimo;

import android.content.Context;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class FilterTest {
    private Solo solo;
    private ListView moodList;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityTestRule.getActivity());
        moodList = (ListView)solo.getView(R.id.mood_list);
    }

    private void removeOldMoods(){
        solo.waitForView(R.id.mood_list);
        while(moodList.getAdapter().getCount() > 0){
            solo.clickLongInList(0);
            solo.clickOnButton("YES");
            solo.waitForView(R.id.mood_list);
        }
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    @Test
    public void emptyIfEmpty(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Filter");
        solo.clickOnView(solo.getView(R.id.filter_happy_button));
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    @Test
    public void moodNotThere(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Filter");
        solo.clickOnView(solo.getView(R.id.filter_confident_button));
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    @Test
    public void moodThere(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Filter");
        solo.clickOnView(solo.getView(R.id.filter_happy_button));
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
    }

    @Test
    public void addMoodWhileDifferentFilter(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Filter");
        solo.clickOnView(solo.getView(R.id.filter_happy_button));
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.sadButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnView(solo.getView(R.id.filter_button));
        solo.clickOnView(solo.getView(R.id.filter_all_button));
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 2);
    }

    @Test
    public void addMoodWhileFiltered(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Filter");
        solo.clickOnView(solo.getView(R.id.filter_happy_button));
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 2);
    }

    @Test
    public void deleteFromFiltered(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.clickOnButton("Done");
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.sadButton));
        solo.clickOnButton("Done");
        solo.waitForView(moodList);
        solo.clickOnView(solo.getView(R.id.filter_button));
        solo.clickOnView(solo.getView(R.id.filter_all_button));
        solo.waitForView(moodList);
        assertEquals(moodList.getAdapter().getCount(), 2);
        solo.clickOnView(solo.getView(R.id.filter_button));
        solo.clickOnView(solo.getView(R.id.filter_happy_button));
        solo.waitForView(R.id.mood_list);
        solo.clickLongInList(0);
        solo.clickOnButton("YES");
        solo.waitForView(moodList);
        assertEquals(moodList.getAdapter().getCount(), 0);
        solo.clickOnView(solo.getView(R.id.filter_button));
        solo.clickOnView(solo.getView(R.id.filter_all_button));
        solo.waitForView(moodList);
        assertEquals(moodList.getAdapter().getCount(), 1);
    }
}
