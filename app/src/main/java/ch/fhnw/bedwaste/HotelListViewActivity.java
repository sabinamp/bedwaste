package ch.fhnw.bedwaste;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.MenuItem;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;


public class HotelListViewActivity extends AppCompatActivity {
    // Keys for storing activity state.
    private static final String KEY_LIST_HOTEL = "hotel_list";

    private BottomNavigationView mBottomNavigationView;
    private Button btn;
    private RecyclerView recyclerView;
    private List<HotelDescriptiveInfo> itemList= null;
    private NetworkDetector netDetector = new NetworkDetector(HotelListViewActivity.this);
    private HotelListModel listmodel=null;
    private HotelListAdapter myAdapter;

    //This flag is required to avoid first time onResume refreshing
    static boolean loaded = false;
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG = "HotelListViewActivity";
    ArrayList<String> passedIds=null;

    public HotelListViewActivity() {
        listmodel = new HotelListModel();
        itemList= new ArrayList<>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list_view);
        Log.d(TAG, "HotelListViewActivity Activity - onCreate(Bundle) called");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        //passed from WelcomeActivity
        passedIds= getIntent().getStringArrayListExtra("bedwaste_hotel_list");
        if(passedIds.isEmpty()){
            ArrayList<String> allhotels=new ArrayList<>();
            allhotels.addAll(WelcomeViewModel.ALL_IDS);
            listmodel.retrieveAllHotelDescriptiveData(allhotels);
        }


        itemList=  listmodel.retrieveAllHotelDescriptiveData(passedIds);
        //list adapter
        myAdapter = new HotelListAdapter(itemList, HotelListViewActivity.this);
        recyclerView.setAdapter(myAdapter);


        addBottomNavigation();

        netDetector.networkRunnable.run();

    }

    private void addBottomNavigation() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:
                    {
                        Intent listIntent= new Intent(HotelListViewActivity.this, LoginActivity.class);
                        startActivity(listIntent);
                        return true;
                    }
                    case R.id.app_bar_map_view:
                    {
                        Intent welcomeIntent= new Intent(HotelListViewActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        return true;
                    }
                    case R.id.app_bar_hotel_list: {
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

    public static Intent makeHotelListIntent(Context cont){
        Intent hotelListIntent = new Intent(cont, HotelListViewActivity.class);
        return hotelListIntent;
    }

    private void setLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");

    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called");
        super.onResume();
        if (!loaded) {
            //First time just set the loaded flag true
            loaded = true;
        } else {
            Log.i("Resuming", "back to my first activity");
            //Reload data
            itemList.clear();
            itemList = listmodel.retrieveAllHotelDescriptiveData(passedIds);

            myAdapter.notifyDataSetChanged();
            //myAdapter.setNotifyOnChange(true);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");
        super.onPause();

    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
   /* *//**
     * Saves the last location on configuration change
     *//*
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.v(TAG, "Inside of onSaveInstanceState");
        if (passedIds != null) {
            state.putStringArrayList(KEY_LIST_HOTEL, passedIds);


        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "Inside of onRestoreInstanceState");
        passedIds= savedInstanceState.getStringArrayList(KEY_LIST_HOTEL);

    }*/
}
