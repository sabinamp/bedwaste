package ch.fhnw.bedwaste;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.fhnw.bedwaste.client.HotelDTO;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

public class HotelListModel {
    /**
     * Debugging tag LoginActivity used by the Android logger.
     */
    private static final String TAG="HotelListModel";

    private List<HotelDTO> items= null;
    public HotelListModel() {
        items = new ArrayList<>();

        //adding hardcoded hotels to the list
        HotelDTO hHelmhaus = new HotelDTO("00U5846j022d292h", "Hotel Helmhaus");
        hHelmhaus.setRating(9);

        items.add(hHelmhaus);
        HotelDTO hHottingen = new HotelDTO("00I5846a022h291r", "Hotel Hottingen");
        hHottingen.setRating(8);
        items.add(hHottingen);
        HotelDTO hPlatzhirsch = new HotelDTO("00U5846f022c291a", "Hotel Platzhirsch");
        hPlatzhirsch.setRating(7);
        items.add(hPlatzhirsch);
        HotelDTO hVillette = new HotelDTO("00U5846j022d291s", "Hotel Villette");
        hVillette.setRating(9);
        hVillette.setNbStars(3);
        items.add(hVillette);
/*        Set<String> hotelIds= model.getHotelId_descriptiveInfo().keySet();
        for (String id: hotelIds ) {
            HotelDescriptiveInfo hotelDescrInfo= model.getHotelId_descriptiveInfo().get(id);
            if(hotelDescrInfo != null){
                HotelDTO toAdd= new HotelDTO(id, hotelDescrInfo.getHotelName());
                toAdd.setPhone(hotelDescrInfo.getContactInfos().get(0).getPhones().get(0).getPhoneNumber());
                toAdd.setRating(Double.parseDouble(hotelDescrInfo.getAffiliationInfo().getAwards().get(1).getRating()));
                toAdd.setAddress(hotelDescrInfo.getContactInfos().get(0).getAddresses().get(0));
                toAdd.setAvailabilities(model.getHotelId_availabilities().get(id));

                items.add(toAdd);
            }


        }*/
    }


    public List<HotelDTO> getItems() {
        return items;
    }







}
