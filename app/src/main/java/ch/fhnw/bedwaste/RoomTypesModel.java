package ch.fhnw.bedwaste;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.RoomAvailabilityResult;
import ch.fhnw.bedwaste.server.AvailabilityResultsListener;
import ch.fhnw.bedwaste.server.HotelAvailabilityResultsService;
import retrofit2.Response;

public class RoomTypesModel {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="HotelListModel";
    /**
     * List of hotel ids passed from WelcomeActivity
     */
    private HotelDescriptiveInfo mItem= null;
    private List<AvailabilityResult> room_availabilities;
    private String currentHotelId;

    RoomTypesModel(Context context, String id) {
        mItem = getHotelDescriptiveData(id);
        room_availabilities= new ArrayList<>();
        currentHotelId= id;
        retrieveAvailabilityResults();

    }

    void retrieveAvailabilityResults() {
        HotelAvailabilityResultsService service = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {
            @Override
            public void success(Response<AvailabilityResults> response) {
                AvailabilityResults results = response.body();
                List<AvailabilityResult> resultList= results.get(0).getProducts();
                for(int i=0; i < resultList.size(); i++){
                    AvailabilityResult aRoomType= resultList.get(i);
                    room_availabilities.add(aRoomType);
                }

            }

            @Override
            public void failed(String message) {
                Log.d(TAG, "couldn't fetch availability results" + message);
            }
        });
        service.getRoomAvailabilitiesInHotel(currentHotelId, 2, 0, 0);

    }
    List<AvailabilityResult> getAvailabilityResults(){
        return Collections.unmodifiableList(room_availabilities);
    }
    private HotelDescriptiveInfo getHotelDescriptiveData(String id) {
        HotelDescriptiveInfo hotelItem=null;

        return hotelItem;
    }
}
