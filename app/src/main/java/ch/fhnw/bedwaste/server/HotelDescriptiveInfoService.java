package ch.fhnw.bedwaste.server;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HotelDescriptiveInfoService {

    private HotelDescriptiveInfoInterface jsonDescriptiveInfoAPI;
    public static final int STATUS_OK = 200;
    public static final int STATUS_SERVER_ERROR = 500;
    public static final int TIMEOUT = 15;

    private FetchDataError descriptiveInfoError= null;
    HotelDescriptiveInfoListener listener=null;
    public HotelDescriptiveInfoService( HotelDescriptiveInfoListener listener){
        /*FetchDataError descriptionError*/
        this.listener= listener;

    }
    public  void getHotelDescriptiveInfo(String lang, final String hotelId){
        jsonDescriptiveInfoAPI = APIClient.getClient().create(HotelDescriptiveInfoInterface.class);
        //descriptiveInfoError = descriptionError;
        Call<HotelDescriptiveInfo> callApi = jsonDescriptiveInfoAPI.getDescriptiveInfo(lang, hotelId);
        callApi.enqueue(new Callback<HotelDescriptiveInfo>() {
            @Override
            public void onResponse(Call<HotelDescriptiveInfo> call, Response<HotelDescriptiveInfo> response) {
                if(response.isSuccessful()){
                    Log.d("TAG",response.code()+"");
                    listener.success(response);
                }else{
                    Log.d("TAG",response.code()+"");
                    return;
                }

            }

            @Override
            public void onFailure(Call<HotelDescriptiveInfo> call, Throwable t) {
                listener.failed("message error: " +t.getMessage());
                call.cancel();
            }
        });

    }




}
