package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.fhnw.bedwaste.client.HotelDescriptiveInfoController;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class HotelInfoActivity extends AppCompatActivity {
    private static final String EXTRA_HOTEL_ID =
            "ch.fhnw.bedwaste.extra.hotel.id";
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelInfoActivity";
    private BottomNavigationView mBottomNavigationView;

    private TextView infoTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

        //receive values that got passed from previous activity
        final Intent intent = getIntent();
        String previous_activity_value = intent.getStringExtra("key");

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.image_linear);
        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            //imageView.setImageBitmap(BitmapFactory.decodeResource(
            //        getResources(), R.drawable.ic_launcher_foreground));
            imageView.setImageResource(R.drawable.ic_launcher_background);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }

    }

    public static Intent makeHotelInfoIntent(Context cont, String extra){
        Intent hotelViewIntent = new Intent(cont, HotelInfoActivity.class);
        hotelViewIntent.putExtra(EXTRA_HOTEL_ID, extra);
        return hotelViewIntent;
    }
}
