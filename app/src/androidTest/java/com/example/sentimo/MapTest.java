package com.example.sentimo;

import android.Manifest;
import android.app.Activity;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * This is a test class for the Map functionality in the Sentimo app; namely
 * US 06.01.01, US 06.02.01, and US 06.03.01.
 * Tests for how many locations the map is displaying by using the getNumber
 * function in the DisplayMapActivity.
 * Tests for if the user has no moods, a mood with no location, one mood with a location,
 * two moods with location, and if the user has three friends, one with a mood but no location,
 * another with a mood and a location, and one without any moods.
 */
public class MapTest {
    private Solo solo;
    private ListView moodList;
    private ArrayList<Mood> moods;

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);


    /**
     * This function is called before each test. It makes sure that the test runs on a
     * specific account and that the account is empty.
     */
    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), testRule.getActivity());

        try{
            onView(withId(R.id.main_screen_username)).perform(click());
            onView(withText("YES")).perform(click());
        } catch (Exception e){

        }
        onView(withId(R.id.Username_LS_editText)).perform(typeText("DONOTDELETEMap"));
        onView(withId(R.id.Username_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.Password_LS_editText)).perform(typeText("password"));
        onView(withId(R.id.Password_LS_editText)).perform(closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());

        solo.waitForView(R.id.mood_list);
        moodList = (ListView)solo.getView(R.id.mood_list);
        while(moodList.getAdapter().getCount() > 0){
            solo.clickLongInList(0);
            onView(withText("YES")).perform(click());
            solo.waitForView(R.id.mood_list);
        }
        assertEquals(moodList.getAdapter().getCount(), 0);

    }

    /**
     * This function tests the map function if the user has no moods. Makes sure that
     * the number of mood locations displayed on the map is zero.
     */
    @Test
    public void TestEmptyMap(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.map_button)).perform(click());
        onView(withId(R.id.my_map_button)).perform(click());
        solo.waitForView(R.id.map);
        try{
            Thread.sleep(2000);
        }catch (Exception e){ }
        DisplayMapActivity map = (DisplayMapActivity)solo.getCurrentActivity();
        assertEquals(map.getNumber(), 0);
    }

    /**
     * This function tests the map function if the user has a mood, but no location
     * associated with that mood. Makes sure that the number of mood locations
     * displayed on the map is zero.
     */
    @Test
    public void TestMyMapWithNoLocation() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.map_button)).perform(click());
        onView(withId(R.id.my_map_button)).perform(click());
        solo.waitForView(R.id.map);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        DisplayMapActivity map = (DisplayMapActivity) solo.getCurrentActivity();
        assertEquals(map.getNumber(), 0);
    }

    /**
     * This function tests the map function if the user has a mood with a location.
     * Makes sure that the number of mood locations displayed on the map is one.
     */
    @Test
    public void TestMyMapWith1Location(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withId(R.id.location_checkbox)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.map_button)).perform(click());
        onView(withId(R.id.my_map_button)).perform(click());
        solo.waitForView(R.id.map);
        try{
            Thread.sleep(2000);
        }catch (Exception e){ }
        DisplayMapActivity map = (DisplayMapActivity)solo.getCurrentActivity();
        assertEquals(map.getNumber(), 1);
    }

    /**
     * This function tests the map function if the user has two separate moods, each
     * having a location. Makes sure that the number of mood locations displayed
     * on the map is two.
     */
    @Test
    public void TestMyMapWith2Locations(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.happyButton)).perform(click());
        onView(withId(R.id.location_checkbox)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        assertEquals(moodList.getAdapter().getCount(), 1);
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.emotion_button)).perform(click());
        onView(withId(R.id.tiredButton)).perform(click());
        onView(withId(R.id.location_checkbox)).perform(click());
        onView(withText("Done")).perform(click());
        solo.waitForView(R.id.mood_list);
        onView(withId(R.id.map_button)).perform(click());
        onView(withId(R.id.my_map_button)).perform(click());
        solo.waitForView(R.id.map);
        try{
            Thread.sleep(2000);
        }catch (Exception e){ }
        DisplayMapActivity map = (DisplayMapActivity)solo.getCurrentActivity();
        assertEquals(map.getNumber(), 2);
    }

    /**
     * This function tests the friend map functionality (US 06.03.01). The tested user has
     * three friends, one with no moods, one with a mood that doesn't have an associated
     * location, and a friend with a mood that has a location. Makes sure that the number of
     * mood locations displayed on the map is one.
     */
    @Test
    public void TestFriendMapMoods(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.map_button)).perform(click());
        onView(withId(R.id.friend_map_button)).perform(click());
        solo.waitForView(R.id.map);
        try{
            Thread.sleep(2000);
        }catch (Exception e){}
        DisplayMapActivity map = (DisplayMapActivity)solo.getCurrentActivity();
        assertEquals(map.getNumber(), 1);
    }
}
