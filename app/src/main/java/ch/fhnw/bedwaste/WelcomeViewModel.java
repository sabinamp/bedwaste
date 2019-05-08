package ch.fhnw.bedwaste;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.client.AvailabilityDTO;
import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.CurrencyCode;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.HotelInfoPosition;
import ch.fhnw.bedwaste.server.AvailabilitiesPerRegionService;
import ch.fhnw.bedwaste.server.ErrorsHandler;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;

public class WelcomeViewModel extends ViewModel {
    static final LatLng HOTTINGEN= new LatLng(47.3697905658882, 8.55352004819906);
    static final LatLng PLATZHIRSCH = new LatLng(47.3735057616661, 8.5440319776535);
    static final LatLng HEMLHAUS= new LatLng(47.369158397978, 8.54404538869858);
    static final LatLng VILLETTE= new LatLng(47.3682, 8.5453);

    private List<AvailabilityDTO> availabilityDTOList=null;
    private AvailabilitiesPerRegionService availabilitiesPerRegionService;
    private Map<String,AvailabilityResult> availabilitiesPerRegion;
    private HotelDescriptiveInfoService descriptiveInfoService;
    private List<String> hotelIdsInRegion=null;


    public WelcomeViewModel(){
        availabilitiesPerRegion = new HashMap<>();
        availabilityDTOList = new ArrayList<>();
        availabilitiesPerRegionService = new AvailabilitiesPerRegionService();
        descriptiveInfoService = new HotelDescriptiveInfoService();
        hotelIdsInRegion = new ArrayList<>();

    }

    public List<AvailabilityDTO> getAvailableRoomsInRegion(String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms){
        availabilitiesPerRegionService.start(region,nbAdults, nbChildren, nbInfants, maxprice, nbrooms);
        availabilitiesPerRegion = availabilitiesPerRegionService.getAvailabilitiesResponse();

        for (Map.Entry<String,AvailabilityResult> each : availabilitiesPerRegion.entrySet() ) {
            AvailabilityResult resultAv = each.getValue();
            String[] rateplanIdElem= resultAv.getRateplanId().split("-");
            availabilityDTOList.add(new AvailabilityDTO(each.getKey(), rateplanIdElem[0], resultAv ));
            hotelIdsInRegion.add(each.getKey());
        }
        return availabilityDTOList;
    }

    public List<HotelDescriptiveInfo> getHotelDescriptiveInfo_HotelsPerRegion(String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms) {
        List<HotelDescriptiveInfo> hotelDescriptiveInfoList=new ArrayList<>();
        descriptiveInfoService.start();
        for (String hotelId: hotelIdsInRegion ) {
            descriptiveInfoService.fetchDescriptiveInfo("en",hotelId);
            hotelDescriptiveInfoList.add(descriptiveInfoService.getHotelinfo());
        }
        return hotelDescriptiveInfoList;

    }
    private List<String> getHotelIdsInRegion(){
        return hotelIdsInRegion;
    }
}
