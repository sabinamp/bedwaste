package ch.fhnw.bedwaste;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BookingFirstStepActivityTest {
    private BookingFirstStepActivity lastActivity;

    @Rule
    public final ActivityTestRule<BookingFirstStepActivity> loginActivityTestRule = new ActivityTestRule<>(BookingFirstStepActivity.class);

    @Before
    public void setUp(){
        lastActivity = loginActivityTestRule.getActivity();
    }

    @Test
    public void viewComponentsNotNull(){
        EditText nametxtField= lastActivity.findViewById(R.id.input_name);
        EditText firstNametxtField = lastActivity.findViewById(R.id.input_firstname);
        EditText phoneTxtField = lastActivity.findViewById(R.id.input_tel);
        Assert.assertNotNull(nametxtField);
        Assert.assertNotNull(firstNametxtField);
        Assert.assertNotNull(phoneTxtField);
    }
    @After
    public void tearDown(){

        lastActivity = null;
    }
}
