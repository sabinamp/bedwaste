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


    private Button btn;
    private RecyclerView recyclerView;
    private List<HotelDescriptiveInfo> itemList;
    private HotelListAdapter myAdapter;
    private LatLng userLocation;
    private View view;
    //This flag is required to avoid first time onResume refreshing
    static boolean loaded = false;
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelListFragment";
    ArrayList<String> passedIds=null;

    public HotelListFragment() {
        itemList= new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_list_view_fragment, container, false);

        loaded=false;
        Log.d(TAG, "HotelListFragment Activity - onCreate(Bundle) called. Loading in onCreate() is "+loaded);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        //passed from WelcomeActivity
        userLocation= new LatLng(getActivity().getIntent().getDoubleExtra("USER_LOC_LATITUDE", WelcomeViewModel.mDefaultLocation.latitude),
                getActivity().getIntent().getDoubleExtra("USER_LOC_LONGITUDE", WelcomeViewModel.mDefaultLocation.longitude));
        updateUI();


        Log.d(TAG, "HotelListFragment Activity - onCreate(Bundle) completed. First time loading completed "+loaded);
        return view;
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

    private void updateUI(){
        HotelListModel listmodel=null;
        //passed from WelcomeActivity
        passedIds= getActivity().getIntent().getStringArrayListExtra("bedwaste_hotel_list");
        if(passedIds.isEmpty()){
            ArrayList<String> allhotels=new ArrayList<>();
            allhotels.addAll(WelcomeViewModel.ALL_IDS);
            listmodel = new HotelListModel(getActivity(),allhotels);
            itemList=listmodel.getItems();
            //list adapter
            myAdapter = new HotelListAdapter(itemList,userLocation, getActivity());
            loaded=true;
            Log.d(TAG, "onCreate() loading " + loaded +"completed - retrieved all hotels.");
        }else{
            listmodel = new HotelListModel(getActivity(), passedIds);
            itemList=  listmodel.getItems();
            //list adapter
            myAdapter = new HotelListAdapter(itemList, userLocation,getActivity());
            loaded=true;
            Log.d(TAG, "onCreate() loading " + loaded +"completed - retrieved the hotels based on the passed ids.");
        }

        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }




    private void setLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }





}
