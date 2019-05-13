package ch.fhnw.bedwaste.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.model.AvailabilityResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AvailabilitiesPerRegionInterface {
    @Headers("Accept: application/json")
    @GET("availabilities/")
    Call<List<AvailabilityResult>> getHotelAvailabilitiesPerRegion(@Field("region") String hotelId, @Field("sessionId") String sessionId,
                                                                   @Field ("start") String startDate, @Field("end") String endDate,
                                                                   @Field("nbAdults") int nbAdults, @Field("nbChildren") int nbChildren,
                                                                   @Field("nbInfants") int nbInfants,
                                                                   @Field("maxprice") int maxprice,
                                                                   @Field("nbrooms") int nbrooms,
                                                                   @Query("breakfast") boolean breakfast,
                                                                   @Query("wifi") boolean wifi);
}
