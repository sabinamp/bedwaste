package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    public static final String PROFILE_EXTRA_MESSAGE = "ch.fhnw.bedwaste.PROFILE_MESSAGE";
    private BottomNavigationView mBottomNavigationView;
    private ConnectionDetector cd = new ConnectionDetector(ProfileActivity.this);

    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "Login Activity - onCreate(Bundle) called");
      //  TextView txtView = findViewById(R.id.profile_activity_text);

     //   addBottomNav();

        cd.internetRunnable.run();
    }

    private void addBottomNav() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile: {
                        //current screen
                        return true;
                    }
                    case R.id.app_bar_hotel_list: {
                        Intent listIntent = HotelListViewActivity.makeHotelListIntent(ProfileActivity.this);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view: {
                        Intent welcomeIntent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

    public static Intent makeProfileIntent(Context cont, String userId){
        Intent profileIntent= new Intent(cont, LoginActivity.class);
        //add user id to the intent
        profileIntent.putExtra(PROFILE_EXTRA_MESSAGE, userId);
        return profileIntent;
    }
}
