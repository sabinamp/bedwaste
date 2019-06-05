package ch.fhnw.bedwaste;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {
    private LoginActivity loginActivity;
    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp(){
        loginActivity = loginActivityTestRule.getActivity();
    }

    @Test
    public void viewComponentsNotNull(){
        EditText emailtxtField= loginActivity.findViewById(R.id.emailText);
        EditText passwordTxt = loginActivity.findViewById(R.id.passwordText);
        Assert.assertNotNull(emailtxtField);
        Assert.assertNotNull(passwordTxt);
    }
    @After
    public void tearDown(){

       loginActivity = null;
    }
}
