package ch.fhnw.bedwaste.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import ch.fhnw.bedwaste.model.AvailabilityResults;
import retrofit2.Response;

public class HotelAvailabilityResultsServiceTest {
    private CountDownLatch responseLatch;
    private AvailabilityResults hotelAvailabilityResults;
    static final String BASE_URL = "http://86.119.40.244:8888";
    private AvailabilityResults fetchedData=null;
    private HotelAvailabilityResultsService service;

    @Before
    public void setUp() {
        responseLatch=new CountDownLatch(1);
        service = new HotelAvailabilityResultsService(new AvailabilityResultsListener() {
            @Override
            public void success(Response<AvailabilityResults> response) {
                System.out.println("successful call"+response.code()+"");
                fetchedData= response.body();
                responseLatch.countDown();
            }

            @Override
            public void failed(String message) {
                System.out.println(message);
                responseLatch.countDown();
            }
        });
    }

    @Test(timeout=30000)
    public void fetchAvailabilityInfo1Adult() throws InterruptedException {
        String hotelHelmhausId = "00U5846j022d292h";

        service.getRoomAvailabilitiesInHotel(hotelHelmhausId, 1,0,0);
        responseLatch.await();
        Assert.assertNotNull(fetchedData.get(0).getProducts());
        Assert.assertNotNull(fetchedData.get(0).getProducts().get(0));
        Assert.assertNotNull(fetchedData.get(0).getProducts().get(1));
        Assert.assertEquals(14, fetchedData.get(0).getProducts().get(0).getAvailability().intValue());
        Assert.assertEquals(24, fetchedData.get(0).getProducts().get(1).getAvailability().intValue());
    }

    @Test(timeout=30000)
    public void fetchAvailabilityInfo1AdultNonexistingHotel() throws InterruptedException {
        String hotelId = "00Uxxxxxxh";

        service.getRoomAvailabilitiesInHotel(hotelId, 1,0,0);
        responseLatch.await();
        Assert.assertNull(fetchedData);

    }
}
