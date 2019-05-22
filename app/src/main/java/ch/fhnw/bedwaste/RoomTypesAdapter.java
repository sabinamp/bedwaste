package ch.fhnw.bedwaste;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.ecommerce.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import ch.fhnw.bedwaste.model.AvailabilityResult;

import ch.fhnw.bedwaste.model.GuestRoom;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfoFacilityInfo;
import ch.fhnw.bedwaste.model.MultimediaDescription;
import ch.fhnw.bedwaste.model.MultimediaDescriptionImages;
import ch.fhnw.bedwaste.model.RoomAvailabilityResult;

public class RoomTypesAdapter extends RecyclerView.Adapter<RoomTypesAdapter.RoomTypeViewHolder> {

    private static final String TAG = "RoomTypesAdapter";
    private List<AvailabilityResult> pricesRoomsList;
    private HotelDescriptiveInfo mHotelDescriptiveInfo;

    private Context context;

    public RoomTypesAdapter(Context context, List<AvailabilityResult> items, HotelDescriptiveInfo hotelDescriptiveInfo){
        this.context=context;
        pricesRoomsList= items;
        mHotelDescriptiveInfo= hotelDescriptiveInfo;
    }
    @NonNull
    @Override
    public RoomTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hotel_room_type, parent, false);
        RoomTypesAdapter.RoomTypeViewHolder viewHolder = new RoomTypeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTypeViewHolder roomTypeViewHolder, int position) {
        final AvailabilityResult item= pricesRoomsList.get(position);
        final RoomTypeViewHolder holder= roomTypeViewHolder;
        final String ratePlanId= pricesRoomsList.get(position).getRateplanId();
        holder.bind(item, mHotelDescriptiveInfo, position);

    }

    @Override
    public int getItemCount() {
        if(pricesRoomsList==null){
            return 0;
        }
        return pricesRoomsList.size();
    }

    public static class RoomTypeViewHolder extends RecyclerView.ViewHolder {
        private AvailabilityResult roomItem;
        TextView roomNameTextView;
        TextView roomInclusiveTextView;
        TextView roomPrice;
        TextView regularPrice;
        ImageView roomThumbnail;
        ImageView roomTypeIcon1;
        Button chooseBtn;


        public RoomTypeViewHolder(View itemView) {
            super(itemView);
            roomNameTextView= itemView.findViewById(R.id.room_name);
            roomInclusiveTextView = itemView.findViewById(R.id.room_inclusive);
            roomPrice = itemView.findViewById(R.id.room_price);
            regularPrice = itemView.findViewById(R.id.regular_price);
            roomThumbnail = (ImageView)itemView.findViewById(R.id.room_thumbnail);
            roomTypeIcon1 = itemView.findViewById(R.id.roomtype_icon1);
            chooseBtn = itemView.findViewById(R.id.choose_btn);
        }
        //method to call within the adapter's onBindViewHolder() after fetching data from the server
        private void bind(AvailabilityResult roomItem, HotelDescriptiveInfo hotelDescriptiveInfo, int position) {
            roomItem=roomItem;
            String[] line = roomItem.getRateplanId().split("-");
            String hotelId= line[0];
            String roomName= line[1] != null ? line[1]: "";
            roomNameTextView.setText(roomName);
            Float rePrice= roomItem.getTotalPrice();
            Integer rPrice = Math.round(rePrice);
            String price = rPrice != null ? rPrice.toString()+" CHF/Nacht" : "";
            roomPrice.setText(price);
            double regPrice= rPrice*1.15;
            int regular_Price=(int) Math.round(regPrice);
            regularPrice.setText("Regulär: "+String.valueOf(regular_Price) + " CHF");
            if(hotelDescriptiveInfo != null){
                HotelDescriptiveInfoFacilityInfo facilityInfo= hotelDescriptiveInfo.getFacilityInfo();
                if(facilityInfo != null){
                    //int totalNbOfRooms= facilityInfo.getGuestRooms().size();

                    //room
                    GuestRoom room= hotelDescriptiveInfo.getFacilityInfo().getGuestRooms().get(position);
                    //room image
                    java.util.List<MultimediaDescription> multimediaDescriptions = room.getMultimediaDescriptions();
                    MultimediaDescription first_mmDescription = multimediaDescriptions.get(0);
                    java.util.List<MultimediaDescriptionImages> room_images = first_mmDescription.getImages();
                    MultimediaDescriptionImages room_picture = room_images.get(0);
                    String imageUrl_room = room_picture.getImageUrl();
                    Picasso.get().load(imageUrl_room)
                            .resize(560, 350).centerCrop()
                            .into(roomThumbnail);
                }
            }




        }
    }
}
