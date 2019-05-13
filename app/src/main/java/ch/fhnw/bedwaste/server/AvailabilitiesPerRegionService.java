package ch.fhnw.bedwaste.server;

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
    private List<AvailabilityResult> availabilitiesPerRegionResponse= null;

    public void start(String region, int nbAdults, int nbChildren, int nbInfants, int maxprice, int nbrooms,boolean breakfast,boolean wifi){

        jsonAPI = APIClient.getClient().create(AvailabilitiesPerRegionInterface.class);


/*
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd") ;
        String today = format.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrow = format.format(calendar.getTime());
        //format the dates in the required format expected by Hotel Spider CRS API*/

        Call<List<AvailabilityResult>> callApi = jsonAPI.getHotelAvailabilitiesPerRegion(region, USER_ID,
                "2019-05-05", "2019-05-05", nbAdults, nbChildren, nbInfants, maxprice,nbrooms, breakfast, wifi);
        System.out.println("Before enque");
        callApi.enqueue(new Callback<List< AvailabilityResult>>() {

            @Override
            public void onResponse(Call<List<AvailabilityResult>> call, Response<List<AvailabilityResult>> response) {
                System.out.println("inside Enque");
                if(response.isSuccessful()){
                    ;
                    availabilitiesPerRegionResponse = response.body();
                    System.out.println(response.body().toString());


                } else {

                    errorCode ="Code"+ response.code();

                    System.out.println(errorCode);
                    return;

                }
            }

            @Override
            public void onFailure(Call<List<AvailabilityResult>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Failure");
            }
        });


    }




    public String getErrorCode() {
        return errorCode;
    }
    public List<AvailabilityResult> getAvailabilitiesResponse() {
        return availabilitiesPerRegionResponse;
    }
}
