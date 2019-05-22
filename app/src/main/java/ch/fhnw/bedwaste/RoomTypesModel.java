package ch.fhnw.bedwaste;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoListener;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;
import retrofit2.Response;


public class RoomTypesModel {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="HotelListModel";

 /*   public HotelDescriptiveInfo getmItem() {
        return mItem;
    }

    public void setmItem(HotelDescriptiveInfo mItem) {
        this.mItem = mItem;
    }*/


    //private HotelDescriptiveInfo mItem= null;
    private List<AvailabilityResult> room_availabilities;
    private String currentHotelId;
    private Context context;
    private static final String DESCRIPTIVEINFO_ALL_HOTELS_MAP = "descriptive_info_all_hotels";

    private static final String ALL_AVAILABILITIES_MAP = "availabilities_all_hotels";

    RoomTypesModel(Context context, String id) {
        this.context = context;
        room_availabilities= new ArrayList<>();
        currentHotelId= id;

        //getHotelDescriptiveData(id);
        retrieveAvailabilities(id);

    }

   /* private void retrieveAvailabilityResults() {
        //read from internal storage
        Map<String, AvailabilityResults> results;
        try{
            results = ( Map<String, AvailabilityResults> )InternalStorage.readObject(context,"availabilities_all_hotels");
            Log.d(TAG,"reading current hotel availabilities from internal storage completed");
            AvailabilityResults availabilities = results.get(currentHotelId);
            updateRoomAvailabilititesBasedOnResults(availabilities);

        }catch (IOException ex){
            Log.e(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+ex.getMessage());
            Log.d(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+ex.getMessage());
        }catch(ClassNotFoundException e){
            Log.e(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+e.getMessage());
            Log.d(TAG, "retrieveHotelDescriptiveData()- exception while reading data from local storage"+e.getMessage());
        }


    }*/

    private void updateRoomAvailabilititesBasedOnResults(AvailabilityResults availabilities) {

        if(availabilities != null){
            List<AvailabilityResult> resultList= availabilities.get(0).getProducts();
            for(int i=0; i < resultList.size(); i++){
                AvailabilityResult aRoomType= resultList.get(i);
                room_availabilities.add(aRoomType);
            }
        }
    }

    List<AvailabilityResult> getAvailabilityResults(){
        return Collections.unmodifiableList(room_availabilities);
    }

/*    void getHotelDescriptiveData(String id) {

        HotelDescriptiveInfoService serviceInfo= new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
            @Override
            public void success(Response<HotelDescriptiveInfo> response) {
                 HotelDescriptiveInfo hotelItem = response.body();
                 setmItem(hotelItem);
                 Log.d(TAG, "retrieved successfully hotel descriptive info ");
            }

            @Override
            public void failed(String message) {
                Log.d(TAG, "failed retrieving HotelDescriptiveData)");
            }
        });
        serviceInfo.getHotelDescriptiveInfo("en", currentHotelId);
    }*/

    private void retrieveAllHotelsAvailabilities(){
        final Map<String, AvailabilityResults> room_av = new HashMap<>();
        Log.d(TAG, "start retrieveAllHotelsAvailabilities() - fetching data from the server");
        for (final String eachId : WelcomeViewModel.ALL_IDS) {

            Log.d(TAG, "start retrieveHotelAvailabilities - fetching data from the server");
            HotelAvailabilityResultsService service = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {
                @Override
                public void success(Response<AvailabilityResults> response) {
                    AvailabilityResults results = response.body();
                    room_av.put(eachId, results);
                }

                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch availability results" + message);
                }
            });
            service.getRoomAvailabilitiesInHotel(eachId, 1, 0, 0);
        }
        Log.d(TAG, "retrieveHotelDescriptiveData()- fetching data from the server - completed");
        /*//writing to internal storage
        try{
            InternalStorage.writeObject(context, ALL_AVAILABILITIES_MAP, room_availabilities);
            Log.d(TAG, "writing all hotels' availabilities  to internal storage completed");
        }catch (IOException ex){
            Log.d(TAG, ex.getMessage());
            Log.e(TAG, ex.getMessage());
        }*/
    }

    private void retrieveAvailabilities(final String id){

        Log.d(TAG, "start retrieveAllHotelsAvailabilities() for hotel "+id+" - fetching data from the server");

            HotelAvailabilityResultsService service = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {
                @Override
                public void success(Response<AvailabilityResults> response) {
                    AvailabilityResults results = response.body();
                    updateRoomAvailabilititesBasedOnResults(results);
                }

                @Override
                public void failed(String message) {
                    Log.d(TAG, "couldn't fetch availability results" + message);
                }
            });
            service.getRoomAvailabilitiesInHotel(id, 1, 0, 0);

        Log.d(TAG, "retrieveHotelDescriptiveData()- fetching data from the server for hotel \"+id+\" - completed");
        //writing to internal storage
        /*try{
            InternalStorage.writeObject(context, ALL_AVAILABILITIES_MAP, room_availabilities);
            Log.d(TAG, "writing all hotels' availabilities  to internal storage completed");
        }catch (IOException ex){
            Log.d(TAG, ex.getMessage());
            Log.e(TAG, ex.getMessage());
        }*/
    }
}
