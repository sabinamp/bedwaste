package ch.fhnw.bedwaste.server;

import java.util.List;

import ch.fhnw.bedwaste.model.AvailabilityResult;

import retrofit2.Response;

interface AvailabilitiesPerRegionListener {
    public void success(Response<List<AvailabilityResult>> response);
    public void failed(String message);
}
