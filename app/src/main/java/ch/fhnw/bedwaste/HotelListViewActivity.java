package ch.fhnw.bedwaste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.MenuItem;

import android.widget.Button;
import java.util.Arrays;


public class HotelListViewActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private Button btn;
    private RecyclerView recyclerView;
    private final static String[] items = {"Hotel Helmhaus", "Hotel Hottingen","Hotel Platzhirsch","Hotel Villette"};
    /**
     * Debugging tag ProfileActivity used by the Android logger.
     */
    private static final String TAG = "HotelListViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list_view);
        Log.d(TAG, "HotelListViewActivity Activity - onCreate(Bundle) called");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        setViewAdapter();
        addListDivider();

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_profile:
                    {
                        Intent listIntent= new Intent(HotelListViewActivity.this, ProfileActivity.class);
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

    private void addListDivider(){
        Drawable divider=getResources().getDrawable(R.drawable.item_divider);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(divider));
    }
    private void setViewAdapter(){
        HotelListAdapter myAdapter = new HotelListAdapter(Arrays.asList(items));
        recyclerView.setAdapter(myAdapter);
    }
}
