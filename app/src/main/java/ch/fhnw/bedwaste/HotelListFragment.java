package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

import android.app.Fragment;


public class HotelListFragment extends Fragment {
    // Keys for storing activity state.
    private static final String KEY_LIST_HOTEL = "hotel_list";
    private static final String HOTEL_IDS_KEY = "tracking_ids";

    private Button btn;
    private RecyclerView recyclerView;
    private List<HotelDescriptiveInfo> itemList;
    private HotelListAdapter myAdapter;
    private LatLng userLocation;
    private View view;
    private ArrayList<String> item_ids;
    //This flag is required to avoid first time onResume refreshing
    static boolean loaded = false;
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelListFragment";
    ArrayList<String> passedIds=null;

    public HotelListFragment() {
        itemList= new ArrayList<>();
        passedIds = new ArrayList<>();
        item_ids = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_list_view_fragment, container, false);

        if(savedInstanceState != null){
            //get saved passedIds
            item_ids= savedInstanceState.getStringArrayList(HOTEL_IDS_KEY);
        }
        loaded=false;
        Log.d(TAG, "HotelListFragment Activity - onCreate(Bundle) called. Loading in onCreate() is "+loaded);
        recyclerView = view.findViewById(R.id.hotel_list_recyclerView);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        //passed from WelcomeActivity
        userLocation= new LatLng(getActivity().getIntent().getDoubleExtra("USER_LOC_LATITUDE", WelcomeViewModel.mDefaultLocation.latitude),
                getActivity().getIntent().getDoubleExtra("USER_LOC_LONGITUDE", WelcomeViewModel.mDefaultLocation.longitude));
        updateUI();


        Log.d(TAG, "HotelListFragment Activity - onCreate(Bundle) completed. First time loading completed "+loaded);
        return view;
    }
    /**
     * Saves the last passed ids on configuration change
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(HOTEL_IDS_KEY, item_ids);
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
        Log.d(TAG, "starting the HotelListFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(loaded){
            myAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "resuming back to the HotelListFragment");
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

    private void updateUI(){
        HotelListModel listmodel=null;
        passedIds= getActivity().getIntent().getStringArrayListExtra("bedwaste_hotel_list");
        //passed from WelcomeActivity
        if(passedIds== null ||passedIds.isEmpty()){
            item_ids=new ArrayList<>();
            item_ids.addAll(WelcomeViewModel.ALL_IDS);

        }else{
            item_ids= passedIds;
            Log.d(TAG, "onCreate() - the passed ids from the previous activity have size : "+passedIds.size());
        }
        listmodel = new HotelListModel(getActivity(),item_ids);
        itemList=listmodel.getItems();
        //list adapter
        myAdapter = new HotelListAdapter(itemList,userLocation, getActivity());
        loaded=true;
        Log.d(TAG, "onCreate() loading " + loaded +"completed - retrieved all hotels.");
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }




    private void setLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


}
