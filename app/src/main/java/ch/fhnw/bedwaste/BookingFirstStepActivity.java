package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import ch.fhnw.bedwaste.model.GuestRoom;


public class BookingFirstStepActivity extends SingleFragmentActivity {

    /**
     * Debugging tag BookingFistStep_Activity used by the Android logger.
     */
    private static final String TAG = "BookingFistS_Activity";

    //private static final String EXTRA_BOOKING_HOTEL_ID ="ch.fhnw.bedwaste.booking_extra.hotel.id";
    private static final String EXTRA_BOOKING_AVAILABILITYRESULT ="ch.fhnw.bedwaste.booking_extra.room";
    @Override
    protected Fragment createFragment() {
        return new BookingFirstStepFragment();
    }

    @Override
    protected void addClickListenersToBottomNav(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:{
                        Intent loginIntent= LoginActivity.makeProfileIntent(BookingFirstStepActivity.this);
                        startActivity(loginIntent);
                        return true;
                    }
                    case R.id.app_bar_hotel_list: {
                        /*Intent listIntent= HotelListViewActivity.makeHotelListIntent(BookingFirstStepActivity.this);
                        startActivity(listIntent);*/
                        finish();
                        return true;
                    }
                    case R.id.app_bar_map_view:
                    {
                        Intent welcomeIntent= new Intent(BookingFirstStepActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

    }
    public static Intent makeBookingFirstStepIntent(Context cont, GuestRoom roomItemExtra){
        Intent bookingFirstStepIntent = new Intent(cont, BookingFirstStepActivity.class);
        bookingFirstStepIntent.putExtra("guestroom_for_booking_first_step_activity", roomItemExtra);

        return bookingFirstStepIntent;
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
