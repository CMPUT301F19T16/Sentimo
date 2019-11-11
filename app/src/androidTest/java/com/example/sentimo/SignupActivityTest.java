package com.example.sentimo;

import android.content.Context;

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

/**
 * A class for testing the SignupActivity
 */
public class SignupActivityTest {
    private Context context;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void init() {
        this.context = activityRule.getActivity().getApplicationContext();
    }

    @After
    public void cleanUp() {

    }

    /**
     * Tests rejection of a non-existent username
     */
    @Test
    public void testNoUsername() {
        onView(withId(R.id.button_sign_upLoginScreen)).perform(click());
        onView(withId(R.id.submit_signup_button)).perform(click());
        onView(allOf(withId(R.id.warning_text),withText(context.getString(R.string.warning_LoginUsernameTooShortError)))).check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Tests rejection of a username that is too short
     */
    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.button_sign_upLoginScreen)).perform(click());
        onView(withId(R.id.Username_SP_editText)).perform(typeText("hello"));
        onView(withId(R.id.Username_SP_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.submit_signup_button)).perform(click());
        onView(allOf(withId(R.id.warning_text),withText(context.getString(R.string.warning_LoginUsernameTooShortError)))).check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Tests rejection of a non-existent password with a valid username
     */
    @Test
    public void noPassword() {
        onView(withId(R.id.button_sign_upLoginScreen)).perform(click());
        onView(withId(R.id.Username_SP_editText)).perform(typeText("hello@test.com"));
        onView(withId(R.id.Username_SP_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.submit_signup_button)).perform(click());
        onView(allOf(withId(R.id.warning_text),withText(context.getString(R.string.warning_LoginPasswordTooShortError)))).check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Tests rejection of a password that is too short
     */
    @Test
    public void tooShortPassword() {
        onView(withId(R.id.button_sign_upLoginScreen)).perform(click());
        onView(withId(R.id.Username_SP_editText)).perform(typeText("hello@test.com"));
        onView(withId(R.id.Username_SP_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password_SP_editText)).perform(typeText("hello"));
        onView(withId(R.id.Password_SP_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.submit_signup_button)).perform(click());
        onView(allOf(withId(R.id.warning_text),withText(context.getString(R.string.warning_LoginPasswordTooShortError)))).check(ViewAssertions.matches(isDisplayed()));
    }
}
