package ch.fhnw.bedwaste;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
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
    private List<HotelDescriptiveInfo> mItems= null;

    private Map<String, HotelDescriptiveInfo> hotelId_descriptiveInfo;
    Context context;

    HotelListModel(Context context, List<String> ids) {
        this.context=context;
        mItems = new ArrayList<>();
        hotelId_descriptiveInfo = new HashMap<>();
        retrieveAllHotelDescriptiveData(ids);
    }


    public List<HotelDescriptiveInfo> getItems(){
        return mItems;
    }

    public HotelDescriptiveInfo getHotelDescriptiveInfoH(String hotelId){
        return hotelId_descriptiveInfo.get(hotelId);
    }

    private void updateHotelId_descriptiveInfo(String id, HotelDescriptiveInfo hotelId_descriptiveInfo) {
        this.hotelId_descriptiveInfo.put(id, hotelId_descriptiveInfo);
    }

/*    private List<HotelDescriptiveInfo> retrieveAllHotelDescriptiveData(List<String> ids){
        for (final String eachId : ids) {
            Log.d("TAG", "hotel ids' number: "+ eachId);
            Log.d(TAG, "start retrieveHotelDescriptiveData - fetching data from the server");
            HotelDescriptiveInfoService service_description = new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
                @Override
                public void success(Response<HotelDescriptiveInfo> response) {
                    HotelDescriptiveInfo hotelDescriptiveInfo = response.body();
                    updateHotelId_descriptiveInfo(eachId, hotelDescriptiveInfo);
                    mItems.add(hotelDescriptiveInfo);

                }

                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch hotel descriptive info" + message);
                }
            });
            service_description.getHotelDescriptiveInfo("en", eachId);
        }
        Log.d(TAG, "retrieveHotelDescriptiveData()- fetching data from the server - completed. Number of hotels : "+ids.size());
        return  mItems;
    }*/
    private List<HotelDescriptiveInfo> retrieveAllHotelDescriptiveData(List<String> ids){
        for (final String eachId : ids) {
            Log.d("TAG", "hotel ids' number: "+ eachId);
            try{
                Log.d(TAG, "start retrieveHotelDescriptiveData - reading data from internal storage");
                Map<String, HotelDescriptiveInfo> alldescriptiveInfo= (Map<String, HotelDescriptiveInfo>)InternalStorage.readObject(context, "descriptive_info_all_hotels" );
                HotelDescriptiveInfo hotelDescriptiveInfo = alldescriptiveInfo.get(eachId);
                updateHotelId_descriptiveInfo(eachId, hotelDescriptiveInfo);
                mItems.add(hotelDescriptiveInfo);
            }catch(IOException ex){
                Log.e(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+ex.getMessage());
                Log.d(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+ex.getMessage());
            }catch (ClassNotFoundException e){
                Log.e(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+e.getMessage());
                Log.d(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+e.getMessage());
            }

        }
        Log.d(TAG, "retrieveHotelDescriptiveData()- reading data from local storage - completed. Number of hotels : "+ids.size());
        return  mItems;
    }



}
