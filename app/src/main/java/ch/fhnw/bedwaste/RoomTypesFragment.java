package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;

public class RoomTypesFragment extends Fragment {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "RoomTypesFragment";
    private View view;
    private RecyclerView recyclerView;
    private List<AvailabilityResult> roomAvailabilitiesList;
    private RoomTypesAdapter roomTypesAdapter;
    private static final String HOTEL_ID_KEY = "tracking_hotel";
    private String hotelId_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.room_types_list_fragment, container, false);
        roomAvailabilitiesList= new ArrayList<>();

        //receive values that got passed from previous activity
        final Intent intent = getActivity().getIntent();
        hotelId_value = intent.getStringExtra("hotel_key");
        Log.d(TAG, " - onCreate(Bundle) called. ");
        if(savedInstanceState != null){
            hotelId_value = savedInstanceState.getString(HOTEL_ID_KEY);
        }
        recyclerView = view.findViewById(R.id.room_types_recyclerView);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();

        updateUI(hotelId_value);

        Log.d(TAG, " - onCreate(Bundle) completed. Loading room types completed ");
        return view;
    }
    private void updateUI(String hotelId){
        RoomTypesModel roomtypes_listModel= new RoomTypesModel(getActivity(), hotelId);
        roomAvailabilitiesList = roomtypes_listModel.getAvailabilityResults();
        Log.d(TAG, "onCreate() loading "+"completed - retrieved  availabilities for. hotel with id "+hotelId);
        if(roomTypesAdapter == null){
            roomTypesAdapter = new RoomTypesAdapter(getActivity(), roomAvailabilitiesList);
            recyclerView.setAdapter(roomTypesAdapter);
        }else{
            roomTypesAdapter.notifyDataSetChanged();
        }


    }
    private void setLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    /**
     * Saves the last passed ids on configuration change
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(HOTEL_ID_KEY, hotelId_value);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "starting the activity");
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI(hotelId_value);

        Log.d(TAG, "resuming back to the RoomTypesFragment");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory() called.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}
