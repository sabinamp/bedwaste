package ch.fhnw.bedwaste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HotelInfoActivity extends AppCompatActivity {
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "HotelInfoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);
    }
}
