package ch.fhnw.bedwaste;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.client.HotelDTO;

public class HotelListModel {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="HotelListModel";
    private List<HotelDTO> items= null;
    public HotelListModel(){
        items= new ArrayList<>();
        //adding hardcoded hotels to the list
        HotelDTO hHelmhaus = new HotelDTO( "00U5846j022d292h","Hotel Helmhaus");
        hHelmhaus.setRating(9);

        items.add(hHelmhaus);
        HotelDTO hHottingen = new HotelDTO( "00I5846a022h291r","Hotel Hottingen");
        hHottingen.setRating(8);
        items.add(hHottingen);
        HotelDTO hPlatzhirsch = new HotelDTO("00U5846f022c291a","Hotel Platzhirsch");
        hPlatzhirsch.setRating(7);
        items.add(hPlatzhirsch);
        HotelDTO hVillette= new HotelDTO( "00U5846j022d291s","Hotel Villette");
        hVillette.setRating(9);
        hVillette.setNbStars(3);
        items.add(hVillette);
    }
    public List<HotelDTO> getItems() {
        return items;
    }



    /*public static HotelDTO fetchHotel(String hotelId, AppCompatActivity activity){
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
            HotelDTO fetchedHotel = null;
            Log.d(TAG, "the hotelAddress textView content is empty");
            if(hotelInfo !=null){

                fetchedHotel= new HotelDTO(hotelId, hotelInfo.getHotelName());
                fetchedHotel.setAddress(hotelInfo.getContactInfos().get(0).getAddresses().get(0));
                fetchedHotel.setPhone(hotelInfo.getContactInfos().get(0).getPhones().get(0));
                Log.d(TAG, "HotelInfoActivity Activity - fetched data from the server");

            }
            return fetchedHotel;
        }

    }*/
    public HotelDTO return_hotel(String hotelname){

        int list_length = items.size();
        HotelDTO hotel = new HotelDTO("123", "HOTEL");

        return hotel;
    }


}
