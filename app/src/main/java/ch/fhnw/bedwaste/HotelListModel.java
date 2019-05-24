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

    HotelListModel(Context context, List<String> ids, List<HotelDescriptiveInfo> data) {
        this.context=context;
        hotelId_descriptiveInfo = new HashMap<>();
        mItems = data;

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





}
