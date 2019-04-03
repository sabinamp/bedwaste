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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class HotelListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HotelListAdapter";
    private List<String> nameList;
    private Map<String, String> hotelsMap= new HashMap<>();
    private HotelDescriptiveInfoService controller;
    private Context context;

    public HotelListAdapter(List<String> list,Context context) {
        this.context = context;
        nameList = list;
        hotelsMap.put(nameList.get(0), "00U5846j022d292h");
        hotelsMap.put(nameList.get(1), "00I5846a022h291r");
        hotelsMap.put(nameList.get(2), "00U5846f022c291a");
        hotelsMap.put(nameList.get(3), "00U5846j022d291s");
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
        final String hotelName = nameList.get(position);
        final MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.hotelNameTextView.setText(hotelName);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hotelDescriptionIntent = HotelInfoActivity.makeHotelInfoIntent(v.getContext(), hotelsMap.get(nameList.get(position)));

                //pass a value to HotelInfoActivity, Hotelname FOR NOW, the hotel id extra is passed above as param to makeHotelInfoIntent
                hotelDescriptionIntent.putExtra("key", hotelName);

                context.startActivity(hotelDescriptionIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (nameList==null) {
            return 0;
        } else {
            return nameList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private HotelDescriptiveInfo hotelInfo;
        private AvailabilityResults availabilities;
        public TextView hotelNameTextView;
        public TextView hotelAddress;
        public TextView hotelPrice;
        public ImageView hotelThumbnail;
        private TextView minHotel;
        public MyViewHolder(View itemView) {
            super(itemView);
            hotelNameTextView = (TextView)itemView.findViewById(R.id.hotel_name);
            hotelThumbnail = (ImageView)itemView.findViewById(R.id.icon);
            hotelThumbnail.setVisibility(View.VISIBLE);
            hotelPrice = (TextView)itemView.findViewById(R.id.hotel_price);
            hotelAddress = (TextView)itemView.findViewById(R.id.hotel_address);
            minHotel = (TextView) itemView.findViewById(R.id.min_hotel);
            itemView.setClickable(true);
        }

        //method to call within the adapter's onBindViewHolder() after fetching data from the server
        private void bind(HotelDescriptiveInfo info, AvailabilityResults results){
            this.hotelInfo = info;
            this.availabilities = results;
            hotelNameTextView.setText(hotelInfo.getHotelName());
            //hotelThumbnail.setImageURI(hotelInfo.getHotelInfo().getDescriptions().getMultimediadescriptions().get("img/index"));
            hotelPrice.setText(hotelInfo.getCurrencyCode() + "" + results.get(0).getProducts().get(0).getTotalPrice());
            hotelAddress.setText(hotelInfo.getContactInfos().get(0).getAddresses().get(1).getAddressLine());
            minHotel.setText(getDistanceToHotel_Minutes() + "Min");

        }

        private int getDistanceToHotel_Minutes() {
            long hotelAltitude = hotelInfo.getHotelInfo().getPosition().getAltitude().longValue();
            long hotelLongitude = hotelInfo.getHotelInfo().getPosition().getLongitude().longValue();
            //todo
            return 3;
        }
        private int getNumberofStars(){

            //default
            return 2;
        }

    }
}
