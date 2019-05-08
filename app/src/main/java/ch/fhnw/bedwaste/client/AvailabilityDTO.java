package ch.fhnw.bedwaste.client;

import ch.fhnw.bedwaste.model.AvailabilityResult;

public class AvailabilityDTO {
    public AvailabilityDTO(String name, String hotelId, AvailabilityResult availableRoom) {
        this.name = name;
        this.hotelId = hotelId;
        this.availableRoom = availableRoom;
    }

    private String name;

    private String hotelId;

    private AvailabilityResult availableRoom;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
