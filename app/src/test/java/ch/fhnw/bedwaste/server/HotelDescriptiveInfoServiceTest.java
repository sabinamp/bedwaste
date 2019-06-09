package ch.fhnw.bedwaste.server;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.TestTimedOutException;

import java.util.concurrent.CountDownLatch;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HotelDescriptiveInfoServiceTest {
    private CountDownLatch responseLatch;
    private HotelDescriptiveInfo hotelInfo = null;
    static final String BASE_URL = "http://86.119.40.244:8888";

    @Before
    public void setUp() {
        responseLatch=new CountDownLatch(1);
    }

    @Test(timeout=30000)
    public void fetchInfo() throws InterruptedException {
        String hotelHelmhausId= "00U5846j022d292h";
        Retrofit retrofit= APIClient.getClient();
        HotelDescriptiveInfoInterface jsonDescriptiveInfoAPI= retrofit.create(HotelDescriptiveInfoInterface.class);
        Call<HotelDescriptiveInfo> callApi = jsonDescriptiveInfoAPI.getDescriptiveInfo("en", hotelHelmhausId);
        callApi.enqueue(new Callback<HotelDescriptiveInfo>() {
            @Override
            public void onResponse(Call<HotelDescriptiveInfo> call, Response<HotelDescriptiveInfo> response) {
                if(response.isSuccessful()){

                   hotelInfo= response.body();
                }else{
                    System.out.println( "unsuccessful response");
                    return;
                }
                responseLatch.countDown();
            }

            @Override
            public void onFailure(Call<HotelDescriptiveInfo> call, Throwable t) {
                t.printStackTrace();
               // call.cancel();
                System.out.println( t.getMessage());
                responseLatch.countDown();
            }
        });

        responseLatch.await();

        Assert.assertNotNull(hotelInfo);
        Assert.assertEquals("Hotel Helmhaus", hotelInfo.getHotelName());
        //Assert.assertNotNull(hotelInfo.getAffiliationInfo().getAwards());
        //Assert.assertNotNull(hotelInfo.getAffiliationInfo().getAwards().get(1));
        Assert.assertEquals(9.2, Double.valueOf(hotelInfo.getAffiliationInfo().getAwards().get(1).getRating()), 0.01);
        Assert.assertEquals("Hotels.com", hotelInfo.getAffiliationInfo().getAwards().get(1).getProvider());

    }

    @Test(timeout=30000)
    public void fetchNonExistingHotelID() throws InterruptedException {
        //test if e.g. hotelid nonexistent
        //test if e.g. hotelid nonexistent
        //test the unexpected, and what you already expect
        String hotelWrongId = "XXXWrongIDXXX";
        Retrofit retrofit = APIClient.getClient();
        HotelDescriptiveInfoInterface jsonDescriptiveInfoAPI = retrofit.create(HotelDescriptiveInfoInterface.class);
        Call<HotelDescriptiveInfo> callApi = jsonDescriptiveInfoAPI.getDescriptiveInfo("en", hotelWrongId);

        callApi.enqueue(new Callback<HotelDescriptiveInfo>() {
                @Override
                public void onResponse(Call<HotelDescriptiveInfo> call, Response<HotelDescriptiveInfo> response) {
                    //check whether the request is actual successful (status 200-299)
                    if (response.isSuccessful()) {
                        hotelInfo = response.body();
                        System.out.println(response.code() + " ");
                        responseLatch.countDown();
                    } else {
                        APIError error = APIErrorUtils.parseError(response);
                        System.out.println(response.code() + " " + " Error message: " + error.message);
                        responseLatch.countDown();
                        return;
                    }

                }

                @Override
                public void onFailure(Call<HotelDescriptiveInfo> call, Throwable t) {
                    t.printStackTrace();
                    call.cancel();
                    System.out.println(t.getMessage());
                    responseLatch.countDown();
                }
            });


        responseLatch.await();

        Assert.assertNull(hotelInfo);



    }
}

