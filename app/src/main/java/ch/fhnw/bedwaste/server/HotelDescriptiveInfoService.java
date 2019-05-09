package ch.fhnw.bedwaste.server;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HotelDescriptiveInfoService {
    static final String BASE_URL = "http://86.119.40.244:8888";
    private HotelDescriptiveInfoInterface jsonDescriptiveInfoAPI;
    public static final int STATUS_OK = 200;
    public static final int STATUS_SERVER_ERROR = 500;
    public static final int TIMEOUT = 15;


    private String errorCode = null;
    private HotelDescriptiveInfo hotelinfo = null;
    private FetchDataError descriptiveInfoError= null;
    public void start(/*FetchDataError descriptionError*/){

       // this.context = context;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://86.119.40.244:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonDescriptiveInfoAPI = retrofit.create(HotelDescriptiveInfoInterface.class);
        //descriptiveInfoError = descriptionError;

    }
    public  void fetchDescriptiveInfo(String lang, String hotelId){
        Call<HotelDescriptiveInfo> callApi = jsonDescriptiveInfoAPI.getDescriptiveInfo(lang, hotelId);
        callApi.enqueue(new Callback<HotelDescriptiveInfo>() {
            @Override
            public void onResponse(Call<HotelDescriptiveInfo> call, Response<HotelDescriptiveInfo> response) {
                hotelinfo = response.body();
            }

            @Override
            public void onFailure(Call<HotelDescriptiveInfo> call, Throwable t) {

            }
        });

    }


    public HotelDescriptiveInfo getHotelinfo() {
        return hotelinfo;
    }
    public String getErrorCode() {

        return errorCode;
    }
}
