package com.example.sentimo;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

// A class for testing data validation feature on ChangeMoodFragment
public class InvalidDataWarningFragmentTest {
    Context context;
    TestingSharedFunctions sharedFunctions = new TestingSharedFunctions();


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        this.context = activityRule.getActivity().getApplicationContext();
    }

    @After
    public void cleanUp() {

    }

    /**
     * Test for submitting a Mood with no emotion selected by user
     */
    @Test
    public void testNullEmotion() {
        sharedFunctions.logout();
        sharedFunctions.login("DONOTDELETEtestUserFriendActivity", "123456");
        sharedFunctions.sleep(4);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.change_mood_fragment_positive_button)).perform(click());
        onView(allOf(withId(R.id.warning_text),withText(context.getString(R.string.warning_CMFNullMoodError)))).check(ViewAssertions.matches(isDisplayed()));
    }


    /**
     * Test for submitting too many words
     */
    @Test
    public void testReasonTooManyWords() {
        sharedFunctions.logout();
        sharedFunctions.login("DONOTDELETEtestUserFriendActivity", "123456");
        sharedFunctions.sleep(4);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withId(R.id.reason_editText)).perform(typeText("1 2 3 4"));
        onView(withId(R.id.reason_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.change_mood_fragment_positive_button)).perform(click());
        onView(allOf(withId(R.id.warning_text),withText(context.getString(R.string.warning_CMFReasonTooManyWordsError)))).check(ViewAssertions.matches(isDisplayed()));
    }
}
