package com.example.sentimo;

import android.Manifest;
import android.widget.ListView;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertEquals;

@LargeTest
public class ChangeMoodFragmentTest {
    private Solo solo;
    private int moodListId;
    private ListView moodList;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
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

//    @Test
//    public void testNoLocationPermissionWarningAddMoodFragment() {
//        onView(withId(R.id.add_button)).perform(click());
//    }
}
