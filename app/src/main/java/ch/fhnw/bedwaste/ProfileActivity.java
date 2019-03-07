package ch.fhnw.bedwaste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    public static final String PROFILE_EXTRA_MESSAGE = "ch.fhnw.bedwaste.PROFILE_MESSAGE";
    private BottomNavigationView mBottomNavigationView;
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "Profile Activity - onCreate(Bundle) called");
        TextView textProfile= (TextView) findViewById(R.id.profile_activity_text);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:{
                        //current screen
                        return true;
                    }
                    case R.id.app_bar_hotel_list: {
                        Intent listIntent= new Intent(ProfileActivity.this, HotelListViewActivity.class);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view:
                    {
                        Intent welcomeIntent= new Intent(ProfileActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.bottom_bar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.app_bar_profile:{
                //current screen
                return true;
            }
            case R.id.app_bar_hotel_list: {
                Intent listIntent= new Intent(ProfileActivity.this, HotelListViewActivity.class);
                startActivity(listIntent);
                return true;
            }
            case R.id.app_bar_map_view:
            {
                Intent welcomeIntent= new Intent(ProfileActivity.this, WelcomeActivity.class);
                startActivity(welcomeIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public static Intent makeProfileIntent(Context cont){
        Intent profileIntent= new Intent(cont, ProfileActivity.class);
        //add user id to the intent
        profileIntent.putExtra(PROFILE_EXTRA_MESSAGE, "1123");
        return profileIntent;
    }
}
