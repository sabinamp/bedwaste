package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.ContactInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.MultimediaDescription;
import ch.fhnw.bedwaste.model.MultimediaDescriptionImages;
import ch.fhnw.bedwaste.model.Phone;
import ch.fhnw.bedwaste.model.Service;
import ch.fhnw.bedwaste.server.APIClient;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoInterface;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HotelInfoActivity extends AppCompatActivity {
    private static final String EXTRA_HOTEL_ID =
            "ch.fhnw.bedwaste.extra.hotel.id";
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelInfoActivity";
    private WelcomeViewModel model;
    private BottomNavigationView mBottomNavigationView;
    private TextView hotelAddress;
    private TextView infoTextView;
    private TextView insert_hotelname;
    private TextView insert_starRating;
    private ImageView insert_banner;
    private TextView insert_minutes_away;
    private TextView insert_price;
    private TextView insert_street;
    private TextView insert_village;
    private TextView insert_telnr;
    private int amount_hotel_pictures;
    private CheckBox checkBox_breakfast;
    private CheckBox checkBox_wlan;

    private NetworkDetector netDetector = new NetworkDetector(HotelInfoActivity.this);

    java.util.List<MultimediaDescriptionImages> hotel_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);
        hotelAddress = findViewById(R.id.text_Address);
        model= new WelcomeViewModel();
        //receive values that got passed from previous activity
        final Intent intent = getIntent();
        final String hotellist_value = intent.getStringExtra("key");

        final TextView insert_hotelname = (TextView) findViewById(R.id.ph_hotelName);
        final TextView insert_starRating = (TextView) findViewById(R.id.ph_starRating);

        final ImageView insert_banner = (ImageView) findViewById(R.id.ph_hotelGeneralImage);

        final TextView insert_minutes_away = (TextView) findViewById(R.id.ph_amountMinutes);
        final TextView insert_price = (TextView) findViewById(R.id.ph_price);
        final TextView insert_address = (TextView) findViewById(R.id.ph_address);


        final TextView insert_village = (TextView) findViewById(R.id.ph_location);
        final TextView insert_telnr = (TextView) findViewById(R.id.ph_phoneNr);

        final CheckBox checkBox_breakfast = (CheckBox) findViewById(R.id.checkBox_Breakfast);
        final CheckBox checkBox_wlan = (CheckBox) findViewById(R.id.checkBox_Wlan);


      HotelDescriptiveInfoService service= new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
            @Override
            public void success(Response<HotelDescriptiveInfo> response) {
                HotelDescriptiveInfo hotelDescriptiveInfo = response.body();

                insert_hotelname.setText(hotelDescriptiveInfo.getHotelName());

                //iterate thrpugh amount of stars to create *** String
                java.util.List<ch.fhnw.bedwaste.model.Award> award_list;
                award_list = hotelDescriptiveInfo.getAffiliationInfo().getAwards();
                String star_amount_string = "";
                ch.fhnw.bedwaste.model.Award hotel_association_rating = award_list.get(0);

                String star_amount = hotel_association_rating.getRating();
                /*int stars = Integer.parseInt(star_amount);
                String starstring = new String(new char[stars]).replace("", "*");*/
                double stars =Math.floor(Double.parseDouble(star_amount));
                star_amount_string = new String(new char[(int)stars]).replace("", "*");

                insert_starRating.setText(star_amount_string);


                //insert_banner?!
                HotelInfo hotelInfo = hotelDescriptiveInfo.getHotelInfo();
                java.util.List<MultimediaDescription> multimediaDescriptions = hotelInfo.getDescriptions().getMultimediadescriptions();
                MultimediaDescription first_mmDescription = multimediaDescriptions.get(0);
                java.util.List<MultimediaDescriptionImages> hotel_images = first_mmDescription.getImages();
                MultimediaDescriptionImages banner_picture = hotel_images.get(0);
                String imageUrl_banner = banner_picture.getImageUrl();


                Picasso.get().load(imageUrl_banner).fit().into(insert_banner);


                //insert_minutes_away.setText(hotelDescriptiveInfo.get());

                HotelAvailabilityResultsService service_price = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {

                    @Override
                    public void success(Response<AvailabilityResults> response) {
                        AvailabilityResults roomAvailabilityResults = response.body();
                        double price = roomAvailabilityResults.get(0).getProducts().get(0).getTotalPrice();
                        insert_price.setText(String.valueOf((int)price));
                    }
                    @Override
                    public void failed(String message) {
                        Log.d(TAG, "couldn't fetch availability results" + message);
                    }
                });
                service_price.getRoomAvailabilitiesInHotel(hotelDescriptiveInfo.getHotelId(), 1, 0, 0);

                java.util.List<ch.fhnw.bedwaste.model.ContactInfo>  hotelDescriptiveInfoContactInfos= hotelDescriptiveInfo.getContactInfos();
                //takes first entry as main contact info
                ContactInfo contactInfo = hotelDescriptiveInfoContactInfos.get(0);
                java.util.List<ch.fhnw.bedwaste.model.Address> addresses  = contactInfo.getAddresses();
                Address address = addresses.get(0);
                //only nr? Possible Second TextView for Street?
                insert_address.setText(address.getAddressLine() + " " + address.getStreetNmbr());
                insert_village.setText(address.getCityName());
                java.util.List<ch.fhnw.bedwaste.model.Phone>  phones = contactInfo.getPhones();
                Phone phone = phones.get(0);

                insert_telnr.setText("(+" + phone.getCountryAccessCode() + ")" + phone.getPhoneNumber());


                //WLAN + Breakfast

                java.util.List<ch.fhnw.bedwaste.model.Service> services = hotelInfo.getServices();


                for (Service s : services) {
                    if (s.getCode().equals("343")) {
                        checkBox_wlan.setChecked(true);
                    }
                    if (s.getCode().equals("173")) {
                        checkBox_breakfast.setChecked(true);
                    }
                }


                //return amount of pictures from previously instantiated multimediadescription and use it for gallery code later.

                amount_hotel_pictures = hotel_images.size();
/*                String test = Integer.toString(amount_hotel_pictures);
                insert_hotelname.setText(test);*/


                for (int i = 0; i < amount_hotel_pictures; i++){
                    ImageView imageView = new ImageView(HotelInfoActivity.this);
                    imageView.setPadding(10,1,10,1);
                    String imageURLs = hotel_images.get(i).getImageUrl();
                    Picasso.get().load(imageURLs).resize(300, 300).into(imageView);
                    ((LinearLayout) findViewById(R.id.image_linear)).addView(
                            imageView, i);
                }





           }

            @Override
            public void failed(String message) {
                insert_hotelname.setText("message" );           }
        });


        service.getHotelDescriptiveInfo("en", hotellist_value);


        addBottomNavigation();

        // start of network connection check
        netDetector.networkRunnable.run();

    }

    private void addBottomNavigation() {
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
    }

    public static Intent makeHotelInfoIntent(Context cont, String extra){
        Intent hotelViewIntent = new Intent(cont, HotelInfoActivity.class);
        hotelViewIntent.putExtra(EXTRA_HOTEL_ID, extra);
        return hotelViewIntent;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
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
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
