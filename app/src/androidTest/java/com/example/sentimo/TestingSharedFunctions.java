package com.example.sentimo;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class TestingSharedFunctions {

    public TestingSharedFunctions() {
    }

    public void logout() {
        try {
            onView(withId(R.id.main_screen_username)).perform(click());
            onView(withText("YES")).perform(click());
        } catch (Exception e) {

        }
    }

    public void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {

        }
    }
}
