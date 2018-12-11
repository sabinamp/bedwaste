package ch.fhnw.bedwaste;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.security.Security;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WelcomeActivity extends AppCompatActivity implements OnMapReadyCallback  {

    /**
     * Debugging tag WelcomeActivity used by the Android logger.
     */
    private static final String TAG = "WelcomeActivity";
    private EditText mEditText;

    // Location classes

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationClient;
    //private LocationCallback mLocationCallback;
    private CameraPosition mCameraPosition;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //private static final String TRACKING_LOCATION_KEY = "tracking_location";
    private GoogleMap mMap;
    // A default location (ZH, CH) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(47.3769, 8.5417);
    private static final int DEFAULT_ZOOM = 15;
    private boolean mLocationPermissionGranted;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Layout Elements
    private ImageButton mLocationButton;
    private LinearLayout filterLayout;

    private TextView textValueDistance;
    private SeekBar seekBarDistance;
    private TextView textValuePrice;
    private SeekBar seekBarPrice;
    private BottomNavigationView mBottomNavigationView;

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
                        getDeviceLocation();
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
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_welcome);
        Log.d(TAG, "onCreate(Bundle) called");

        setupGoogleApiClient();
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.current_location);
        mapFragment.getMapAsync(this);

        mEditText=findViewById(R.id.input_location);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_filters:{
                        if (filterLayout.getVisibility() == View.VISIBLE){
                            filterLayout.setVisibility(View.GONE);
                        }
                        else{
                            filterLayout.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                    case R.id.app_bar_profile: {
                        if (filterLayout.getVisibility() == View.VISIBLE) {
                            filterLayout.setVisibility(View.GONE);
                        }
                        //showUserProfile();
                        return true;
                    }
                    case R.id.app_bar_home:
                    { if (filterLayout.getVisibility() == View.VISIBLE){
                            filterLayout.setVisibility(View.GONE);
                        }
                        //showLocation();
                        return true;}
                    default:
                        return false;
                }
            }
        });

        mLocationButton = (ImageButton) findViewById(R.id.search);
        filterLayout = (LinearLayout) findViewById(R.id.layoutFilters);
        //Set Progress Bar to Default Distance of 10 km
        seekBarDistance = (SeekBar) findViewById(R.id.seekBarDistance);
        seekBarDistance.setProgress(10);
        textValueDistance = (TextView) findViewById(R.id.textValueDistance);
        //Set Progress Bar of Price to Default 100 CHF and the max to 1000 CHF
        seekBarPrice = (SeekBar) findViewById(R.id.seekBarPrice);
        seekBarPrice.setMax(1000);
        seekBarPrice.setProgress(100);
        textValuePrice = (TextView) findViewById(R.id.textPriceValue);
        // Set Visibility of Filter to invisible
        filterLayout.setVisibility(View.GONE);


        //Listener vor Seek Bar of Distance Changes
        seekBarDistance.setOnSeekBarChangeListener (new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                textValueDistance.setText(seekBarDistance.getProgress() + " km");
            }
        });

        //Listener for Seek Bar for Price Changes
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textValuePrice.setText(seekBarPrice.getProgress() + " CHF");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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



    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(DEFAULT_ZOOM);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
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

    protected void setupGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng mlastLatLng= new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlastLatLng, DEFAULT_ZOOM));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(mlastLatLng);
                            markerOptions.icon(bitmapDescriptorFromVector(WelcomeActivity.this, R.drawable.ic_marker));
                            mMap.addMarker(markerOptions);
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Showing default location",
                Toast.LENGTH_SHORT).show();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(getDefaultLocation()));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(getDefaultLocation());
        markerOptions.icon(bitmapDescriptorFromVector(this,R.drawable.ic_marker));
        mMap.addMarker(markerOptions);
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        //Checking if the user has granted the permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else{
            //Requesting the Location permission
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationUI();
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
        super.onSaveInstanceState(outState);
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }
    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
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
        super.onResume();
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
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.bottom_bar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.app_bar_filters:{
                if (filterLayout.getVisibility() == View.VISIBLE){
                    filterLayout.setVisibility(View.GONE);
                }
                else{
                    filterLayout.setVisibility(View.VISIBLE);
                }
                return true;}
            case R.id.app_bar_profile:
                if (filterLayout.getVisibility() == View.VISIBLE){
                    filterLayout.setVisibility(View.GONE);
                }
                //showUserProfile();
                return true;
            case R.id.app_bar_home:
                if (filterLayout.getVisibility() == View.VISIBLE){
                    filterLayout.setVisibility(View.GONE);
                }
                //showCurrentDeviceLocationAndHotels();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION,ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }*/



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

    public Location getLastKnownLocation() {
        return mLastKnownLocation;
    }
    public LatLng getDefaultLocation() {
        return mDefaultLocation;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()*2, vectorDrawable.getIntrinsicHeight()*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
