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

    private List<AvailabilityResult> room_availabilities;

    private Context context;
    private static final String DESCRIPTIVEINFO_ALL_HOTELS_MAP = "descriptive_info_all_hotels";

    private static final String ALL_AVAILABILITIES_MAP = "availabilities_all_hotels";

    RoomTypesModel(Context context, List<AvailabilityResult> availabilityResults) {
        this.context = context;
        room_availabilities= availabilityResults;

    }



 /*   private void updateRoomAvailabilititesBasedOnResults(AvailabilityResults availabilities) {

        if(availabilities != null){
            List<AvailabilityResult> resultList= availabilities.get(0).getProducts();
            for(int i=0; i < resultList.size(); i++){
                AvailabilityResult aRoomType= resultList.get(i);
                room_availabilities.add(aRoomType);
            }
        }
    }*/

    List<AvailabilityResult> getAvailabilityResults(){
        return Collections.unmodifiableList(room_availabilities);
    }






}
