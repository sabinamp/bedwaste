package ch.fhnw.bedwaste.client;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotelDescriptiveInfoAPI {
    @GET("descriptive-info/{lang}/{hotelId}")
    Call<HotelDescriptiveInfo> getDescriptiveInfo(@Path("lang") String lang, @Path("hotelId") String hotelId);
}
