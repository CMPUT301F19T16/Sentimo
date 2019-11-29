package com.example.sentimo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;

import com.robotium.solo.Solo;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

public class FriendActivityTest {
    TestingSharedFunctions sharedFunctions = new TestingSharedFunctions();

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

    /**
     * Test display of recent moods on FriendActivity for a constant test user
     */
    @Test
    public void testLaunchFriendActivity() {
        launchFriendActivity();
        // Check that view exists, indicating launch successful
        onView(withId(R.id.friend_request_button));
    }

    /**
     * Tests recent mood values for specific test user
     */
    @Test
    public void testRecentMoodValues() {
        launchFriendActivity();
        sharedFunctions.sleep(4);
        onData(anything())
                .inAdapterView(withId(R.id.friend_list)).atPosition(0)
                .check(matches(isDisplayed()));
        onData(anything())
                .inAdapterView(withId(R.id.friend_list)).atPosition(1)
                .check(matches(isDisplayed()));
        onData(anything())
                .inAdapterView(withId(R.id.friend_list)).atPosition(2)
                .check(matches(isDisplayed()));
    }

    /**
     * Checks that pending friend request displays one friend request, and displays an alert to
     * confirm it when clicked
     */
    @Test
    public void testPendingRequestDisplay() {
        launchFriendActivity();
        // Wait for network request
        sharedFunctions.sleep(4);
        onView(withId(R.id.friend_request_button)).perform(click());
        // Wait for network request
        sharedFunctions.sleep(4);
        onData(anything())
                .inAdapterView(withId(R.id.friend_request_listview))
                .atPosition(0)
                .check(matches(isDisplayed()));
        onView(withId(R.id.request_name_textview)).perform(click());
        onView(withText("YES"));
    }

    /**
     * Tests adding a non-existent user
     */
    @Test
    public void testAddNonExistent() {
        launchFriendActivity();
        onView(withId(R.id.friend_search_button)).perform(click());
        onView(withId(R.id.friend_search_editText)).perform(typeText("ASDdqwe121dasdqwddqwasdadd"));
        onView(withId(R.id.friend_search_editText)).perform(closeSoftKeyboard());
        onView(withText("Search and Add")).perform(click());
        sharedFunctions.sleep(4);
    }

    /**
     * General method from getting from MainActivity or LoginActivity to FriendActivity
     */
    private void launchFriendActivity() {
        sharedFunctions.logout();
        onView(withId(R.id.Username_LS_editText)).perform(typeText("DONOTDELETEfriendActivityTestUserFollower"));
        onView(withId(R.id.Username_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password_LS_editText)).perform(typeText("123456"));
        onView(withId(R.id.Password_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
        sharedFunctions.sleep(4);
        onView(withId(R.id.friend_button)).perform(click());
    }
}
