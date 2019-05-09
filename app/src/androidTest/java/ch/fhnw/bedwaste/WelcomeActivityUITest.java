package ch.fhnw.bedwaste;

import android.location.Location;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class WelcomeActivityUITest {
    @Rule
    public ActivityTestRule<WelcomeActivity> welcomeActivityTestRule = new ActivityTestRule<WelcomeActivity>(WelcomeActivity.class);

    private WelcomeActivity welcomeActivity =null;


    @Before
    public void setUp() throws Exception {
        welcomeActivity = welcomeActivityTestRule.getActivity();

    }
    @Test
    public void getCustomLocationTest () {
        Location loc = welcomeActivity.getLastKnownLocation();
        LatLng latlng = new LatLng(loc.getLatitude(), loc.getLongitude());
        onView(withId(R.id.input_location))
                .perform(typeText("Zurich"));
        onView(withId(R.id.search))
                .perform(click());
        LatLng locnew = welcomeActivity.getLocationFromAddress("Zurich");
        assertThat(latlng,not(locnew));



    }

    @Test
    public void AnimationTest() {

        onView(withId(R.id.app_bar_profile))
                .perform(click());
        onView(withId(R.id.buttonAlsGastFortfahren))
                .perform(click());
        onView(withId(R.id.secondHelpBox))
                .check(matches(isDisplayed()));
        onView(withId(R.id.firstHelpBox))
                .check(matches(isDisplayed()));
        onView(withId(R.id.secondHelpBox))
                .perform(click());
        onView(withId(R.id.bottom_navigation))
                .check(matches(isDisplayed()));
        onView(withId(R.id.input_location))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkAnimationNotPresent () {
        onView(withId(R.id.firstHelpBox))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.secondHelpBox))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void checkFilterTest (){
        onView(withId(R.id.filters_btn))
                .perform(click());
        onView(withId(R.id.layoutFilters))
                .check(matches(
                        isDisplayed()));
        onView(withId(R.id.btnExpandFilter))
                .perform(click());
        onView(withId(R.id.checkBoxWlan))
                .check(
                        matches(isDisplayed()));
        onView(withId(R.id.imageApply))
                .perform(click());
        onView(withId(R.id.layoutFilters))
                .check(matches(
                        not(isDisplayed())
                ));
    }



}
