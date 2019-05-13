package ch.fhnw.bedwaste.server;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import retrofit2.Response;

public interface HotelDescriptiveInfoListener {
    public void success(Response<HotelDescriptiveInfo> response);
    public void failed(String message);
}
