package ch.fhnw.bedwaste;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfoPosition;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    /**
     * Debugging tag Splash Activity used by the Android logger.
     */
    private static final String TAG="SplashActivity";
    //Time before disappearance in ms
    private static int SPLASH_TIME_OUT = 3000;
    // Keys for storing data to internal storage.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                    Intent homeIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    homeIntent.putExtra("Startup", true);
                    startActivity(homeIntent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");

        super.onPause();
    }

}

