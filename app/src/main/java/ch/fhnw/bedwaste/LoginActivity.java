package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.file.WatchEvent;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_EXTRA_MESSAGE = "ch.fhnw.bedwaste.LOGIN_MESSAGE";
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "Login Activity - onCreate(Bundle) called");
        EditText editTxt = (EditText) findViewById(R.id.emailText);
        EditText passwordTxt = (EditText) findViewById(R.id.passwordText);
        Button loginBtn = (Button) findViewById(R.id.buttonLogin);
        Button passwordForgotBtn = findViewById(R.id.buttonPasswordForgot);
        Button registerBtn = findViewById(R.id.buttonRegistrieren);
        Button alsGastFortfahrenBtn = (Button) findViewById(R.id.buttonAlsGastFortfahren);
        alsGastFortfahrenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToWelcomeIntent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(backToWelcomeIntent);
            }
            });

    }



    public static Intent makeProfileIntent(Context cont){
        Intent loginIntent= new Intent(cont, LoginActivity.class);

        return loginIntent;
    }
}
