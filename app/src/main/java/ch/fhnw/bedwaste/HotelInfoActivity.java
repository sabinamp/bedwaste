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
import android.text.TextUtils;
import android.widget.Toast;

import ch.fhnw.bedwaste.client.HotelItem;
import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.Award;
import ch.fhnw.bedwaste.model.ContactInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.Phone;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoInterface;
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
    private BottomNavigationView mBottomNavigationView;

    private TextView insert_hotelname;
    private TextView insert_starRating;
    private ImageView insert_banner;
    private TextView insert_minutes_away;
    private TextView insert_price;
    private TextView insert_street;
    private TextView insert_village;
    private TextView insert_telnr;


    private TextView hotelAddress;
    private TextView infoTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);
        hotelAddress = findViewById(R.id.text_Address);
        //receive values that got passed from previous activity
        final Intent intent = getIntent();
        String hotellist_value = intent.getStringExtra("key");

        final TextView insert_hotelname = (TextView) findViewById(R.id.ph_hotelName);
        final TextView insert_starRating = (TextView) findViewById(R.id.ph_starRating);
        final ImageView insert_banner = (ImageView) findViewById(R.id.ph_hotelGeneralImage);
        final TextView insert_minutes_away = (TextView) findViewById(R.id.ph_amountMinutes);
        final TextView insert_price = (TextView) findViewById(R.id.ph_price);
        final TextView insert_street = (TextView) findViewById(R.id.ph_streetPlusNr);
        final TextView insert_village = (TextView) findViewById(R.id.ph_location);
        final TextView insert_telnr = (TextView) findViewById(R.id.ph_phoneNr);


        TextView hotelPhone = (TextView) findViewById(R.id.ph_phoneNr);
        /*HotelItem hotelToDisplay= HotelListModel.fetchHotel(getIntent().getStringExtra(EXTRA_HOTEL_ID), HotelInfoActivity.this);
        if(hotelToDisplay!= null){
            hotelPhone.setText(hotelToDisplay.getPhone().getPhoneNumber());
        }*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://86.119.40.244:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HotelDescriptiveInfoInterface hotelDescriptiveInfoInterface = retrofit.create(HotelDescriptiveInfoInterface.class);

        Call<HotelDescriptiveInfo> call = hotelDescriptiveInfoInterface.getDescriptiveInfo("eng", hotellist_value);

        call.enqueue(new Callback<HotelDescriptiveInfo>() {
            @Override
            public void onResponse(Call<HotelDescriptiveInfo> call, Response<HotelDescriptiveInfo> response) {

                if(!response.isSuccessful()) {
                    insert_hotelname.setText("Code: " + response.code());

                    return;
                }

                HotelDescriptiveInfo hotelDescriptiveInfo = response.body();

                insert_hotelname.setText(hotelDescriptiveInfo.getHotelName());
                /**
                //iterate thrpugh amount of stars to create *** String
                java.util.List<ch.fhnw.bedwaste.model.Award> award_list;
                award_list = hotelDescriptiveInfo.getAffiliationInfo().getAwards();
                String star_amount_string = "";

                for (Award a : award_list)
                {
                    if(a.getProvider() == "HotelAssociation"){
                        star_amount_string = a.getRating();
                    }
                }

                int stars = Integer.parseInt(star_amount_string);
                String starstring = new String(new char[10]).replace("", "hello");

                insert_starRating.setText(starstring);


                //insert_banner?!


                //insert_minutes_away.setText(hotelDescriptiveInfo.get());

                //Not in Hotel DescriptiveInfo nut Availabilies()
                //insert_price.setText(hotelDescriptiveInfo.get());

                java.util.List<ch.fhnw.bedwaste.model.ContactInfo>  hotelDescriptiveInfoContactInfos= hotelDescriptiveInfo.getContactInfos();
                //takes first entry as main contact info
                ContactInfo contactInfo = hotelDescriptiveInfoContactInfos.get(0);
                java.util.List<ch.fhnw.bedwaste.model.Address> addresses  = contactInfo.getAddresses();
                Address address = addresses.get(0);
                //only nr? Possible Second TextView for Street?
                insert_street.setText(address.getStreetNmbr());
                insert_village.setText(address.getCityName());
                java.util.List<ch.fhnw.bedwaste.model.Phone>  phones = contactInfo.getPhones();
                Phone phone = phones.get(0);

                insert_telnr.setText(phone.getPhoneNumber());
*/

            }

            @Override
            public void onFailure(Call<HotelDescriptiveInfo> call, Throwable t) {
                insert_hotelname.setText(t.getMessage());
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
        addBottomNavigation();

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
