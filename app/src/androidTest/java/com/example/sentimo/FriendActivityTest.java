package com.example.sentimo;

import android.Manifest;
import android.app.AlertDialog;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class FriendActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Before
    public void init() {
    }

    @After
    public void cleanUp() {

    }

    @Test
    public void testLogout() {
        try {
            onView(withId(R.id.main_screen_username)).perform(click());
            onView(withText("YES")).perform(click());
        } catch (Exception e) {

        }
        onView(withId(R.id.Username_LS_editText)).perform(typeText("DONOTDELETEfriendActivityTestUserFollower"));
        onView(withId(R.id.Username_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password_LS_editText)).perform(typeText("123456"));
        onView(withId(R.id.Password_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
        try {
            Thread.sleep(4000);
        } catch (Exception e) {

        }
    }
}
