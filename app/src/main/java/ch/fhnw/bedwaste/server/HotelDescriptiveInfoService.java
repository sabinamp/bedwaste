package ch.fhnw.bedwaste.server;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.server.HotelDescriptiveInfoInterface;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotelDescriptiveInfoService implements Callback<HotelDescriptiveInfo> {
    static final String BASE_URL = "http://192.168.0.108:8080/";
    private HotelDescriptiveInfoInterface jsonDescriptiveInfoAPI;


    private String errorCode = null;
    private HotelDescriptiveInfo hotelinfo = null;

    public void start(String lang, String hotelId){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = retrofitBuilder.build();
        jsonDescriptiveInfoAPI = retrofit.create(HotelDescriptiveInfoInterface.class);
        getDescriptiveInfo(lang, hotelId);

    }
    private void getDescriptiveInfo(String lang, String hotelId){
        Call<HotelDescriptiveInfo> callApi = jsonDescriptiveInfoAPI.getDescriptiveInfo(lang, hotelId);
        callApi.enqueue(this);
    }


    @Override
    public void onResponse(Call<HotelDescriptiveInfo> call, Response<HotelDescriptiveInfo> response) {
        if(response.isSuccessful()) {
             hotelinfo = response.body();
        } else {
            errorCode = response.errorBody().toString();
        }
    }

    @Override
    public void onFailure(Call<HotelDescriptiveInfo> call, Throwable t) {
        t.printStackTrace();
    }
    public HotelDescriptiveInfo getHotelinfo() {

        return hotelinfo;
    }
    public String getErrorCode() {
        return errorCode;
    }
}
