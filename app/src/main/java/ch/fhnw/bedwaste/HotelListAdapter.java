package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.client.HotelDescriptiveInfoController;
import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.ContactInfo;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class HotelListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HotelListAdapter";
    private List<String> nameList;
    private Map<String, String> hotelsMap= new HashMap<>();
    private HotelDescriptiveInfoController controller;
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
                Intent hotelDescriptionIntent = HotelInfoActivity.makeHotelInfoIntent(v.getContext(), nameList.get(position));

                //pass a value to HotelInfoActivity, Hotelname FOR NOW, later ID
                hotelDescriptionIntent.putExtra("key", hotelName);
                context.startActivity(hotelDescriptionIntent);

                /*controller = new HotelDescriptiveInfoController();
                String hoteln= nameList.get(position);
                controller.start("en", hotelsMap.get(hoteln));
                String error = controller.getErrorCode();
                if(!(error.isEmpty() || error == null)){
                    holder.hotelAddress.setText(error);
                    Log.d(TAG, "HotelListViewActivity Activity - could not fetch data from the server");
                } else {
                    HotelDescriptiveInfo hotelInfo = controller.getHotelinfo();
                    String content = "";
                    ContactInfo contact=hotelInfo.getContactInfos().get(0);
                    content +=  contact.getCompanyName() +"\n";
                    Address address= contact.getAddresses().get(0);
                    content +=  address.getAddressLine() +"\n";
                    content +=  address.getStreetNmbr() +"\n";
                    content +=  address.getCityName() +"\n";
                    content +=  address.getCountryName() +"\n";
                    holder.hotelAddress.setText(content);
                    Log.d(TAG, "HotelListViewActivity Activity - fetched data from the server");
                } */
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
        public TextView hotelNameTextView;
        public TextView hotelAddress;
        public TextView hotelPrice;
        public ImageView hotelThumbnail;
        public MyViewHolder(View itemView) {
            super(itemView);
            hotelNameTextView = (TextView)itemView.findViewById(R.id.hotel_name);
            hotelThumbnail = (ImageView)itemView.findViewById(R.id.icon);

            hotelPrice = (TextView)itemView.findViewById(R.id.hotel_price);
            hotelAddress = (TextView)itemView.findViewById(R.id.hotel_address);
            hotelThumbnail.setVisibility(View.VISIBLE);
            itemView.setClickable(true);
        }


    }
}
