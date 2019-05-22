package ch.fhnw.bedwaste;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

public class HotelListViewActivity extends SingleFragmentActivity {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelListViewActivity";
    @Override
    protected Fragment createFragment() {
        return new HotelListFragment();
    }

    @Override
    protected void addClickListenersToBottomNav(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:
                    {
                        Intent listIntent= new Intent(HotelListViewActivity.this , LoginActivity.class);
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
        Intent hotelListIntent = new Intent(cont, HotelListViewActivity.class);
        return hotelListIntent;
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
        Log.d(TAG, "resuming back to the recycle view activity");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");
        super.onPause();

    }
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");

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
