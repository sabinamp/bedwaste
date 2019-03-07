package ch.fhnw.bedwaste;

import android.app.Activity;
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


public class HotelListViewActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private Button btn;
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "HotelListViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list_view);
        Log.d(TAG, "HotelListViewActivity Activity - onCreate(Bundle) called");
        btn = (Button) findViewById(R.id.btn_hotel_view);
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
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent hotelViewIntent=  HotelInfoActivity.makeHotelInfoIntent(HotelListViewActivity.this,"00U5846f022c291a" );
            startActivity(hotelViewIntent);
        }
    });
    }

    public static Intent makeHotelListIntent(Context cont){
        Intent hotelListIntent = new Intent(cont, HotelListViewActivity.class);
        return hotelListIntent;
    }




}
