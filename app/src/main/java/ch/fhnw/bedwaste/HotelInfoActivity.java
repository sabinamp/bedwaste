package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.ContactInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.MultimediaDescription;
import ch.fhnw.bedwaste.model.MultimediaDescriptionImages;
import ch.fhnw.bedwaste.model.Phone;
import ch.fhnw.bedwaste.model.Service;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Response;


public class HotelInfoActivity extends SingleFragmentActivity {
    private static final String EXTRA_HOTEL_ID =
            "ch.fhnw.bedwaste.extra.hotel.id";
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelInfoActivity";
    //private NetworkDetector netDetector = new NetworkDetector(HotelInfoActivity.this);


    @Override
    protected Fragment createFragment() {
        return new HotelInfoFragment();
    }

    @Override
    protected void addClickListenersToBottomNav(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:{
                        Intent loginIntent= LoginActivity.makeProfileIntent(HotelInfoActivity.this);
                        startActivity(loginIntent);
                        return true;
                    }
                    case R.id.app_bar_hotel_list: {
                        Intent listIntent= HotelListViewActivity.makeHotelListIntent(HotelInfoActivity.this);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view:
                    {
                        Intent welcomeIntent= new Intent(HotelInfoActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

    public static Intent makeHotelInfoIntent(Context cont, String extra){
        Intent hotelViewIntent = new Intent(cont, HotelInfoActivity.class);
        hotelViewIntent.putExtra(EXTRA_HOTEL_ID, extra);
        return hotelViewIntent;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called");
        super.onResume();
    }
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }
    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");

        super.onPause();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
