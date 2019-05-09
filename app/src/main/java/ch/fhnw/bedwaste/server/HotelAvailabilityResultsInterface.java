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
    Call<AvailabilityResults> getHotelAvailabilities(@Path("hotelID") String hotelId, @Field("sessionId") String sessionId,
                                                     @Field ("start") String startDate, @Field("end") String endDate,
                                                     @Field("nbAdults") int nbAdults, @Field("nbChildren") int nbChildren,
                                                     @Field("nbInfants") int nbInfants);


}
