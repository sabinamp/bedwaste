package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import ch.fhnw.bedwaste.client.HotelDescriptiveInfoController;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class HotelInfoActivity extends AppCompatActivity {
    private static final String EXTRA_HOTEL_ID =
            "ch.fhnw.bedwaste.extra.hotelid";
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "HotelInfoActivity";
    private BottomNavigationView mBottomNavigationView;
    private HotelDescriptiveInfoController controller;
    private TextView responseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);
        responseTextView = (TextView) findViewById(R.id.text_view_result);
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
                        Intent listIntent= new Intent(HotelInfoActivity.this, HotelListViewActivity.class);
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
        controller = new HotelDescriptiveInfoController();
        controller.start("en", "00U5846f022c291a");
        String error = controller.getErrorCode();
        if(!(error.isEmpty() || error == null)){
            responseTextView.setText(error);
            Log.d(TAG, "HotelListViewActivity Activity - could not fetch data from the server");
        } else {
            HotelDescriptiveInfo hotelInfo = controller.getHotelinfo();
            String content = "";
            content +=  hotelInfo.getHotelName() +"\n";
            responseTextView.setText(content);
            Log.d(TAG, "HotelListViewActivity Activity - fetched data from the server");
        }
    }

    public static Intent makeHotelInfoIntent(Context cont, String extra){
        Intent hotelViewIntent = new Intent(cont, HotelInfoActivity.class);
        hotelViewIntent.putExtra(EXTRA_HOTEL_ID, extra);
        return hotelViewIntent;
    }
}
