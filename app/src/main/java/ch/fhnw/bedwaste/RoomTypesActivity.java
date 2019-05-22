package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import retrofit2.Response;


public class RoomTypesActivity extends SingleFragmentActivity {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="RoomTypesActivity";


    @Override
    protected Fragment createFragment() {
        return new RoomTypesFragment();
    }

    @Override
    protected void addClickListenersToBottomNav(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_hotel_list: {
                        Intent listIntent = HotelListViewActivity.makeHotelListIntent(RoomTypesActivity.this);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view: {
                        Intent listIntent = new Intent(RoomTypesActivity.this, WelcomeActivity.class);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_profile: {

                        Intent profileIntent = new Intent(RoomTypesActivity.this, LoginActivity.class);
                        startActivity(profileIntent);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

}
