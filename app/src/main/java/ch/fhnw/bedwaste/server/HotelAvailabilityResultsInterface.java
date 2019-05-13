package ch.fhnw.bedwaste.server;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HotelAvailabilityResultsInterface {
    @Headers("Accept: application/json")
    @GET("availabilities/{hotelId}")
    Call<AvailabilityResults> getHotelAvailabilities(@Path("hotelId") String hotelId, @Query("sessionId") String sessionId,
                                                     @Query ("start") String startDate, @Query("end") String endDate,
                                                     @Query("nbAdults") int nbAdults, @Query("nbChildren") int nbChildren,
                                                     @Query("nbInfants") int nbInfants);


}
