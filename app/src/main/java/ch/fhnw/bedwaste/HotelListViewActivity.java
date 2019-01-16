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

public class HotelListViewActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "HotelListViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list_view);
        Log.d(TAG, "HotelListViewActivity Activity - onCreate(Bundle) called");
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:
                    {
                        Intent listIntent= new Intent(HotelListViewActivity.this, ProfileActivity.class);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view:
                    {
                        Intent welcomeIntent= new Intent(HotelListViewActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        return true;
                    }
                    case R.id.app_bar_hotel_list: {

                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

    public static Intent makeHotelListIntent(Context cont){
        Intent profileIntent= new Intent(cont, ProfileActivity.class);
        return profileIntent;
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
                Intent profileIntent= new Intent(HotelListViewActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            }
            case R.id.app_bar_hotel_list: {
                //nt sure we have list view btn on the profile screen-to be designed
                //maybe we have bookings-
                return true;
            }
            case R.id.app_bar_map_view:
            {
                Intent welcomeIntent= new Intent(HotelListViewActivity.this, WelcomeActivity.class);
                startActivity(welcomeIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
