package com.example.sentimo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class FilterTest {
    private Solo solo;
    private ListView moodList;
    private int moodListId;

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), testRule.getActivity());
        moodListId = R.id.mood_list;
        try{
            onView(withId(R.id.main_screen_username)).perform(click());
            onView(withText("YES")).perform(click());
        } catch (Exception e){

        }
        onView(withId(R.id.Username_LS_editText)).perform(typeText("DONOTDELETEFilter"));
        onView(withId(R.id.Username_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password_LS_editText)).perform(typeText("password"));
        onView(withId(R.id.Password_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());

        solo.waitForView(moodListId);
        moodList = (ListView)solo.getView(moodListId);
        while(moodList.getAdapter().getCount() > 0){
            solo.clickLongInList(0);
            onView(withText("YES")).perform(click());
            solo.waitForView(R.id.mood_list);
        }
        assertEquals(moodList.getAdapter().getCount(), 0);
    }


    @After
    public void cleanUp(){
    }


    @Test
    public void emptyIfEmpty(){

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_happy_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    @Test
    public void moodNotThere(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_confident_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    @Test
    public void moodThere(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.filter_button));
        onView(withId(R.id.filter_happy_button));
        assertEquals(moodList.getAdapter().getCount(), 1);
    }

    @Test
    public void addMoodWhileDifferentFilter(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_happy_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.sadButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_all_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 2);
    }

    @Test
    public void addMoodWhileFiltered(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_happy_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 2);
    }

    @Test
    public void deleteFromFiltered(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.sadButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(moodList);
        assertEquals(moodList.getAdapter().getCount(), 2);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_happy_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        solo.clickLongInList(0);
        onView(withText("YES")).perform(click());
        solo.waitForView(moodList);
        assertEquals(moodList.getAdapter().getCount(), 0);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_all_button)).perform(click());
        solo.waitForView(moodList);
        assertEquals(moodList.getAdapter().getCount(), 1);
    }
}
