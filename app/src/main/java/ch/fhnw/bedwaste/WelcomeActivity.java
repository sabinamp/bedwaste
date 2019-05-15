package ch.fhnw.bedwaste;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.ContactInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.HotelInfoPosition;
import ch.fhnw.bedwaste.model.MultimediaDescription;
import ch.fhnw.bedwaste.model.MultimediaDescriptionImages;
import ch.fhnw.bedwaste.server.AvailabilitiesPerRegionListener;
import ch.fhnw.bedwaste.server.AvailabilitiesPerRegionService;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WelcomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private WelcomeViewModel pmodel;
    private String searchRegion;

    public Set<String> getmHotelsToDisplay() {
        return mHotelsToDisplay;
    }

    public void updatemHotelsToDisplay(Set<String> mHotelsToDisplay) {
        this.mHotelsToDisplay.clear();
        this.mHotelsToDisplay = mHotelsToDisplay;
    }
    private void addmHotelsToDisplay(String hotelId){
        this.mHotelsToDisplay.add(hotelId);
    }
    private Set<String> mHotelsToDisplay=null;
    private List<Marker> markers;

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
    private static final int DEFAULT_ZOOM = 13;
    private boolean mLocationPermissionGranted;

    //Object for network connection check
    private NetworkDetector netDetector = new NetworkDetector(WelcomeActivity.this);

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Layout Elements
    private ImageButton mLocationButton;
    private LinearLayout filterLayout;
    private ImageButton mFiltersButton;
    private LinearLayout mFilterExtendedLayout;
    private ImageView btnFilterExtend;
    private ImageView btnFilterLess;
    private ImageView btnMoreRooms;
    private ImageView btnLessRooms;

    private ImageView btnMorePeople;
    private ImageView btnLessPeople;
    private TextView textValueRooms;
   // private TextView textValueNights;
    private TextView textValuePeople;
    private ImageButton declineFilter;
    private ImageButton applyFilter;
    private CheckBox checkBoxWLAN;
    private CheckBox checkBoxBreakfast;
    private TextView helpText1;
    private TextView helpText2;
    private LinearLayout searchBar;
    private TextView textValueDistance;
    private SeekBar seekBarDistance;
    private TextView textValuePrice;
    private SeekBar seekBarPrice;
    private TextView textValueRating;
    private SeekBar seekBarRating;

    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton FABLocation;
    private View BlankAnimationBar;
    private int distanceToHotel;
    private int maxprice;
    private int nbadults;
    private int nbrooms;
    private Boolean breakfast;
    private Boolean wifi;


    private CardView hotel_overview_layout;
    private TextView ho_hotelname;
    private ImageView ho_image;
    private TextView ho_star_rating;
    private TextView ho_price_per_night;
    private TextView ho_address;
    private TextView ho_city;
    private TextView ho_rating;
    private TextView ho_minutes;


    //GoogleAPI Client related
    private final int REQUEST_RESOLVE_GOOGLE_CLIENT_ERROR = 1;
    boolean mResolvingError;
    GoogleApiClient mGoogleApiClient;
    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                   // Toast.makeText(WelcomeActivity.this, "Google API Client - onConnected()", Toast.LENGTH_LONG).show();
                    try {
                        getDeviceLocation();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                }
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

    public WelcomeActivity() throws ParseException {
        maxprice=500;
        nbadults=1;
        nbrooms=1;
        pmodel= new WelcomeViewModel();
        breakfast=null;
        wifi=null;
        markers = new ArrayList<>();
        mHotelsToDisplay= new TreeSet<>();
        searchRegion=null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);


        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_welcome);
        Log.d(TAG, "Welcome Activity - onCreate(Bundle) called");

        setupGoogleApiClient();
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.current_location);
        mapFragment.getMapAsync(this);

        mEditText = findViewById(R.id.input_location);
        addBottomNavigation();
        Intent intent = getIntent();
        boolean Startup = intent.getBooleanExtra("Startup", false);
        if (Startup){
            addIntroAnimation();

        }
        else {
            helpText1 = (TextView) findViewById(R.id.firstHelpBox);
            helpText2 = (TextView) findViewById(R.id.secondHelpBox);
            BlankAnimationBar = (View) findViewById(R.id.blank_bar);
            helpText1.setVisibility(View.INVISIBLE);
            helpText2.setVisibility(View.INVISIBLE);
            BlankAnimationBar.setVisibility(View.INVISIBLE);
        }

        //load Filter Code
        bindFilterData();

        mLocationButton = (ImageButton) findViewById(R.id.search);


        //HotelOverview Items
       //List<String> all_ids = Arrays.asList("00B5846B02barlac", "00U5846j020d210g", "00G5846t022gotth", "00U5846j022d292h", "00I5846a022h291r", "00U5845j020s210l", "00U5847f022marri", "00F5846A022nowifi", "00F5846A02nowifi2", "00U5846f022c291a", "00U5844f022rigib", "00B5846t02termin", "00U5846e0f2ukulm", "00U5846j022d291s", "00U5845f022gbrugg", "00U5846f022marco", "00U5556f030plb91", "00U5845f022rotesh");


        ho_hotelname = (TextView) findViewById(R.id.wel_hotel_name);
        ho_image = (ImageView) findViewById(R.id.wel_icon);
        ho_star_rating = (TextView) findViewById(R.id.wel_stars);
        ho_price_per_night =(TextView) findViewById(R.id.wel_hotel_price);
        ho_address =(TextView) findViewById(R.id.wel_hotel_address_line);
        ho_city = (TextView) findViewById(R.id.wel_otel_city);
        ho_minutes =(TextView) findViewById(R.id.wel_min_hotel);





        // Set the listener for the location button.
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggle the tracking state.
             * @param v The hotels nearby button.
             */
            @Override
            public void onClick(View v) {
                mEditText = findViewById(R.id.input_location);

                if (!mEditText.getText().toString().equals("")) {
                    String locationSearched = mEditText.getText().toString();

                    if (locationSearched.equalsIgnoreCase("Brugg")) {
                        setSearchRegion("Aargau");

                    } else if (locationSearched.equalsIgnoreCase("Basel")) {
                        setSearchRegion("Basel");
                    } else {
                        setSearchRegion("ZH");
                    }

                    LatLng newLocation = getLocationFromAddress(mEditText.getText().toString());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                    applyFiltering_RetrieveHotelAvailabilities();
                    }

                }

        });


        // start of network connection check
        netDetector.networkRunnable.run();

        //add overview of clicked hotel
        hotel_overview_layout = (CardView) findViewById(R.id.hotel_overview);

        // call Countdown
        countdownRunnable.run();

    }
    private void retrieveAllDescriptiveHotelData(){
        Log.d(TAG, "start retrieveHotelDescriptiveData() - fetching data from the server");
        for (final String eachId : WelcomeViewModel.ALL_IDS) {

            Log.d(TAG, "start retrieveHotelData - fetching data from the server");
    /*        HotelAvailabilityResultsService service_price = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {

                @Override
                public void success(Response<AvailabilityResults> response) {
                    AvailabilityResults roomAvailabilityResults = response.body();
                    float price = roomAvailabilityResults.get(0).getProducts().get(0).getTotalPrice();
                    //updatePrice
                    pmodel.updateDisplayedPrices(eachId, (int)price);

                    //update availabilities
                    pmodel.updateHotelId_availabilities(eachId, roomAvailabilityResults);
                }
                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch availability results" + message);
                }
            });
            service_price.getRoomAvailabilitiesInHotel(eachId, 1, 0, 0);
*/
            HotelDescriptiveInfoService service_description = new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
                @Override
                public void success(Response<HotelDescriptiveInfo> response) {
                    HotelDescriptiveInfo hotelDescriptiveInfo = response.body();
                    pmodel.updateHotelId_descriptiveInfo(eachId, hotelDescriptiveInfo);

                    HotelInfoPosition position = hotelDescriptiveInfo.getHotelInfo().getPosition();
                    LatLng currentHotelPosition= new LatLng(position.getLatitude().doubleValue(),position.getLongitude().doubleValue());
                    pmodel.HotelIdBaasedOnPosition.put(currentHotelPosition, eachId);

                }

                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch hotel descriptive info" + message);
                }
            });
            service_description.getHotelDescriptiveInfo("en", eachId);
        }
        Log.d(TAG, "retrieveHotelDescriptiveData()- fetching data from the server - completed");
    }

    private void displayMarkers(){
        markers.clear();
        Log.d("TAG", "start displaying markers. Now the markers should be 0. Actual nb of markers is "+markers.size());
        for (final String eachId : getmHotelsToDisplay()) {
            //markers and prices
            HotelDescriptiveInfo currentHotelDescriptiveInfo= pmodel.getHotelId_descriptiveInfo().get(eachId);
            if(currentHotelDescriptiveInfo !=null){
                HotelInfo info=currentHotelDescriptiveInfo.getHotelInfo();
                LatLng currentHotelPosition=null;
                if(info != null){
                    currentHotelPosition= new LatLng(info.getPosition().getLatitude().doubleValue(),
                            currentHotelDescriptiveInfo.getHotelInfo().getPosition().getLongitude().doubleValue());
                }
                Map<String, Integer> pricesMap=pmodel.getDisplayedPrices();
                Integer price= pricesMap.get(eachId);

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(currentHotelPosition)
                        .title(currentHotelDescriptiveInfo.getHotelName())
                        .icon(bitmapDescriptorFromVector(WelcomeActivity.this, R.drawable.ic_marker)));
                        //.snippet( " CHF " + pmodel.getDisplayedPrices().get(eachId)));
                if(price instanceof Integer){
                    marker.setSnippet( " CHF " + price.intValue());
                }
                else{
                    marker.setSnippet(" CHF " + 0);
                }
                markers.add(marker);
                marker.showInfoWindow();
            }
        }
        Log.d(TAG, "finished displaying markers in region "+getSearchRegion()+". Number of markers is "+markers.size());
    }

    public String getSearchRegion() {
        return searchRegion;
    }

    public void setSearchRegion(String searchRegion) {
        this.searchRegion = searchRegion;
    }
    private void bindFilterData (){

        mFiltersButton = (ImageButton) findViewById(R.id.filters_btn);
        filterLayout = (LinearLayout) findViewById(R.id.filter);
        mFilterExtendedLayout = (LinearLayout) findViewById(R.id.expandedFilter);
        btnFilterExtend = (ImageView) findViewById(R.id.btnExpandFilter);
        btnFilterLess = (ImageView) findViewById(R.id.lessfilter);
        mFilterExtendedLayout.setVisibility(View.GONE);
        btnFilterLess.setVisibility(View.GONE);
        btnLessPeople = (ImageView) findViewById(R.id.lessPeople);
        btnMorePeople = (ImageView) findViewById(R.id.morepeople);

        btnLessRooms = (ImageView) findViewById(R.id.lessRooms);
        btnMoreRooms = (ImageView) findViewById(R.id.moreRooms);
        // textValueNights = (TextView) findViewById(R.id.textNightValue);
        textValuePeople = (TextView) findViewById(R.id.textPeopleValue);
        textValueRooms = (TextView) findViewById(R.id.textRoomsValue);
        applyFilter = (ImageButton) findViewById(R.id.imageApply);
        declineFilter = (ImageButton) findViewById(R.id.imageDecline);
        checkBoxBreakfast = findViewById(R.id.checkBoxBreakfast);
        checkBoxWLAN = findViewById(R.id.checkBoxWlan);
        searchBar = findViewById(R.id.search_bar);
        FABLocation = findViewById(R.id.floatingActionButtonLocation);
        seekBarRating = (SeekBar) findViewById(R.id.seekBarRating);
        textValueRating = (TextView) findViewById(R.id.textValueRating);
        seekBarRating.setMax(10);
        seekBarRating.setProgress(1);

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


        mFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.GONE);
                } else {
                    filterLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        btnFilterExtend.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFilterLess.setVisibility(View.VISIBLE);
                mFilterExtendedLayout.setVisibility(View.VISIBLE);
                btnFilterExtend.setVisibility(View.GONE);

            }
        });

        btnFilterLess.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFilterExtend.setVisibility(View.VISIBLE);
                mFilterExtendedLayout.setVisibility(View.GONE);
                btnFilterLess.setVisibility(View.GONE);
            }

        });

        declineFilter.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBarPrice.setMax(1000);
                seekBarPrice.setProgress(100);
                textValueDistance.setText("100 CHF");
                textValueDistance.setText("10 km");
                textValuePeople.setText("1");
                //textValueNights.setText("1");
                textValueRooms.setText("1");
                checkBoxBreakfast.setChecked(false);
                checkBoxWLAN.setChecked(false);

            }
        });
        btnLessPeople.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PeopleValue = textValuePeople.getText().toString();

                if (Integer.parseInt(PeopleValue) != 1) {
                    PeopleValue = String.valueOf(Integer.parseInt(PeopleValue) - 1);
                    textValuePeople.setText(PeopleValue);
                    nbadults= Integer.parseInt(PeopleValue);
                }


            }
        });
        btnMorePeople.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PeopleValue = textValuePeople.getText().toString();
                PeopleValue = String.valueOf(Integer.parseInt(PeopleValue) + 1);
                textValuePeople.setText(PeopleValue);
                nbadults= Integer.parseInt(PeopleValue);

            }
        });
        btnLessRooms.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RoomsValue = textValueRooms.getText().toString();
                if (Integer.parseInt(RoomsValue) != 1) {
                    RoomsValue = String.valueOf(Integer.parseInt(RoomsValue) - 1);
                    textValueRooms.setText(RoomsValue);
                    nbrooms= Integer.parseInt(RoomsValue);
                }


            }
        });
        btnMoreRooms.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String RoomsValue = textValueRooms.getText().toString();
                RoomsValue = String.valueOf(Integer.parseInt(RoomsValue) + 1);
                textValueRooms.setText(RoomsValue);
                nbrooms= Integer.parseInt(RoomsValue);
            }
        });

        seekBarRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textValueRating.setText(String.valueOf(seekBarRating.getProgress()) );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Listener vor Seek Bar of Distance Changes
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textValueDistance.setText(seekBarDistance.getProgress() + " km");
                distanceToHotel = seekBarDistance.getProgress();
            }
        });

        //Listener for Seek Bar for Price Changes
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textValuePrice.setText(seekBarPrice.getProgress() + " CHF");
                maxprice= seekBarPrice.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        applyFilter.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFiltersButton.callOnClick();

            }
        });

    }

    private void addIntroAnimation() {
        //Animation for Introduction of Map
        helpText1 = (TextView) findViewById(R.id.firstHelpBox);
        helpText2 = (TextView) findViewById(R.id.secondHelpBox);
        BlankAnimationBar = (View) findViewById(R.id.blank_bar);
        final ConstraintLayout animationLayout = findViewById(R.id.animationLayout);
        animationLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                animationLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startAnimation();
            }
        });
    }


    private void addBottomNavigation() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_hotel_list: {
                        Intent listIntent = new Intent(WelcomeActivity.this, HotelListViewActivity.class);
                        ArrayList<String> listToPass= new ArrayList<String>();
                        listToPass.addAll(mHotelsToDisplay);
                        listIntent.putStringArrayListExtra("bedwaste_hotel_list", listToPass);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view: {
                        return true;
                    }
                    case R.id.app_bar_profile: {

                        Intent profileIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(profileIntent);
                        return true;
                    }
                    default:
                        return false;
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
                            this, R.raw.style3));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        googleMap.setOnMarkerClickListener(this);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        hotel_overview_layout.setVisibility(View.VISIBLE);

        final String matched_hotel_id = WelcomeViewModel.HotelIdBaasedOnPosition.get( marker.getPosition());

       HotelDescriptiveInfoService service= new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
            @Override
            public void success(Response<HotelDescriptiveInfo> response) {
                HotelDescriptiveInfo hotelDescriptiveInfo = response.body();
                //the following line for when not fetching retrieving data the server
                // details can be obtained from the model (except ImageUrls- why are imageURL not displayed? - to do )
                //HotelDescriptiveInfo hotelDescriptiveInfo= pmodel.getHotelId_descriptiveInfo().get(matched_hotel_id);
                String rating = hotelDescriptiveInfo.getAffiliationInfo().getAwards().get(1).getRating() +"/10";
                ho_hotelname.setText(hotelDescriptiveInfo.getHotelName());

                //iterate through amount of stars to create *** String
                String star_amount_string = "";
                ch.fhnw.bedwaste.model.Award hotel_association_rating = hotelDescriptiveInfo.getAffiliationInfo().getAwards().get(0);

                String star_amount = hotel_association_rating.getRating();
                String star_string;
                if (star_amount == null){
                    star_string = "No Rating";
                }
                else {
                      int stars = Integer.parseInt(star_amount);
                      star_string = new String(new char[stars]).replace("", "*");
                      ho_star_rating.setText(star_string);
                        /*double stars =Math.floor(Double.parseDouble(star_amount));
                        star_string = new String(new char[(int)stars]).replace("", "*");
                        ho_star_rating.setText(star_string);*/
                }

                //insert_banner?!
                HotelInfo hotelInfo = hotelDescriptiveInfo.getHotelInfo();
                java.util.List<MultimediaDescription> multimediaDescriptions = hotelInfo.getDescriptions().getMultimediadescriptions();
                MultimediaDescription first_mmDescription = multimediaDescriptions.get(0);
                java.util.List<MultimediaDescriptionImages> hotel_images = first_mmDescription.getImages();
                MultimediaDescriptionImages banner_picture = hotel_images.get(0);
                String imageUrl_banner = banner_picture.getImageUrl();

                Picasso.get().load(imageUrl_banner).fit().into(ho_image);

                //insert_minutes_away.setText(hotelDescriptiveInfo.get());

                ho_price_per_night.setText("CHF "+ pmodel.getDisplayedPrices().get(matched_hotel_id));
                java.util.List<ch.fhnw.bedwaste.model.ContactInfo>  hotelDescriptiveInfoContactInfos= hotelDescriptiveInfo.getContactInfos();
                //takes first entry as main contact info
                ContactInfo contactInfo = hotelDescriptiveInfoContactInfos.get(0);
                java.util.List<ch.fhnw.bedwaste.model.Address> addresses  = contactInfo.getAddresses();
                ch.fhnw.bedwaste.model.Address address = addresses.get(0);

                ho_address.setText(address.getAddressLine() + " " + address.getStreetNmbr());
                ho_city.setText(address.getPostalCode() + " " + address.getCityName());
           }

            @Override
            public void failed(String message) {
                Log.d(TAG, "couldn't fetch descriptive info"+message);
            }
        });

        service.getHotelDescriptiveInfo("en", matched_hotel_id);
        return false;
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
                        if (task.getResult() != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            LatLng mlastLatLng = new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlastLatLng, DEFAULT_ZOOM));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(mlastLatLng);
                            //add user location marker if needed
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                           /* mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);*/
                            Toast toast=Toast.makeText(WelcomeActivity.this, "You need to grant this app location permission, " +
                                            "Turn on the location services and restart the app.",
                                    Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 5,5);
                                    toast.setDuration(6000);
                            toast.show();

                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
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
        } else {
            //Requesting the Location permission
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void applyFiltering_RetrieveHotelAvailabilities(){
        breakfast= checkBoxBreakfast.isChecked();
        wifi=checkBoxWLAN.isChecked();
        String region = getSearchRegion();
        Log.d("TAG", "Start retrieving availabilities for region "+region);
        AvailabilitiesPerRegionService service3= new AvailabilitiesPerRegionService(new AvailabilitiesPerRegionListener() {
            @Override
            public void success(Response<List<AvailabilityResult>> response) {

                List<AvailabilityResult> availabilitiesPerRegion= response.body();
                Set<String> hotelsToDisplayOnFiltering=new TreeSet<>();
                updatemHotelsToDisplay(hotelsToDisplayOnFiltering);
                Log.d("TAG", "filtering/retrieving availabilities per region, hotelsToDisplay size is "+getmHotelsToDisplay().size());
                for (AvailabilityResult resultAv: availabilitiesPerRegion) {
                    String[] rateplanIdElem= resultAv.getRateplanId().split("-");
                    String hotelId= rateplanIdElem[0];
                    Log.d("TAG", "retrieving price for- "+hotelId);
                    //hotelsToDisplayOnFiltering.add(hotelId);
                    addmHotelsToDisplay(hotelId);
                    Log.d("TAG", "filtering/retrieving availabilities hotelsToDisplay size is "+getmHotelsToDisplay().size());
                    int avPrice= resultAv.getTotalPrice().intValue();
                    Log.d("TAG", "retrieving price "+avPrice+" for "+hotelId);
                    pmodel.updateDisplayedPrices(hotelId, avPrice);

                }
                displayMarkers();
               // updatemHotelsToDisplay(hotelsToDisplayOnFiltering);

            }

            @Override
            public void failed(String message) {
                Log.d("TAG", "failed receiving filtered availabilities");
            }
        });
        if(!breakfast){
            breakfast=null;
        }
        if(!wifi){
            wifi=null;
        }
        service3.getAvailabilitiesPerRegion(region, nbadults, 0,0, maxprice, nbrooms, breakfast, wifi);
        Log.d("TAG", "finished retrieving availabilities for region "+region);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        mGoogleApiClient.connect();
        retrieveAllDescriptiveHotelData();
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

    //make overview disappear when not clicked on marker

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (hotel_overview_layout.getVisibility() == View.VISIBLE){
            hotel_overview_layout.setVisibility(View.GONE);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }


    // Search for Inserted Address

    /**
     * Takes a String and gives the found Coordinates in form of a LatLng Object Back
     *
     * @param address_string String to Search
     * @return LatLng of First Result
     */
    public LatLng getLocationFromAddress(String address_string) {
        Geocoder address_geocoder = new Geocoder(this);
        LatLng coordinates;
        try {
            List<Address> address = address_geocoder.getFromLocationName(address_string, 5);
            if (address == null) {
                coordinates = new LatLng(0, 0);
            } else {
                Address location = address.get(0);
                coordinates = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (Exception e) {
            e.printStackTrace();
            coordinates = new LatLng(0, 0);
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
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    // COUNTDOWN LOGIC

    private final static int HOUR_TO_ACTIVATE_COUNTDOWNLABEL = 12;
    private final static String ENDTIME_TO_DEACTIVATE_COUNTDOWNLABEL = "24:00:00";

    Handler countdownHandler = new Handler();

    private TextView countdownTextSeconds;
    private TextView countdownTextMinutes;
    private TextView countdownTextHours;
    private TableLayout countdownBox;

    private CountDownTimer countDownTimer;

    // This Runnable Object is used to trigger the countdown method & the handlermethod calls this runnable each 5sec after APP has started.

    private Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            countdownHandler.postDelayed(this, 10000);

            try {

                LocalTime localTime = LocalTime.now();
                if (localTime.getHour() >= HOUR_TO_ACTIVATE_COUNTDOWNLABEL) {
                    startCountdown();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };

    public void startCountdown() throws ParseException {

        LocalTime localTime = LocalTime.now();

        // Format time and calcualtion of elapsed time..
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date endTime = format.parse(ENDTIME_TO_DEACTIVATE_COUNTDOWNLABEL);
        Date deviceLocaltime = format.parse(localTime.toString());

        long timeElapsed = endTime.getTime() - deviceLocaltime.getTime();

        countdownTextSeconds = (TextView) findViewById(R.id.countdownTextSeconds);
        countdownTextMinutes = (TextView) findViewById(R.id.countdownTextMinutes);
        countdownTextHours = (TextView) findViewById(R.id.countdownTextHours);
        countdownBox = (TableLayout) findViewById(R.id.countdownBox);

        countdownBox.setAlpha(1);

        countDownTimer = new CountDownTimer(timeElapsed, 1000) {

            public void onTick(long millisUntilFinished) {

                // in Seconds
                if ((millisUntilFinished / 1000 % 60) >= 10) {
                    countdownTextSeconds.setText("" + millisUntilFinished / 1000 % 60);
                } else {
                    countdownTextSeconds.setText("0" + millisUntilFinished / 1000 % 60);
                }

                // in Minutes
                if ((millisUntilFinished / (60 * 1000) % 60) >= 10) {
                    countdownTextMinutes.setText("" + millisUntilFinished / (60 * 1000) % 60);
                } else {
                    countdownTextMinutes.setText("0" + millisUntilFinished / (60 * 1000) % 60);
                }

                // in Hours
                if ((millisUntilFinished / (60 * 60 * 1000) % 24) >= 10) {
                    countdownTextHours.setText("" + millisUntilFinished / (60 * 60 * 1000) % 24);
                } else {
                    countdownTextHours.setText("0" + millisUntilFinished / (60 * 60 * 1000) % 24);
                }
                countdownHandler.removeCallbacks(countdownRunnable);
            }

            public void onFinish() {
                countdownBox.setAlpha(0);
                countdownHandler.postDelayed(countdownRunnable, 10000);

            }
        }.start();
    }


    public void startAnimation() {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        searchBar.setVisibility(View.INVISIBLE);
        if (countdownBox != null) {
            countdownBox.setVisibility(View.INVISIBLE);
        }

        FABLocation.hide();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxY = mdispSize.y;
        int maxX = mdispSize.x;
        final ValueAnimator helpText1X = ValueAnimator.ofFloat(helpText1.getX(), 0);
        final ValueAnimator helpText1Y = ValueAnimator.ofFloat(helpText1.getY(), maxY - 280);
        final ValueAnimator helpText1Width = ValueAnimator.ofInt(helpText1.getWidth(), width);
        final ValueAnimator helpText1Height = ValueAnimator.ofInt(helpText1.getHeight(), 150);
        helpText1X.setDuration(1500);
        helpText1Y.setDuration(1500);
        helpText1Width.setDuration(1500);
        helpText1Height.setDuration(1500);
        helpText1X.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                helpText1.setText("");
                helpText1.setX((float) animation.getAnimatedValue());
            }
        });
        helpText1Y.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                helpText1.setY((float) animation.getAnimatedValue());
            }
        });
        helpText1Width.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                helpText1.setWidth((int) animation.getAnimatedValue());
            }
        });

        helpText1Height.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                helpText1.setHeight((int) animation.getAnimatedValue());
            }
        });


        final ValueAnimator BlankAnimationBarX = ValueAnimator.ofFloat(BlankAnimationBar.getX(), FABLocation.getX());
        final ValueAnimator BlankAnimationBarY = ValueAnimator.ofFloat(BlankAnimationBar.getY(), FABLocation.getY());
        final ValueAnimator BlankAnimationBarWidth = ValueAnimator.ofInt(BlankAnimationBar.getWidth(), 100);
        final ValueAnimator BlankAnimationBarHeight = ValueAnimator.ofInt(BlankAnimationBar.getHeight(), FABLocation.getHeight());
        BlankAnimationBarX.setDuration(1500);
        BlankAnimationBarY.setDuration(1500);
        BlankAnimationBarWidth.setDuration(1500);
        BlankAnimationBarHeight.setDuration(1500);
        BlankAnimationBarX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                BlankAnimationBar.setX((float) animation.getAnimatedValue() + 50);
            }
        });
        BlankAnimationBarY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BlankAnimationBar.setY((float) animation.getAnimatedValue() + 50);
            }
        });
        BlankAnimationBarWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                BlankAnimationBar.setLayoutParams(new ConstraintLayout.LayoutParams((int) animation.getAnimatedValue(), 100));


            }
        });

        BlankAnimationBarHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                BlankAnimationBar.setMinimumHeight((int) animation.getAnimatedValue());
            }
        });


        BlankAnimationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpText2.setVisibility(View.INVISIBLE);
                BlankAnimationBarX.start();
                BlankAnimationBarY.start();
                BlankAnimationBarHeight.start();
                BlankAnimationBarWidth.start();
                helpText1X.start();
                helpText1Y.start();
                helpText1Height.start();
                helpText1Width.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomNavigationView.setVisibility(View.VISIBLE);
                        searchBar.setVisibility(View.VISIBLE);
                        if (countdownBox != null) {
                            countdownBox.setVisibility(View.VISIBLE);
                        }

                        FABLocation.show();
                        helpText1.setVisibility(View.INVISIBLE);
                        BlankAnimationBar.setVisibility(View.INVISIBLE);
                    }
                }, 1500);

            }
        });
        helpText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpText1X.start();
                helpText1Y.start();
                helpText1Height.start();
                helpText1Width.start();
                helpText2.setVisibility(View.INVISIBLE);
                BlankAnimationBarX.start();
                BlankAnimationBarY.start();
                BlankAnimationBarHeight.start();
                BlankAnimationBarWidth.start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomNavigationView.setVisibility(View.VISIBLE);
                        searchBar.setVisibility(View.VISIBLE);
                        if (countdownBox != null) {
                            countdownBox.setVisibility(View.VISIBLE);
                        }
                        FABLocation.show();
                        BlankAnimationBar.setVisibility(View.INVISIBLE);
                        helpText1.setVisibility(View.INVISIBLE);
                    }
                }, 1500);
            }
        });


    }


}
