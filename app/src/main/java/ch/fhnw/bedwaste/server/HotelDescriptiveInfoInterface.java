package ch.fhnw.bedwaste.server;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HotelDescriptiveInfoInterface {
    @GET("descriptive-info/{lang}/{hotelId}")
    Call<HotelDescriptiveInfo> getDescriptiveInfo(@Path("lang") String lang, @Path("hotelId") String hotelId);


}
