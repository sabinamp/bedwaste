package ch.fhnw.bedwaste.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotelDescriptiveInfoController implements Callback<HotelDescriptiveInfo> {
    static final String BASE_URL = "https://localhost:8080/";
    private HotelDescriptiveInfoAPI jsonDescriptiveInfoAPI;


    private String errorCode = null;
    private HotelDescriptiveInfo hotelinfo = null;

    public void start(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

         jsonDescriptiveInfoAPI = retrofit.create(HotelDescriptiveInfoAPI.class);


    }
    public void getDescriptiveInfo(String lang, String hotelId){
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
