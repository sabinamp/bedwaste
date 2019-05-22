package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.MultimediaDescription;
import ch.fhnw.bedwaste.model.MultimediaDescriptionImages;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import retrofit2.Response;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.MyViewHolder> {
    private static final String TAG = "HotelListAdapter";

    private List<HotelDescriptiveInfo> hotelList;
    private Context context;

    private LatLng userLocation;

    public HotelListAdapter( List<HotelDescriptiveInfo> list, LatLng userLoc, Context context) {
        this.context = context;
        hotelList = list;
        userLocation= userLoc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hotel_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int position) {
        final HotelDescriptiveInfo hotel = hotelList.get(position);
        boolean notNullHotel= hotel!=null;
        Log.d(TAG, "current hotel is not null: "+notNullHotel);
        final MyViewHolder holder = viewHolder;
        if(notNullHotel){
            final String hotelId = hotel.getHotelId();

            holder.bind(hotel, userLocation);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent hotelDescriptionIntent = HotelInfoActivity.makeHotelInfoIntent(v.getContext(), hotelId);

                    hotelDescriptionIntent.putExtra("key", hotelId);
                    hotelDescriptionIntent.putExtra("user_loc_lat",getUserLocation().latitude);
                    hotelDescriptionIntent.putExtra("user_loc_lng", getUserLocation().longitude);
                    context.startActivity(hotelDescriptionIntent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (hotelList !=null) {
            return hotelList.size();
        }else{
            return 0;
        }
    }

  /*  public void update(List<HotelDescriptiveInfo> data) {
        hotelList.clear();
        hotelList=data;
        notifyDataSetChanged();
    }*/
    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private HotelDescriptiveInfo hotelItem;
        TextView hotelNameTextView;
        TextView hotelAddressLine;
        TextView hotelCity;
        TextView hotelPrice;
        TextView hotelRating;
        TextView hotelStars;
        ImageView hotelThumbnail;
        TextView minHotel;
        Button bookBtn;
        //TextView distanceKmTextview;

        public MyViewHolder(View itemView) {
            super(itemView);
            hotelNameTextView = (TextView)itemView.findViewById(R.id.hotel_name);
            hotelRating = (TextView)itemView.findViewById(R.id.hotel_rating);
            hotelThumbnail = (ImageView)itemView.findViewById(R.id.icon);
            //hotelThumbnail.setVisibility(View.VISIBLE);
            hotelPrice = (TextView)itemView.findViewById(R.id.hotel_price);
            hotelAddressLine = (TextView)itemView.findViewById(R.id.hotel_address_line);
            hotelCity = (TextView)itemView.findViewById(R.id.hotel_city);
            minHotel = (TextView) itemView.findViewById(R.id.min_hotel);
            //distanceKmTextview= (TextView)itemView.findViewById(R.id.distance_km);
            hotelStars= (TextView) itemView.findViewById(R.id.nb_stars) ;
            bookBtn= (Button)itemView.findViewById(R.id.book_rooms);

            itemView.setClickable(true);
        }

        //method to call within the adapter's onBindViewHolder() after fetching data from the server
        private void bind(HotelDescriptiveInfo hotelItem, LatLng userLocation){
            this.hotelItem = hotelItem;
            String name = hotelItem.getHotelName() != null ? hotelItem.getHotelName() : "";


            String addressLine1= hotelItem.getContactInfos().get(0).getAddresses().get(0).getAddressLine();
            Address address=hotelItem.getContactInfos().get(0).getAddresses().get(0);

            String streetName = addressLine1!= null ? addressLine1 : "";
            Integer streetNb= address.getStreetNmbr();
            String displayedNb = streetNb!= null ? streetNb.toString() : "";

            String city=address.getCityName();
            String city_zipcode = city != null ? city + " "+ address.getPostalCode(): "";

            String rating =  hotelItem.getAffiliationInfo().getAwards().get(1).getRating() +"/10" ;
            LatLng hotelLoc= new LatLng(hotelItem.getHotelInfo().getPosition().getLatitude().doubleValue(),
                                 hotelItem.getHotelInfo().getPosition().getLongitude().doubleValue());
            String distanceKmString= WelcomeViewModel.getDistanceAsStringBetween(new LatLng(userLocation.latitude,
                    userLocation.longitude), hotelLoc);
           /* double distanceKm = WelcomeViewModel.getDistanceBetween(new LatLng(userLocation.latitude,
                    userLocation.longitude), hotelLoc);

            String distanceInHoursString= WelcomeViewModel.getDistanceInHoursasString(distanceKm);*/

            hotelNameTextView.setText(name);
            String star_amount = hotelItem.getAffiliationInfo().getAwards().get(0).getRating();

            double stars =Math.floor(Double.parseDouble(star_amount));
            String star_amount_string = new String(new char[(int)stars]).replace("", "★");


            //hotel image
            HotelInfo hotelInfo = hotelItem.getHotelInfo();
            java.util.List<MultimediaDescription> multimediaDescriptions = hotelInfo.getDescriptions().getMultimediadescriptions();
            MultimediaDescription first_mmDescription = multimediaDescriptions.get(0);
            java.util.List<MultimediaDescriptionImages> hotel_images = first_mmDescription.getImages();
            MultimediaDescriptionImages banner_picture = hotel_images.get(0);
            String imageUrl_banner = banner_picture.getImageUrl();
            Picasso.get().load(imageUrl_banner)
            .resize(584, 350).centerCrop()
                    //.placeholder(R.drawable.ic_location_city_blue_240dp)
                    .into(hotelThumbnail);

            hotelStars.setText(star_amount_string);
            hotelAddressLine.setText(streetName+" "+displayedNb);
            hotelCity.setText(city_zipcode );
            hotelRating.setText(rating);

            minHotel.setText(distanceKmString);
            //distanceKmTextview.setText(distanceKmString);
            HotelAvailabilityResultsService service_price = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {

                @Override
                public void success(Response<AvailabilityResults> response) {
                    AvailabilityResults roomAvailabilityResults = response.body();
                    double price = roomAvailabilityResults.get(0).getProducts().get(0).getTotalPrice();
                    hotelPrice.setText(String.valueOf((int)price)+" CHF/Nacht ");
                }
                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch availability results" + message);
                }
            });
            service_price.getRoomAvailabilitiesInHotel(hotelItem.getHotelId(), 1, 0, 0);

        }
    }


}
