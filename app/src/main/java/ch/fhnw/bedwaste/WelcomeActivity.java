package ch.fhnw.bedwaste;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Security;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WelcomeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationResultReceiver locResultReceiver;
    /**
     * Debugging tag WelcomeActivity used by the Android logger.
     */
    private static final String TAG = "WelcomeActivity";


    // A default location (ZH, CH) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(47.3769, 8.5417);
    private static final int DEFAULT_ZOOM = 16;
    private EditText mEditText;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TRACKING_LOCATION_KEY = "tracking_location";
    private GoogleMap mMap;

    // Location classes
    private boolean mTrackingLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    Button mLocationButton;
    LinearLayout filterLayout;
    ImageButton btnFilter;


    private Location mLastKnownLocation;
    private Location mCurrentLocation;

    //GoogleAPI Client related
    private final int REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR=1;
    boolean mResolvingError;
    GoogleApiClient mGoogleApiClient;
    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Toast.makeText(WelcomeActivity.this, "Google API Client - onConnected()", Toast.LENGTH_LONG).show();
                    try{
                        setLastKnownLocation();
                    }catch (SecurityException e){
                        e.printStackTrace();
                    }
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

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(DEFAULT_ZOOM);
                    return false;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Log.d(TAG, "onCreate(Bundle) called");
        mEditText=findViewById(R.id.input_location);
        mLocationButton = (Button) findViewById(R.id.search);
        filterLayout = (LinearLayout) findViewById(R.id.layoutFilters);
        // Set Visibility of Filter to univisible
        filterLayout.setVisibility(View.GONE);
        btnFilter = (ImageButton) findViewById(R.id.buttonFilter);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.current_location);
        mapFragment.getMapAsync(this);
        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupGoogleApiClient();
        locResultReceiver = new LocationResultReceiver(new Handler());

        // Restore the state if the activity is recreated.
        if (savedInstanceState != null) {
            mTrackingLocation = savedInstanceState.getBoolean(TRACKING_LOCATION_KEY);
        }
        // Disable/Enable Filter
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLayout.getVisibility() == View.VISIBLE){
                    filterLayout.setVisibility(View.GONE);
                }
                else{
                    filterLayout.setVisibility(View.VISIBLE);
                }
            }
        } );

        // Set the listener for the location button.
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggle the tracking state.
             * @param v The hotels nearby button.
             */
            @Override
            public void onClick(View v) {
               mEditText = findViewById(R.id.input_location);
                if (!mEditText.getText().toString().equals("")){

                   LatLng newLocation = getLocationFromAddress(mEditText.getText().toString());
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

               }
            }
        });
        mLocationCallback = new LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates your location.
             * @param locationResult The result containing the device location.
             */
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (mTrackingLocation) {
                    mCurrentLocation= locationResult.getLocations().get(0);
                    addDeviceLocation();
                }
            }
        };
        // Initialize the location callbacks.
        startLocationUpdates();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(DEFAULT_ZOOM);

        addLocationToMap();

    }
    private void addLocationToMap(){
        //Checking if the user has granted the permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requesting the Location permission
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        Location lastLoc= getLastKnownLocation();
        if( lastLoc!= null){
            LatLng redmond= new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(redmond);
            mMap.addMarker(markerOptions);
        }
        else{
            showDefaultLocation();
            enableMyLocationIfPermitted();
        }

    }
    private void showDefaultLocation() {
        Toast.makeText(this, "Showing default location",
                Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(getDefaultLocation()));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(getDefaultLocation());
        mMap.addMarker(markerOptions);
    }
/*    private void showDeviceCurrentLocation() {
        Toast.makeText(this, "Showing device location",
                Toast.LENGTH_SHORT).show();
        double lat=  getCurrentLocation().getAltitude();
        double longitude= getCurrentLocation().getLongitude();
        LatLng redmond= new LatLng(lat, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(redmond);
        mMap.addMarker(markerOptions);
    }*/


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

    protected void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    private void setLastKnownLocation() throws SecurityException{

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLastKnownLocation=location;
                }else {
                    Toast.makeText(WelcomeActivity.this, "Location unknown", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mTrackingLocation = true;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                            mLocationCallback,
                            null);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                    addLocationToMap();
                } else {
                    Toast.makeText(this, "Location permission not granted, " +
                                    "restart the app if you want the feature",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    /**
     * Saves the last location on configuration change
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TRACKING_LOCATION_KEY, mTrackingLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        mGoogleApiClient.connect();
    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called");
        if (mTrackingLocation) {
            startLocationUpdates();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");
        if (mTrackingLocation) {
            mTrackingLocation = false;
        }
        super.onPause();
    }
    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called");
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }


    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION,ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }
    private void addDeviceLocation(){
        Intent intent = new Intent(this, CurrentLocationIntentService.class);
        intent.putExtra("add_location", getCurrentLocation());
        startService(intent);
    }


    private class LocationResultReceiver extends ResultReceiver {
        LocationResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Current device location", "Location null retrying");
            }

            if (resultCode == 1) {
                Toast.makeText(WelcomeActivity.this,
                        "Device location not found, " ,
                        Toast.LENGTH_SHORT).show();
            }
            mCurrentLocation = (Location)resultData.get("add_location");


        }
    }
    // Search for Inserted Address

    /**
     * Takes a String and gives the found Coordinates in form of a LatLng Object Back
     *
     * @param address_string String to Search
     * @return LatLng of First Result
     */
    public LatLng getLocationFromAddress (String address_string){
        Geocoder address_geocoder = new Geocoder(this);
        LatLng coordinates;
        try {
            List<Address> address = address_geocoder.getFromLocationName(address_string, 5);
            if (address == null){
                coordinates = new LatLng(0,0);
            }
            else  {
                Address location = address.get(0);
                coordinates = new LatLng(location.getLatitude(), location.getLongitude());

            }

        }
        catch (Exception e) {
            e.printStackTrace();
            coordinates = new LatLng(0,0);
        }
        return coordinates;

    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public Location getLastKnownLocation() {
        return mLastKnownLocation;
    }
    public LatLng getDefaultLocation() {
        return mDefaultLocation;
    }


}
