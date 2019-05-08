package ch.fhnw.bedwaste.client;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.bedwaste.model.Address;
import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.CurrencyCode;
import ch.fhnw.bedwaste.model.GuestRoom;
import ch.fhnw.bedwaste.model.HotelInfoPosition;
import ch.fhnw.bedwaste.model.Phone;

public class HotelDTO {
    private String name;
    private Address address;
    private String hotelId;
    private int distanceUnitOfMeasureCode;
    private String currencyCode;
    private Phone phone;
    private String email;
    private int rating;
    private String description;
    private HotelInfoPosition position;
    private List<GuestRoom> rooms;
    private List<AvailabilityResult> availabilities;
    private int nbStars;


    public HotelDTO(String hotelId, String hotelName){
        this.hotelId=hotelId;
        currencyCode = "CHF";
        phone=null;
        rooms= new ArrayList<>();
        availabilities = new ArrayList<>();
        name= hotelName;
        nbStars=2;
        rating=2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public int getDistanceUnitOfMeasureCode() {
        return distanceUnitOfMeasureCode;
    }

    public void setDistanceUnitOfMeasureCode(int distanceUnitOfMeasureCode) {
        this.distanceUnitOfMeasureCode = distanceUnitOfMeasureCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {

        if(rating <= 10){
            this.rating = rating;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HotelInfoPosition getPosition() {
        return position;
    }

    public void setPosition(HotelInfoPosition position) {
        this.position = position;
    }


    public Phone getPhone() {
        return phone;
    }
    public List<GuestRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<GuestRoom> rooms) {
        this.rooms = rooms;
    }

    public List<AvailabilityResult> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<AvailabilityResult> availabilities) {
        this.availabilities = availabilities;
    }

    public int getWalkingDistanceToHotelInMinutes(LatLng userLocation) {
      /*  double hotelAltitude = this.getPosition().getAltitude().doubleValue();
        double hotelLongitude = this.getPosition().getLongitude().doubleValue();
        LatLng hotelLocation = new LatLng(hotelAltitude, hotelLongitude) ;*/
        //todo calculate the distance from the current userLocation to the hotel hotelLocation
        return 3;
    }
    public int getNbStars() {
        return nbStars;
    }

    public void setNbStars(int nbStars) {
        this.nbStars = nbStars;
    }

}
