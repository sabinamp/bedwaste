package ch.fhnw.bedwaste;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;


import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashActivityActivityTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    private SplashActivity splashActivity =null;

    @Before
    public void setUp() throws Exception {
        splashActivity = splashActivityActivityTestRule.getActivity();

    }

    @Test

    public void splash_screen(){

        View view = splashActivity.findViewById(R.id.imageView3);

        assertNotNull(view);


    }

    @After
    public void tearDown() throws Exception {

        splashActivity = null;
    }

}