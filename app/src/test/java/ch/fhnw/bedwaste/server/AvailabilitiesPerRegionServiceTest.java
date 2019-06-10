package ch.fhnw.bedwaste.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import retrofit2.Response;


public class AvailabilitiesPerRegionServiceTest {
    private CountDownLatch responseLatch;

    static final String BASE_URL = "http://86.119.40.244:8888";
    private List<AvailabilityResult> fetchedData=null;
    private AvailabilitiesPerRegionService service;
    @Before
    public void setUp() {
        responseLatch = new CountDownLatch(1);
        service = new AvailabilitiesPerRegionService(new AvailabilitiesPerRegionListener() {
            @Override
            public void success(Response<List<AvailabilityResult>> response) {
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
    public void fetchAvailabilityInfoPerRegion1Adult() throws InterruptedException {

        String region="ZH";
        service.getAvailabilitiesPerRegion(region, 1, 0, 0, 400, 1, null, null);
        responseLatch.await();

        Assert.assertNotNull(fetchedData);
        Assert.assertEquals(9, fetchedData.size());
    }

    @Test(timeout=30000)
    public void fetchAvailabilityInfoPerRegion1AdultNonExistingRegion() throws InterruptedException {

        String region="HH";
        service.getAvailabilitiesPerRegion(region, 1, 0, 0, 400, 1, null, null);
        responseLatch.await();

        Assert.assertNull(fetchedData);

    }

    @Test(timeout=30000)
    public void fetchAvailabilityInfoPerRegion1AdultWithBreakfastWithWifi() throws InterruptedException {
        String region="ZH";
        service.getAvailabilitiesPerRegion(region, 1, 0, 0, 400, 1, true, true);
        responseLatch.await();

        Assert.assertNotNull(fetchedData);
        Assert.assertEquals(6, fetchedData.size());

    }
}
