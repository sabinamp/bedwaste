package ch.fhnw.bedwaste;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.client.HotelItem;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.server.ErrorsHandler;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoService;

public class HotelListModel {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="HotelListModel";
    private List<HotelItem> items= null;
    public HotelListModel(){
        items= new ArrayList<>();
        //adding hardcoded hotels to the list
        HotelItem hHelmhaus = new HotelItem( "00U5846j022d292h","Hotel Helmhaus");
        hHelmhaus.setRating(9);

        items.add(hHelmhaus);
        HotelItem hHottingen = new HotelItem( "00I5846a022h291r","Hotel Hottingen");
        hHottingen.setRating(8);
        items.add(hHottingen);
        HotelItem hPlatzhirsch = new HotelItem("00U5846f022c291a","Hotel Platzhirsch");
        hPlatzhirsch.setRating(7);
        items.add(hPlatzhirsch);
        HotelItem hVillette= new HotelItem( "00U5846j022d291s","Hotel Villette");
        hVillette.setRating(9);
        hVillette.setNbStars(3);
        items.add(hVillette);
    }
    public List<HotelItem> getItems() {
        return items;
    }



    /*public static HotelItem fetchHotel(String hotelId, AppCompatActivity activity){
        HotelDescriptiveInfoService service = new HotelDescriptiveInfoService();
        Log.d(TAG, "start fetching data from the server");
        service.start( new ErrorsHandler(activity));
        service.fetchDescriptiveInfo("en",hotelId);

        String error = service.getErrorCode();
        if(!(*//*error.isEmpty() ||*//* error == null)){
            Log.d(TAG, "HotelInfoActivity Activity - could not fetch data from the server.Error code:"+ service.getErrorCode());
            return null;
        }  else{
            HotelDescriptiveInfo hotelInfo = service.getHotelinfo();
            HotelItem fetchedHotel = null;
            Log.d(TAG, "the hotelAddress textView content is empty");
            if(hotelInfo !=null){

                fetchedHotel= new HotelItem(hotelId, hotelInfo.getHotelName());
                fetchedHotel.setAddress(hotelInfo.getContactInfos().get(0).getAddresses().get(0));
                fetchedHotel.setPhone(hotelInfo.getContactInfos().get(0).getPhones().get(0));
                Log.d(TAG, "HotelInfoActivity Activity - fetched data from the server");

            }
            return fetchedHotel;
        }

    }*/
    public HotelItem return_hotel(String hotelname){

        int list_length = items.size();
        HotelItem hotel = new HotelItem("123", "HOTEL");

        return hotel;
    }


}
