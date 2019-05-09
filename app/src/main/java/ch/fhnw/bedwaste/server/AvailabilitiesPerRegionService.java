package ch.fhnw.bedwaste.server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    static final String BASE_URL = "http://86.119.40.244:8888";
    private AvailabilitiesPerRegionInterface jsonAPI;
    private static final String USER_ID = "test";
    private String errorCode = null;
    private Map<String,AvailabilityResult> availabilitiesPerRegionResponse= null;

    public void start(String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms,boolean breakfast,boolean wifi){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = retrofitBuilder.build();
        jsonAPI = retrofit.create(AvailabilitiesPerRegionInterface.class);
        fetchAvailabilitiesPerRegion(region, nbAdults, nbChildren, nbInfants, maxprice, nbrooms, breakfast, wifi);

    }
    private void fetchAvailabilitiesPerRegion( String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms, boolean breakfast, boolean wifi){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd") ;
        String today = format.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrow = format.format(calendar.getTime());
        //format the dates in the required format expected by Hotel Spider CRS API

        Call<Map<String,AvailabilityResult>> callApi = jsonAPI.getHotelAvailabilitiesPerRegion(region, USER_ID,
                today, tomorrow , nbAdults, nbChildren, nbInfants, maxprice,nbrooms, breakfast, wifi);
        System.out.println(callApi.request());
        callApi.enqueue(new Callback<Map<String, AvailabilityResult>>() {
            @Override
            public void onResponse(Call<Map<String, AvailabilityResult>> call, Response<Map<String, AvailabilityResult>> response) {
                if(response.isSuccessful()){
                    availabilitiesPerRegionResponse = response.body();
                    System.out.println(response.body().toString());


                } else {

                    errorCode ="Code"+ response.code();

                    System.out.println(errorCode);
                    return;

                }
            }

            @Override
            public void onFailure(Call<Map<String, AvailabilityResult>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Failure");
            }
        });


    }




    public String getErrorCode() {
        return errorCode;
    }
    public Map<String, AvailabilityResult> getAvailabilitiesResponse() {
        return availabilitiesPerRegionResponse;
    }
}
