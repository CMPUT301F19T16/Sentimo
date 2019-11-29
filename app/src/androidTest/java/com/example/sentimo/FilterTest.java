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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a test class for the filter functionality on the main screen of
 * our app. It tests for many important functions of filter as well as some user
 * stories; namely, US 04.02.01. It tests for different situations that the user
 * can find themselves in including basic filtering, filtering an empty list,
 * deleting elements from a filtered list, editing elements of a filtered list,
 * and adding elements to a filtered list.
 */
public class FilterTest {
    private Solo solo;
    private ListView moodList;
    private int moodListId;

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * This is a function of the FilterTest that makes sure that a specific user is
     * logged on for the tests. It also makes sure that all the different tests
     * start with no moods at the beginning.
     */
    @Before
    public void setUp(){
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

    /**
     * This test looks at what happens when the user tries to filter an
     * empty list. Makes sure that filtered list has no elements in it.
     */
    @Test
    public void emptyIfEmpty(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_happy_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    /**
     * This test looks at what happens when the user filters for an emotion
     * not in the list when they already have an mood. The test makes a happy mood
     * be the only mood in the list and then attempts to filter for a
     * confident mood. The test makes sure that the filtered list is empty.
     */
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

    /**
     * This test looks at what happens in the normally situation where the user
     * filters for a emotion that the have in the mood list. Creates a happy
     * mood and then filters for happy moods. Makes sure that the filtered list
     * has one mood in it.
     */
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

    /**
     * This test looks at what happens when the user tries to add a mood while
     * they are currently in a filtered list. Specifically, it looks at what
     * happens when a user adds a mood that does not have the emotion that they
     * are filtered for. Creates a happy mood and goes to the happy filter, then
     * creates a sad mood sees if the filtered list contains the sad mood. Makes
     * sure that the list does not contain the sad mood by making sure that
     * the filtered list only has the one mood.
     */
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

    /**
     * This test looks at what happens when the user tries to add a mood while
     * they are currently in a filtered list. Specifically, it looks at what
     * happens when a user adds a mood that matches the filtered list that they
     * are in. Creates a happy mood and goes to the happy mood, then creates
     * another happy mood and sees if the happy mood is added to the filtered list.
     * Since it should be added, the test makes sure that the filtered list now
     * contains two moods.
     */
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

    /**
     * This test looks at what happens when the user tries to delete a mood from
     * the filtered list. Does this by first creating a happy mood and a sad mood,
     * then it goes to the happy filtered list and deletes the happy mood. Since
     * the mood should be deleted from the total mood list, it makes sure that
     * the mood list only contains the sad mood. Also makes sure that the
     * filtered happy list has no moods after the happy mood is deleted.
     */
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

    /**
     * This test looks at what happens when the user tries to edit a mood to a different
     * emotion in the filtered list. Creates a happy mood with the reason "Happy Reason" and then
     * filters by happy emotion. The test then changes the happy emotion to a confident
     * emotion. Makes sure that the happy filtered list is now empty. It then goes to the
     * confident filtered list and checks whether the "Happy Reason" is still the same. Makes
     * sure that it is.
     */
    @Test
    public void editEmotionFromFiltered() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withId(R.id.reason_editText)).perform(typeText("Happy Reason"));
        onView(withId(R.id.reason_editText)).perform(closeSoftKeyboard());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_happy_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        solo.clickInList(0);
        solo.waitForView(R.id.emotion_button);
        onView(withId(R.id.emotion_image)).perform(click());
        onView(withId(R.id.confidentButton)).perform(click());
        onView(withText("Confirm Edit")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 0);
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_confident_button)).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        solo.clickInList(0);
        solo.waitForView(R.id.emotion_button);
        onView(withId(R.id.reason_editText)).check(matches(withText("Happy Reason")));
    }
}
