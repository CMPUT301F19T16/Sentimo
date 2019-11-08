package com.example.sentimo;

import android.app.AlertDialog;
import android.os.SystemClock;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChangeMoodFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {

    }

    @After
    public void cleanUp() {

    }

    @Test
    public void testAddFragmentLaunch() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.add_mood_fragment)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testDismissAddFragment() {
        onView(withId(R.id.add_button)).perform(click());
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//
//        }
        onView(withText("Back")).perform(click());
        onView(withId(R.id.add_mood_fragment)).check(ViewAssertions.doesNotExist());
    }

    @Test
    public void testSelectEmotionLaunch() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.select_emoji_layout)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testDismissSelectEmotion() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.select_emoji_layout)).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.select_emoji_layout)).check(ViewAssertions.doesNotExist());
    }
}
