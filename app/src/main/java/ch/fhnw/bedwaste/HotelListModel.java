package ch.fhnw.bedwaste;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.client.HotelItem;

public class HotelListModel {

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

}
