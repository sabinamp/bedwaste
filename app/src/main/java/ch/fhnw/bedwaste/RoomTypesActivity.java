package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;


public class RoomTypesActivity extends SingleFragmentActivity {
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
}
