package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_EXTRA_MESSAGE = "ch.fhnw.bedwaste.LOGIN_MESSAGE";
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "LoginActivity";
    private String userId;
    private String userPassword;
    private boolean credentialsOK  = false;
    private NetworkDetector netDetector = new NetworkDetector(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "Login Activity - onCreate(Bundle) called");
        EditText emailTxt = (EditText) findViewById(R.id.emailText);
        emailTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText passwordTxt = (EditText) findViewById(R.id.passwordText);
        passwordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userPassword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button loginBtn = (Button) findViewById(R.id.buttonLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to be updated to cover the correct login logic-checking if the user id and password match a registered user's credentials
                if(credentialsOK)
                login(userId);
            }
        });
        Button passwordForgotBtn = findViewById(R.id.buttonPasswordForgot);
        Button registerBtn = findViewById(R.id.buttonRegistrieren);
        Button alsGastFortfahrenBtn = (Button) findViewById(R.id.buttonAlsGastFortfahren);
        alsGastFortfahrenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToWelcomeIntent = new Intent(LoginActivity.this, WelcomeActivity.class);
                backToWelcomeIntent.putExtra("Startup", true);
                startActivity(backToWelcomeIntent);
            }
            });

        netDetector.networkRunnable.run();
    }



    public static Intent makeProfileIntent(Context cont){
        Intent loginIntent= new Intent(cont, LoginActivity.class);

        return loginIntent;
    }

    private void login(String userId){
        Intent profileIntent = ProfileActivity.makeProfileIntent(LoginActivity.this, userId);
    }
}
