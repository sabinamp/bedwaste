package ch.fhnw.bedwaste.server;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HotelAvailabilityResultsServiceTest {
    private CountDownLatch responseLatch;
    private AvailabilityResults hotelAvailabilityResults;
    static final String BASE_URL = "http://86.119.40.244:8888";
    private AvailabilityResults fetchedData;

    @Before
    public void setUp() {
        responseLatch=new CountDownLatch(1);
    }

    @Test(timeout=30000)
    public void fetchAvailabilityInfo1Adult() throws InterruptedException {
        String hotelHelmhausId = "00U5846j022d292h";
        HotelAvailabilityResultsInterface jsonAPI = APIClient.getClient().create(HotelAvailabilityResultsInterface.class);
        Call<AvailabilityResults> callApi = jsonAPI.getHotelAvailabilities(hotelHelmhausId, "test",
                "2019-01-01", "2019-01-01" , 1, 0, 0);

        callApi.enqueue(new Callback<AvailabilityResults>() {
            @Override
            public void onResponse(Call<AvailabilityResults> call, Response<AvailabilityResults> response) {
                if(response.isSuccessful()) {
                    //Log.d("TAG",response.statusCode()+"");
                    System.out.println(response.code()+"");
                    fetchedData= response.body();
                    responseLatch.countDown();

                } else {
                    String errorCode = response.errorBody().toString();
                    //Log.d("TAG",response.statusCode()+errorCode);
                    System.out.println(response.code()+errorCode);
                    return;
                }
            }

            @Override
            public void onFailure(Call<AvailabilityResults> call, Throwable t) {
                System.out.println("message error: " +t.getMessage());
                //call.cancel();
                responseLatch.countDown();
            }
        });

        responseLatch.await();
        Assert.assertNotNull(fetchedData);
        Assert.assertNotNull(fetchedData.get(0).getProducts());
        Assert.assertNotNull(fetchedData.get(0).getProducts().get(0));
        Assert.assertNotNull(fetchedData.get(0).getProducts().get(1));
        Assert.assertEquals(14, fetchedData.get(0).getProducts().get(0).getAvailability().intValue());
        Assert.assertEquals(24, fetchedData.get(0).getProducts().get(1).getAvailability().intValue());
    }
}
