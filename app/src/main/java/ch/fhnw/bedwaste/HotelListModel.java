package ch.fhnw.bedwaste;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Response;

public class HotelListModel {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="HotelListModel";
    /**
     * List of hotel ids passed from WelcomeActivity
     */
    private ArrayList<String> HOTEL_IDS=null;
    private List<HotelDescriptiveInfo> items= null;

    private Map<String, HotelDescriptiveInfo> hotelId_descriptiveInfo=null;

    public HotelListModel() {
        items = new ArrayList<>();
        hotelId_descriptiveInfo = new HashMap<>();
        HOTEL_IDS= new ArrayList<>();
    }

    void updateHotel_IDS(List<String> newList){
        HOTEL_IDS.clear();
        HOTEL_IDS.addAll(newList);

    }

    public List<HotelDescriptiveInfo> getItems() {
        if(!(HOTEL_IDS.isEmpty())){
            retrieveAllHotelDescriptiveData(HOTEL_IDS);
        }else{
            ArrayList<String> allhotels=new ArrayList<>();
            allhotels.addAll(WelcomeViewModel.ALL_IDS);
            retrieveAllHotelDescriptiveData(allhotels);
        }
        return items;
    }

    private Map<String, HotelDescriptiveInfo> getHotelId_descriptiveInfo() {
        return Collections.unmodifiableMap(hotelId_descriptiveInfo);
    }

    private void updateHotelId_descriptiveInfo(String id, HotelDescriptiveInfo hotelId_descriptiveInfo) {
        this.hotelId_descriptiveInfo.put(id, hotelId_descriptiveInfo);
    }
    private void retrieveAllHotelDescriptiveData(List<String> ids){
        for (final String eachId : ids) {
            Log.d("TAG", "hotel ids: "+ HOTEL_IDS);
            Log.d(TAG, "start retrieveHotelDescriptiveData - fetching data from the server");
            HotelDescriptiveInfoService service_description = new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
                @Override
                public void success(Response<HotelDescriptiveInfo> response) {
                    HotelDescriptiveInfo hotelDescriptiveInfo = response.body();
                    updateHotelId_descriptiveInfo(eachId, hotelDescriptiveInfo);
                    items.add(hotelDescriptiveInfo);

                }

                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch hotel descriptive info" + message);
                }
            });
            service_description.getHotelDescriptiveInfo("en", eachId);
        }
        Log.d(TAG, "retrieveHotelDescriptiveData()- fetching data from the server - completed");
    }




}
