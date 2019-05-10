package ch.fhnw.bedwaste;
import android.arch.lifecycle.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

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
    static final LatLng BARAULAC= new LatLng(47.367301, 8.539349);
    static final LatLng GOTTHARD= new LatLng(47.480866, 8.207631);
    static final LatLng GLOCKENHOF= new LatLng(47.37290, 8.53652);
    static final LatLng LEONECK= new LatLng(47.379, 8.546);
    static final LatLng MARRIOTT= new LatLng(47.3825796207, 8.54042923447);
    static final LatLng NOWIFIBRUGG= new LatLng(47.483683, 8.207625);
    static final LatLng NOWIFIBRUGG2= new LatLng(47.484220, 8.205203);
    static final LatLng RIGIBLICK= new LatLng(47.388256, 8.553537);
    static final LatLng TERMINUS= new LatLng(47.480889, 8.208165);
    static final LatLng UTOKULM= new LatLng(47.349647, 8.491144);
    static final LatLng JUGENDHERBERGE= new LatLng(47.481790, 8.194111);
    static final LatLng MARCOPOLO= new LatLng(47.485581, 8.208088);
    static final LatLng DESIGNBOUTIQUE= new LatLng(47.480177, 8.247090);
    static final LatLng ROTESHAUS= new LatLng(47.484363, 8.206767);

    private List<AvailabilityDTO> availabilityDTOList=null;
    private AvailabilitiesPerRegionService availabilitiesPerRegionService;
    private Map<String,AvailabilityResult> availabilitiesPerRegion;
    private HotelDescriptiveInfoService descriptiveInfoService;
    public static Map<LatLng, String > hotelIdBaasedOnPosition= null;
    static  {
        hotelIdBaasedOnPosition = new HashMap<>();
        hotelIdBaasedOnPosition.put(HOTTINGEN, "00I5846a022h291r");
        hotelIdBaasedOnPosition.put( PLATZHIRSCH,"00U5846f022c291a");
        hotelIdBaasedOnPosition.put(HEMLHAUS,"00U5846j022d292h");
        hotelIdBaasedOnPosition.put(VILLETTE,"00U5846j022d291s");
        hotelIdBaasedOnPosition.put(BARAULAC,"00B5846B02barlac" );

        hotelIdBaasedOnPosition.put( GLOCKENHOF,"00U5846j020d210g");
        hotelIdBaasedOnPosition.put( LEONECK,"00U5845j020s210l");
        hotelIdBaasedOnPosition.put( MARRIOTT,"00U5847f022marri");
        hotelIdBaasedOnPosition.put( RIGIBLICK,"00U5844f022rigib");
        hotelIdBaasedOnPosition.put( TERMINUS,"00B5846t02termin");
        hotelIdBaasedOnPosition.put( UTOKULM,"00U5846e0f2ukulm");

        hotelIdBaasedOnPosition.put( NOWIFIBRUGG,"00F5846A022nowifi");
        hotelIdBaasedOnPosition.put( NOWIFIBRUGG2,"00F5846A02nowifi2");
        hotelIdBaasedOnPosition.put( GOTTHARD, "00G5846t022gotth");
        hotelIdBaasedOnPosition.put( JUGENDHERBERGE,"00U5845f022gbrugg");
        hotelIdBaasedOnPosition.put( MARCOPOLO, "00U5846f022marco");
        hotelIdBaasedOnPosition.put( DESIGNBOUTIQUE, "00U5556f030plb91");
        hotelIdBaasedOnPosition.put( ROTESHAUS,"00U5845f022rotesh");
    }
    private Map<String, LatLng> hotelIdsInRegionAargau=null;
    private Map<String, LatLng> hotelIdsInRegionZH=null;


    public WelcomeViewModel(){
        availabilitiesPerRegion = new HashMap<>();
        availabilityDTOList = new ArrayList<>();
        availabilitiesPerRegionService = new AvailabilitiesPerRegionService();
        descriptiveInfoService = new HotelDescriptiveInfoService();
        hotelIdsInRegionZH = new HashMap<>();
        hotelIdsInRegionAargau = new HashMap<>();
        hotelIdsInRegionZH.put("00I5846a022h291r",HOTTINGEN);
        hotelIdsInRegionZH.put("00U5846f022c291a", PLATZHIRSCH);
        hotelIdsInRegionZH.put("00U5846j022d292h",HEMLHAUS);
        hotelIdsInRegionZH.put("00U5846j022d291s",VILLETTE);
        hotelIdsInRegionZH.put("00B5846B02barlac",BARAULAC );

        hotelIdsInRegionZH.put("00U5846j020d210g", GLOCKENHOF);
        hotelIdsInRegionZH.put("00U5845j020s210l", LEONECK);
        hotelIdsInRegionZH.put("00U5847f022marri", MARRIOTT);
        hotelIdsInRegionZH.put("00U5844f022rigib", RIGIBLICK);
        hotelIdsInRegionZH.put("00B5846t02termin", TERMINUS);
        hotelIdsInRegionZH.put("00U5846e0f2ukulm", UTOKULM);

        hotelIdsInRegionAargau.put("00F5846A022nowifi", NOWIFIBRUGG);
        hotelIdsInRegionAargau.put("00F5846A02nowifi2", NOWIFIBRUGG2);
        hotelIdsInRegionAargau.put("00G5846t022gotth", GOTTHARD);
        hotelIdsInRegionAargau.put("00U5845f022gbrugg", JUGENDHERBERGE);
        hotelIdsInRegionAargau.put("00U5846f022marco", MARCOPOLO);
        hotelIdsInRegionAargau.put("00U5556f030plb91", DESIGNBOUTIQUE);
        hotelIdsInRegionAargau.put("00U5845f022rotesh", ROTESHAUS);
    }

/*    public List<AvailabilityDTO> getAvailableRoomsInRegion(String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms, Boolean breakfast, Boolean wifi){
        availabilitiesPerRegionService.start(region,nbAdults, nbChildren, nbInfants, maxprice, nbrooms, breakfast, wifi);
        availabilitiesPerRegion = availabilitiesPerRegionService.getAvailabilitiesResponse();
        System.out.println("WelcomeViewModel");
        for (Map.Entry<String,AvailabilityResult> each : availabilitiesPerRegion.entrySet() ) {
            AvailabilityResult resultAv = each.getValue();
            System.out.println("WelcomeViewModel-inloop");
            String[] rateplanIdElem= resultAv.getRateplanId().split("-");
            availabilityDTOList.add(new AvailabilityDTO(each.getKey(), rateplanIdElem[0], resultAv ));
            hotelIdsInRegion.add(each.getKey());
        }
        return availabilityDTOList;
    }

    */


    public Map<String, LatLng> getHotelIdsInRegion(String reg) {
        if (reg.equals("ZH")) {
            return hotelIdsInRegionZH;
        }
        else if(reg.equals("Brugg")){
            return hotelIdsInRegionZH;
        }
       return null;
    }

}
