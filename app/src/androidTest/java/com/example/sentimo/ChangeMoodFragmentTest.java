package com.example.sentimo;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.*;

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
