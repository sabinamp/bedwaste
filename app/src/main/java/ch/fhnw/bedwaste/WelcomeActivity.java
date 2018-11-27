package ch.fhnw.bedwaste;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WelcomeActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * Debugging tag WelcomeActivity used by the Android logger.
     */
    private static final String TAG = "WelcomeActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient mClient;
    private GoogleMap mMap;
    private final int REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR=1;
    boolean mResolvingError;
    GoogleApiClient mGoogleApiClient;
    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Toast.makeText(WelcomeActivity.this, "onConnected()", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onConnectionSuspended(int i) {}
            };

    GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    Toast.makeText(WelcomeActivity.this, connectionResult.toString(), Toast.LENGTH_LONG).show();
                    if (mResolvingError) {
                        return;
                    } else if (connectionResult.hasResolution()) {
                        mResolvingError = true;
                        try {
                            connectionResult.startResolutionForResult(WelcomeActivity.this,
                                    REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR);
                        } catch (IntentSender.SendIntentException e) {
                            mGoogleApiClient.connect();
                        }
                    } else {
                        showGoogleAPIErrorDialog(connectionResult.getErrorCode());
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Log.d(TAG, "onCreate(Bundle) called");
        EditText locInput=findViewById(R.id.TF_location);
        Button search= findViewById(R.id.H_search);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.current_location);
        mapFragment.getMapAsync(this);
        setupGoogleApiClient();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);
        showDefaultLocation();

    }
    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.3769, 8.5417);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(redmond);
        mMap.addMarker(markerOptions);
    }
    private void showCurrentLocation(){
       /* try{
           double[]coord = getLocation();
            LatLng redmond = new LatLng(coord[0], coord[1]);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(redmond);
            mMap.addMarker(markerOptions);
        }catch(SecurityException e){
            e.printStackTrace();
        }*/
    }
    private void showGoogleAPIErrorDialog(int errorCode) {
        GoogleApiAvailability googleApiAvailability =
                GoogleApiAvailability.getInstance();
        Dialog errorDialog = googleApiAvailability.getErrorDialog(
                this, errorCode, REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR);
        errorDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK
                    && !mGoogleApiClient.isConnecting()
                    && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }
    }
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION,
                            ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }
    protected void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        mGoogleApiClient.connect();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(17);
                    return false;
                }
            };

    /*
     * getting the user's last location
     */
    private double[] getLocation() throws SecurityException{
        final double[] userCoordinates= new double[2];
        mClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat=location.getLatitude();
                    double longitude= location.getLongitude();
                    userCoordinates[0]=lat;
                    userCoordinates[1]=longitude;
                }else {
                    Toast toast1= Toast.makeText(WelcomeActivity.this,"no location", Toast.LENGTH_LONG);
                    toast1.setGravity(Gravity.BOTTOM, 1,5);
                    toast1.show();
                }
            }
        });
        return userCoordinates;

    }
}
