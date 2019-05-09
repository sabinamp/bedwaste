package ch.fhnw.bedwaste;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import androidx.test.rule.ActivityTestRule;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;


import static org.junit.Assert.*;

public class WelcomeActivityTest {

    @Rule
    public ActivityTestRule<WelcomeActivity> welcomeActivityTestRule = new ActivityTestRule<WelcomeActivity>(WelcomeActivity.class);

    private WelcomeActivity welcomeActivity =null;


    @Before
    public void setUp() throws Exception {
        welcomeActivity = welcomeActivityTestRule.getActivity();

    }

    @Test
    public void check_default_people(){
        TextView people_nr = welcomeActivity.findViewById(R.id.textPeopleValue);
        String people_nr_string = people_nr.getText().toString();

        Assert.assertEquals(people_nr_string, "1");
    }

    @Test
    public void check_default_rooms(){
        TextView room_nr = welcomeActivity.findViewById(R.id.textRoomsValue);
        String room_nr_string = room_nr.getText().toString();

        Assert.assertEquals(room_nr_string, "1");
    }

    @Test
    public void AnimationTest() {
        boolean  Startup = welcomeActivity.getIntent().getBooleanExtra("Startup",false);
        int VisibilityHelpBox1 = welcomeActivity.findViewById(R.id.firstHelpBox).getVisibility();
        int VisibilityHelpBox2 = welcomeActivity.findViewById(R.id.secondHelpBox).getVisibility();
        assertEquals(VisibilityHelpBox1, VisibilityHelpBox2);
        if (Startup){
            assertEquals(VisibilityHelpBox1, View.VISIBLE);
        }
        else {
            assertEquals(VisibilityHelpBox1, View.INVISIBLE);
        }

    }



    @After
    public void tearDown() throws Exception {
        welcomeActivity = null;
    }
}
