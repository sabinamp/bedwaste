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
import java.util.List;

import ch.fhnw.bedwaste.model.AvailabilityResult;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.RoomAvailabilityResult;

public class RoomTypesAdapter extends RecyclerView.Adapter<RoomTypesAdapter.RoomTypeViewHolder> {

    private static final String TAG = "RoomTypesAdapter";
    private List<AvailabilityResult> pricesRoomsList;

    private Context context;

    public RoomTypesAdapter(Context context, List<AvailabilityResult> items){
        this.context=context;
        pricesRoomsList= items;
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
        holder.bind(item);

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
            roomThumbnail = itemView.findViewById(R.id.room_thumbnail);
            roomTypeIcon1 = itemView.findViewById(R.id.roomtype_icon1);
            chooseBtn = itemView.findViewById(R.id.choose_btn);
        }
        //method to call within the adapter's onBindViewHolder() after fetching data from the server
        private void bind(AvailabilityResult roomItem) {
            roomItem=roomItem;
            String[] line = roomItem.getRateplanId().split("-");
            String roomName= line[1] != null ? line[1]: "";
            roomNameTextView.setText(roomName);
            Float rPrice= roomItem.getTotalPrice();
            String price = rPrice != null ? rPrice.toString()+" CHF/Nacht" : "";
            roomPrice.setText(price);


        }
    }
}
