package com.example.sentimo;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

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

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void addMood() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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

}

