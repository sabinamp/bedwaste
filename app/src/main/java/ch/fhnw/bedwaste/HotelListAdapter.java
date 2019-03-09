package ch.fhnw.bedwaste;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HotelListAdapter extends RecyclerView.Adapter {
    private List<String> nameList;

    public HotelListAdapter(List<String> list) {
        nameList = list;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final String hotelName = nameList.get(position);
        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.hotelNameTextView.setText(hotelName);
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
        public MyViewHolder(View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.hotel_name);
        }
    }
}
