package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.List;

import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.ContactInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.MultimediaDescription;
import ch.fhnw.bedwaste.model.MultimediaDescriptionImages;
import ch.fhnw.bedwaste.model.Phone;
import ch.fhnw.bedwaste.model.Service;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Response;

public class HotelInfoFragment extends Fragment {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelInfoFragment";
    private View view;
    private WelcomeViewModel model;
    private TextView hotelAddress;
    private TextView infoTextView;
    private TextView insert_hotelname;
    private TextView insert_starRating;
    private ImageView insert_banner;
    private TextView insert_km_away;
    private TextView insert_price;
    private TextView insert_street;
    private TextView insert_village;
    private TextView insert_telnr;
    private int amount_hotel_pictures;
    private CheckBox checkBox_breakfast;
    private CheckBox checkBox_wlan;
    private double userLocationLat;
    private double userLocationLng;
    private Button bookRoomsBtn;
    List<MultimediaDescriptionImages> hotel_images;
    AvailabilityResults availabilityResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_info_fragment, container, false);
        final HotelDescriptiveInfo hotelDescriptiveInfo = (HotelDescriptiveInfo) getActivity().getIntent().getSerializableExtra("hotel_descriptive_data");
        availabilityResults= new AvailabilityResults();
        model= new WelcomeViewModel();
        //receive values that got passed from previous activity
        final Intent intent = getActivity().getIntent();
        final String hotellist_value = intent.getStringExtra("key");

        hotelAddress =view.findViewById(R.id.text_Address);
        final TextView insert_hotelname = (TextView) view.findViewById(R.id.ph_hotelName);
        final TextView insert_starRating = (TextView) view.findViewById(R.id.ph_starRating);

        final ImageView insert_banner = (ImageView) view.findViewById(R.id.ph_hotelGeneralImage);

        final TextView insert_km_away = (TextView) view.findViewById(R.id.ph_amountMinutes);
        final TextView insert_price = (TextView) view.findViewById(R.id.ph_price);
        final TextView insert_address = (TextView) view.findViewById(R.id.ph_address);


        final TextView insert_village = (TextView) view.findViewById(R.id.ph_location);
        final TextView insert_telnr = (TextView) view.findViewById(R.id.ph_phoneNr);

        final CheckBox checkBox_breakfast = (CheckBox) view.findViewById(R.id.checkBox_Breakfast);
        final CheckBox checkBox_wlan = (CheckBox) view.findViewById(R.id.checkBox_Wlan);
        bookRoomsBtn = view.findViewById(R.id.button_booking);
        bookRoomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open new activity RoomTypes
                Intent intent = new Intent(getActivity(), RoomTypesActivity.class);
                intent.putExtra("hotel_key", hotellist_value);
                intent.putExtra("hotel_descriptive_data_for_rooms_activity", hotelDescriptiveInfo);
                intent.putExtra("availability_results_for_rooms_activity", availabilityResults);
                startActivity(intent);
            }
        });
        userLocationLat= getActivity().getIntent().getDoubleExtra("user_loc_lat", WelcomeViewModel.mDefaultLocation.latitude);
        userLocationLng = getActivity().getIntent().getDoubleExtra("user_loc_lng", WelcomeViewModel.mDefaultLocation.longitude);



                insert_hotelname.setText(hotelDescriptiveInfo.getHotelName());

                //iterate thrpugh amount of stars to create *** String
                java.util.List<ch.fhnw.bedwaste.model.Award> award_list;
                award_list = hotelDescriptiveInfo.getAffiliationInfo().getAwards();
                String star_amount_string = "";
                ch.fhnw.bedwaste.model.Award hotel_association_rating = award_list.get(0);

                String star_amount = hotel_association_rating.getRating();

                double stars =Math.floor(Double.parseDouble(star_amount));
                star_amount_string = new String(new char[(int)stars]).replace("", "★");

                insert_starRating.setText(star_amount_string);


                //insert_banner?!
                HotelInfo hotelInfo = hotelDescriptiveInfo.getHotelInfo();
                java.util.List<MultimediaDescription> multimediaDescriptions = hotelInfo.getDescriptions().getMultimediadescriptions();
                MultimediaDescription first_mmDescription = multimediaDescriptions.get(0);
                java.util.List<MultimediaDescriptionImages> hotel_images = first_mmDescription.getImages();
                MultimediaDescriptionImages banner_picture = hotel_images.get(0);
                String imageUrl_banner = banner_picture.getImageUrl();


                Picasso.get().load(imageUrl_banner).fit().into(insert_banner);


                //distance in km from the device location to the hotel location
                String distKm= WelcomeViewModel.getDistanceAsStringBetween(new LatLng(hotelInfo.getPosition().getLatitude().doubleValue(),
                                hotelInfo.getPosition().getLongitude().doubleValue()),
                        new LatLng(userLocationLat, userLocationLng));
                insert_km_away.setText(distKm);
                HotelAvailabilityResultsService service_price = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {

                    @Override
                    public void success(Response<AvailabilityResults> response) {
                        AvailabilityResults roomAvailabilityResults = response.body();
                        availabilityResults= roomAvailabilityResults;
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
                Integer streetNb= address.getStreetNmbr();
                String displayedNb = streetNb!= null ? streetNb.toString() : "";
                insert_address.setText(address.getAddressLine() + " " + displayedNb);
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
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setPadding(10,1,10,1);
                    final String imageURLs = hotel_images.get(i).getImageUrl();
                    Picasso.get().load(imageURLs).resize(300, 300).into(imageView);
                    imageView.setClickable(true);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Picasso.get().load(imageURLs).fit().into(insert_banner);
                        }
                    });

                    ((LinearLayout) view.findViewById(R.id.image_linear)).addView(
                            imageView, i);
                }


        return view;
    }
}
