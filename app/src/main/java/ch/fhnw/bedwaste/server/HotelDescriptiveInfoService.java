package ch.fhnw.bedwaste.server;

import java.util.concurrent.TimeUnit;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HotelDescriptiveInfoService implements Callback<HotelDescriptiveInfo> {
    static final String BASE_URL = "http://86.119.40.244:8888/";
    private HotelDescriptiveInfoInterface jsonDescriptiveInfoAPI;
    public static final int STATUS_OK = 200;
    public static final int STATUS_SERVER_ERROR = 500;
    public static final int TIMEOUT = 15;

    private String errorCode = null;
    private HotelDescriptiveInfo hotelinfo = null;
    private FetchDataError descriptiveInfoError= null;
    public void start( FetchDataError descriptionError){
        LoggingInterceptor logging = new LoggingInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

        Retrofit retrofit = retrofitBuilder.build();
        jsonDescriptiveInfoAPI = retrofit.create(HotelDescriptiveInfoInterface.class);
        descriptiveInfoError = descriptionError;

    }
    public void fetchDescriptiveInfo(String lang, String hotelId){
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
