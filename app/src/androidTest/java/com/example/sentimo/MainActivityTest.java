package com.example.sentimo;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Test class for main activity
 */
public class MainActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    private void removeOldMoods() {
        solo.waitForView(R.id.mood_list);
        // delete all existing moods for testing
        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        int moodCount = moodList.getAdapter().getCount();
        for (int i = 0; i < moodCount; ++i) {
            solo.clickLongInList(0);
            solo.clickOnButton("YES");
            solo.waitForView(R.id.mood_list);
        }
        assertEquals(moodList.getAdapter().getCount(), 0);
    }

    /**
     * Test if adding a mood works
     */
    @Test
    public void addMood() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        // click on happy button
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Happy");
        // get current date and time to search for after adding
        TextView dateTextView = (TextView) solo.getView(R.id.date_text);
        TextView timeTextView = (TextView) solo.getView(R.id.time_text);
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();

        solo.clickOnButton("Done");
        assertTrue(solo.waitForText(date, 1, 2000));
        assertTrue(solo.waitForText(time, 1, 2000));

    }

    /**
     * Test if editing a mood works
     */
    @Test
    public void editMood() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // add a mood to edit
        removeOldMoods();
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        // click on sad button
        solo.clickOnView(solo.getView(R.id.sadButton));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Sad");
        // get current date and time to search for after adding
        TextView dateTextView = (TextView) solo.getView(R.id.date_text);
        TextView timeTextView = (TextView) solo.getView(R.id.time_text);
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();

        solo.clickOnButton("Done");
        assertTrue(solo.waitForText(date, 1, 2000));
        assertTrue(solo.waitForText(time, 1, 2000));

        // now edit the mood
        solo.clickInList(0);
        solo.clearEditText((EditText) solo.getView(R.id.reason_editText));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Somewhat sad");
        solo.clickOnButton("Confirm Edit");

        solo.clickInList(0);
        assertTrue(solo.waitForText("Somewhat sad", 1, 2000));
    }

    /**
     * Test if deleting a mood works
     */
    @Test
    public void deleteMood() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        removeOldMoods();

        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        // click on happy button
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Happy");
        // get current date and time to search for after adding
        TextView dateTextView = (TextView) solo.getView(R.id.date_text);
        TextView timeTextView = (TextView) solo.getView(R.id.time_text);
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();

        solo.clickOnButton("Done");
        assertTrue(solo.waitForText(date, 1, 2000));
        assertTrue(solo.waitForText(time, 1, 2000));

        solo.clickLongInList(0);
        solo.clickOnButton("YES");

        assertFalse(solo.waitForText(date, 1, 2000));
        assertFalse(solo.waitForText(time, 1, 2000));
    }

    /**
     * Test if moods are sorted reverse chronologically
     */
    @Test
    public void sortMood() {
        removeOldMoods();
        // add 3 moods to sort
        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.happyButton));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Happy");
        solo.clickOnButton("Done");

        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.sadButton));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Sad");
        solo.clickOnButton("Done");

        solo.clickOnButton("Add");
        solo.clickOnButton("Add Emotion");
        solo.clickOnView(solo.getView(R.id.tiredButton));
        solo.enterText((EditText) solo.getView(R.id.reason_editText), "Tired");
        solo.clickOnButton("Done");

        // edit the time of the moods randomly
        // edit 3 rounds and check final order
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                solo.clickInList(j);
                solo.clearEditText((EditText) solo.getView(R.id.time_text));
                solo.clearEditText((EditText) solo.getView(R.id.date_text));
                Random random = new Random();
                int day = random.nextInt(25) + 1;
                int month = random.nextInt(11) + 1;
                // generate a number between 0 to 12
                int hour = random.nextInt(12);
                // generate a number between 0 to 60
                int minute = random.nextInt(12);
                solo.enterText((EditText) solo.getView(R.id.time_text), String.format("%d:%d", hour, minute));
                solo.enterText((EditText) solo.getView(R.id.date_text), String.format("%d/%d/2019", day, month));
                solo.clickOnButton("Confirm Edit");
                solo.waitForView(R.id.mood_list);
            }
        }

        ListView moodList = (ListView) solo.getView(R.id.mood_list);
        Date lastDate = null;
        // check order one by one
        for (int i = 0; i < 3; ++i) {
            Mood mood = (Mood) moodList.getAdapter().getItem(i);
            Date thisDate = mood.getTime().getTime();
            if (lastDate != null) {
                // compare to returns a negative number when current date is earlier than last date
                assertTrue(thisDate.compareTo(lastDate) < 0);
            }
            lastDate = thisDate;
        }

    }
}

