package ch.fhnw.bedwaste.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotelAvailabilityResultsService {

    private HotelAvailabilityResultsInterface jsonAPI;
    private static final String USER_ID = "test";
    private String errorCode = null;
    private AvailabilityResultsListener listener;
    private static final String TAG="AvailabilityResultsServ";


    public HotelAvailabilityResultsService(AvailabilityResultsListener listener){
        this.listener= listener;
    }

    public void getRoomAvailabilitiesInHotel( String hotelId, int nbAdults, int nbChildren, int nbInfants){
        jsonAPI = APIClient.getClient().create(HotelAvailabilityResultsInterface.class);


        /*Call<AvailabilityResults> callApi = jsonAPI.getHotelAvailabilities(hotelId, USER_ID,
                today, tomorrow , nbAdults, nbChildren, nbInfants);*/
        //the date doesn't matter at least for now
        Call<AvailabilityResults> callApi = jsonAPI.getHotelAvailabilities(hotelId, USER_ID,
                "2019-01-01", "2019-01-01" , nbAdults, nbChildren, nbInfants);
        callApi.enqueue(new Callback<AvailabilityResults>() {
            @Override
            public void onResponse(Call<AvailabilityResults> call, Response<AvailabilityResults> response) {
                if(response.isSuccessful()) {
                   Log.d("TAG",response.code()+"");
                   listener.success(response);
                } else {
                   // errorCode = response.errorBody().toString();
                    APIError error = APIErrorUtils.parseError(response);
                    listener.failed(response.code()+"Error message: "+error.message);
                    Log.d("TAG","Error statusCode: "+ response.code()+"Error message: "+error.message);
                    return;
                }
            }

            @Override
            public void onFailure(Call<AvailabilityResults> call, Throwable t) {
                listener.failed("message error: " +t.getMessage());
                call.cancel();
            }
        });
    }



}
