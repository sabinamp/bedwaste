package ch.fhnw.bedwaste;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HotelListActivityContextTest {
    private View listfragment=null;
    private View hotelItemRoot= null;
    @Before
    public void init(){
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                LayoutInflater inflater=LayoutInflater
                        .from(InstrumentationRegistry.getTargetContext());
                listfragment=inflater.inflate(R.layout.hotel_list_view_fragment, null);

            }
        });
        hotelItemRoot = listfragment.getRootView();

    }

    @Test
    public void exists() {
        Assert.assertNotNull(hotelItemRoot);
    }

    @After
    public void tearDown(){
        listfragment=null;
    }
}
