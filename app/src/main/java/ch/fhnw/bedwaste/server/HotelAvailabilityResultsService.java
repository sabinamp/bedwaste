package ch.fhnw.bedwaste.server;

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

public class HotelAvailabilityResultsService implements Callback<AvailabilityResults> {
    static final String BASE_URL = "http://86.119.40.244:8888";
    private HotelAvailabilityResultsInterface jsonAPI;
    private static final String USER_ID = "test";
    private String errorCode = null;


    public AvailabilityResults getAvailabilitiesResponse() {
        return availabilitiesResponse;
    }

    private AvailabilityResults availabilitiesResponse= null;

    public void start(String hotelId, int nbAdults, int nbChildren, int nbInfants){

        jsonAPI = APIClient.getClient().create(HotelAvailabilityResultsInterface.class);
        getAvailabilities(hotelId, nbAdults, nbChildren, nbInfants);

    }

    private void getAvailabilities( String hotelId, int nbAdults, int nbChildren, int nbInfants){
         Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(calendar);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrow = format.format(calendar);
        //format the dates in the required format expected by Hotel Spider CRS API

        Call<AvailabilityResults> callApi = jsonAPI.getHotelAvailabilities(hotelId, USER_ID,
                today, tomorrow , nbAdults, nbChildren, nbInfants);
        callApi.enqueue(this);
    }


    @Override
    public void onResponse(Call<AvailabilityResults> call, Response<AvailabilityResults> response) {
        if(response.isSuccessful()) {
            availabilitiesResponse = response.body();
        } else {
            errorCode = response.errorBody().toString();
        }
    }

    @Override
    public void onFailure(Call<AvailabilityResults> call, Throwable t) {
        t.printStackTrace();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
