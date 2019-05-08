package ch.fhnw.bedwaste.server;

import java.util.HashMap;
import java.util.Map;

import ch.fhnw.bedwaste.model.AvailabilityResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AvailabilitiesPerRegionInterface {
    @GET("availabilities/")
    Call<Map<String, AvailabilityResult>> getHotelAvailabilitiesPerRegion(@Query("region") String hotelId, @Query("sessionId") String sessionId,
                                                                          @Query ("start") String startDate, @Query("end") String endDate,
                                                                          @Query("nbAdults") int nbAdults, @Query("nbChildren") int nbChildren,
                                                                          @Query("nbInfants") int nbInfants,
                                                                          @Query("nbInfants") int maxprice, @Query("nbInfants") int nbrooms);
}
