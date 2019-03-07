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
import android.widget.TextView;

import ch.fhnw.bedwaste.client.HotelDescriptiveInfoController;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class HotelListViewActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "HotelListViewActivity";
    private TextView responseTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list_view);
        Log.d(TAG, "HotelListViewActivity Activity - onCreate(Bundle) called");
        responseTextView = findViewById(R.id.text_view_result);
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

        HotelDescriptiveInfoController controller = new HotelDescriptiveInfoController();
        controller.start();
        controller.getDescriptiveInfo("en", "00U5846f022c291a");
        HotelDescriptiveInfo hotelinfo = controller.getHotelinfo();
        String content = "";
        content +=  hotelinfo.getHotelName() +"\n";
        content +=  hotelinfo.getContactInfos()+"\n";
        responseTextView.append(content);

}

    public static Intent makeHotelListIntent(Context cont){
        Intent profileIntent= new Intent(cont, ProfileActivity.class);
        return profileIntent;
    }




}
