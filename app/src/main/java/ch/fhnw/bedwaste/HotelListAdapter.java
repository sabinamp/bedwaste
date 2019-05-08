package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

import ch.fhnw.bedwaste.client.HotelItem;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;

public class HotelListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HotelListAdapter";
    private List<HotelItem> hotelList;
    private HotelDescriptiveInfoService controller;
    private Context context;

    private LatLng userLocation;

    public HotelListAdapter(List<HotelItem> list,Context context) {
        this.context = context;
        hotelList = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hotel_list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final HotelItem hotel = hotelList.get(position);
        final String hotelName = hotelList.get(position).getName();
        final MyViewHolder holder = (MyViewHolder) viewHolder;
        final String hotel_id = hotelList.get(position).getHotelId();
       // holder.hotelNameTextView.setText(hotelName);
        holder.bind(hotel, userLocation);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hotelDescriptionIntent = HotelInfoActivity.makeHotelInfoIntent(v.getContext(), hotelList.get(position).getHotelId());

                //pass a value to HotelInfoActivity, Hotelname FOR NOW, the hotel id extra is passed above as param to makeHotelInfoIntent
                hotelDescriptionIntent.putExtra("key", hotelName);
                context.startActivity(hotelDescriptionIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (hotelList ==null) {
            return 0;
        } else {
            return hotelList.size();
        }
    }
    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private HotelItem hotelItem;
        TextView hotelNameTextView;
        TextView hotelAddressLine;
        TextView hotelCity;
        TextView hotelPrice;
        TextView hotelRating;
        ImageView hotelThumbnail;
        private TextView minHotel;
        public MyViewHolder(View itemView) {
            super(itemView);
            hotelNameTextView = (TextView)itemView.findViewById(R.id.hotel_name);
            hotelRating = (TextView)itemView.findViewById(R.id.hotel_rating);
            hotelThumbnail = (ImageView)itemView.findViewById(R.id.icon);
            hotelThumbnail.setVisibility(View.VISIBLE);
            hotelPrice = (TextView)itemView.findViewById(R.id.hotel_price);
            hotelAddressLine = (TextView)itemView.findViewById(R.id.hotel_address_line);
            hotelCity = (TextView)itemView.findViewById(R.id.hotel_city);
            minHotel = (TextView) itemView.findViewById(R.id.min_hotel);

            itemView.setClickable(true);
        }

        //method to call within the adapter's onBindViewHolder() after fetching data from the server
        private void bind(HotelItem hotelItem, LatLng userLocation){
            this.hotelItem = hotelItem;
            String name = hotelItem.getName() != null ? hotelItem.getName() : "";
            String description = hotelItem.getDescription() != null ? hotelItem.getDescription() : "";
            //String price = hotelItem.getAvailabilities() != null? hotelItem.getAvailabilities().get(0).getTotalPrice().intValue() + "CHF" : "200 CHF";
            /*String street = hotelItem.getAddress().getAddressLine() != null ? hotelItem.getAddress().getAddressLine() : "";
            String city_zipcode = hotelItem.getAddress().getCityName() != null ? hotelItem.getAddress().getCityName()+ hotelItem.getAddress().getPostalCode() : "";
            */
            String rating =  hotelItem.getRating() +"/10" ;
            String distance = hotelItem.getWalkingDistanceToHotelInMinutes(userLocation)+"Min";
            hotelNameTextView.setText(name);
            //hotelThumbnail.setImageURI(hotelInfo.getHotelInfo().getDescriptions().getMultimediadescriptions().get("img/index"));
            /*hotelPrice.setText(price);
            hotelAddressLine.setText(street);
            hotelCity.setText(city_zipcode );*/
            hotelRating.setText(rating);
            minHotel.setText(distance);

       

        }



    }
}
