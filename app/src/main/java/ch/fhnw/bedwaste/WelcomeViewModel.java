package ch.fhnw.bedwaste;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class WelcomeViewModel extends ViewModel {
    static final LatLng mDefaultLocation = new LatLng(47.3769, 8.5417);
   /* static final LatLng HOTTINGEN= new LatLng(47.3697905658882, 8.55352004819906);
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
    static final LatLng ROTESHAUS= new LatLng(47.484363, 8.206767);*/



    private Map<String,AvailabilityResult> availabilitiesPerRegion;
    public static Set<String> ALL_IDS= null;
   
    public static Map<LatLng, String > HotelIdBaasedOnPosition= null;
    static  {
        HotelIdBaasedOnPosition = new HashMap<>();
        ALL_IDS= new TreeSet<>();
        ALL_IDS.add("00I5846a022h291r");
        ALL_IDS.add("00U5846f022c291a");
        ALL_IDS.add("00U5846j022d292h");
        ALL_IDS.add("00U5846j022d291s");
        ALL_IDS.add("00B5846B02barlac");
        ALL_IDS.add("00U5846j020d210g");
        ALL_IDS.add("00U5845j020s210l");
        ALL_IDS.add("00U5847f022marri");
        ALL_IDS.add("00U5844f022rigib");
        ALL_IDS.add("00B5846t02termin");
        ALL_IDS.add("00U5846e0f2ukulm");
        ALL_IDS.add("00F5846A022nowifi");
        ALL_IDS.add("00F5846A02nowifi2");
        ALL_IDS.add("00G5846t022gotth");
        ALL_IDS.add("00U5845f022gbrugg");
        ALL_IDS.add("00U5846f022marco");
        ALL_IDS.add("00U5556f030plb91");
        ALL_IDS.add("00U5845f022rotesh");
        ALL_IDS.add("00G5846t022easyh");
    }
    /*private Map<String, LatLng> hotelIdsInRegionAargau=null;
    private Map<String, LatLng> hotelIdsInRegionZH=null;*/

    private Map<String, Integer> displayedPrices = null;



    private Map<String, HotelDescriptiveInfo> hotelId_descriptiveInfo;
    private Map<String, AvailabilityResults> hotelId_availabilities;

    public WelcomeViewModel(){
        availabilitiesPerRegion = new HashMap<>();

        hotelId_descriptiveInfo = new HashMap<>();
        hotelId_availabilities = new HashMap<>();
       /* hotelIdsInRegionZH = new HashMap<>();
        hotelIdsInRegionAargau = new HashMap<>();*/
        displayedPrices = new HashMap<>();
    }



    public Map<String, HotelDescriptiveInfo> getHotelId_descriptiveInfo() {
        return hotelId_descriptiveInfo;
    }

    public void updateHotelId_descriptiveInfo(String id, HotelDescriptiveInfo hotelId_descriptiveInfo) {
        this.hotelId_descriptiveInfo.put(id, hotelId_descriptiveInfo);
    }

    public Map<String, AvailabilityResults> getHotelId_availabilities() {
        return Collections.unmodifiableMap(hotelId_availabilities);
    }

    public void updateHotelId_availabilities(String id, AvailabilityResults hotelId_availabilities) {
        this.hotelId_availabilities.put(id, hotelId_availabilities);
    }
    public void updateDisplayedPrices(String id, Integer price) {
        this.displayedPrices.put(id, price);
    }
    public Map<String, Integer> getDisplayedPrices() {
        return displayedPrices;
    }

    //return the distance in Km
   public static Double getDistanceBetween(LatLng hotelLoc, LatLng userLoc) {
        if (hotelLoc == null || userLoc == null){
            return null;
        }
        return (double)(SphericalUtil.computeDistanceBetween(hotelLoc,userLoc)/1000) ;
    }

    public static String getDistanceAsStringBetween(LatLng hotelLoc, LatLng userLoc) {
        if (hotelLoc == null || userLoc == null){
            return null;
        }
        double input=(double)(SphericalUtil.computeDistanceBetween(hotelLoc,userLoc)/1000) ;
        DecimalFormat df2 = new DecimalFormat("#.##");
        df2.setRoundingMode(RoundingMode.DOWN);
        String distKmAsString= df2.format(input) +" Km";
        return distKmAsString;
    }

    public static double getDistanceInHours(double distanceKm){
        double distanceInHours= (distanceKm*12.6)/60;
        return distanceInHours;
    }

    public static String getDistanceInHoursasString(double distanceKm){
        double distanceInHours= (distanceKm*13)/60;
        DecimalFormat df2 = new DecimalFormat("#.##");
        df2.setRoundingMode(RoundingMode.DOWN);
        String distMinAsString= df2.format(distanceInHours);
        return distMinAsString+ " Hours";
    }
}
