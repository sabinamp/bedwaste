package ch.fhnw.bedwaste.server;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import retrofit2.Response;

public interface AvailabilityResultsListener {
    public void success(Response<AvailabilityResults> response);
    public void failed(String message);
}
