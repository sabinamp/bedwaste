package ch.fhnw.bedwaste.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import retrofit2.Response;

public class HotelDescriptiveInfoServiceTest {
    private CountDownLatch responseLatch;
    private HotelDescriptiveInfo hotelInfo = null;
    static final String BASE_URL = "http://86.119.40.244:8888";
    private HotelDescriptiveInfoService service=null;

    @Before
    public void setUp() {
        responseLatch=new CountDownLatch(1);
        service = new HotelDescriptiveInfoService(new HotelDescriptiveInfoListener() {
            @Override
            public void success(Response<HotelDescriptiveInfo> response) {
                hotelInfo = response.body();
                System.out.println( "successfull call");
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
    public void fetchInfo() throws InterruptedException {
        String hotelHelmhausId= "00U5846j022d292h";

        service.getHotelDescriptiveInfo("en", hotelHelmhausId);
        responseLatch.await();

        Assert.assertNotNull(hotelInfo);
        Assert.assertEquals("Hotel Helmhaus", hotelInfo.getHotelName());
        Assert.assertNotNull(hotelInfo.getAffiliationInfo().getAwards());
        Assert.assertNotNull(hotelInfo.getAffiliationInfo().getAwards().get(1));
        Assert.assertEquals(9.2, Double.valueOf(hotelInfo.getAffiliationInfo().getAwards().get(1).getRating()), 0.01);
        Assert.assertEquals("Hotels.com", hotelInfo.getAffiliationInfo().getAwards().get(1).getProvider());
    }
    @Test(timeout=30000)
    public void fetchInfoNonExistingHotel() throws InterruptedException {
        String hotelId= "00Uxxxxh";

        service.getHotelDescriptiveInfo("en", hotelId);
        responseLatch.await();

        Assert.assertNull(hotelInfo);

    }
}
