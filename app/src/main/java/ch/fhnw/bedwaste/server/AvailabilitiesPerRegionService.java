package ch.fhnw.bedwaste.server;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.AvailabilityResults;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvailabilitiesPerRegionService {

    private AvailabilitiesPerRegionInterface jsonAPI;
    private static final String USER_ID = "test";
    private String errorCode = null;
    private AvailabilitiesPerRegionListener listener;
    public AvailabilitiesPerRegionService(AvailabilitiesPerRegionListener listener){
        this.listener= listener;
    }

    public void getAvailabilitiesPerRegion(final String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms, Boolean breakfast, Boolean wifi){

        //format the dates in the required format expected by Hotel Spider CRS API*/
        jsonAPI = APIClient.getClient().create(AvailabilitiesPerRegionInterface.class);
        Call<List<AvailabilityResult>> callApi = jsonAPI.getHotelAvailabilitiesPerRegion(region, USER_ID,
                "2019-05-05", "2019-05-05", nbAdults, nbChildren, nbInfants, maxprice,nbrooms, breakfast, wifi);
        Log.d("TAG", "Before enqueing"+"getAvailabilittiesInRegion "+region+" nbAdults = "+nbAdults);
        callApi.enqueue(new Callback<List< AvailabilityResult>>() {

            @Override
            public void onResponse(Call<List<AvailabilityResult>> call, Response<List<AvailabilityResult>> response) {

                if(response.isSuccessful()){

                    Log.d("TAG",response.code()+"");
                    listener.success(response);

                } else {
                    errorCode =""+ response.code();
                    Log.d("TAG","Error code: "+errorCode);
                    return;

                }
            }

            @Override
            public void onFailure(Call<List<AvailabilityResult>> call, Throwable t) {
                Log.d("TAG", "failed retrieving availability results per region "+region);
                listener.failed("message error: " +t.getMessage());
                call.cancel();
            }
        });


    }


}
