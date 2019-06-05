package ch.fhnw.bedwaste;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class RoomTypesActivityTest {
    private RecyclerView listView= null;
    private RoomTypesActivity roomTypesActivity;
    @Rule
    public final ActivityTestRule<RoomTypesActivity> roomTypesActivityTestRule = new ActivityTestRule<>(RoomTypesActivity.class, true);

    @Before
    public void setUp(){
        roomTypesActivity = roomTypesActivityTestRule.getActivity();
        listView = roomTypesActivity.findViewById(R.id.room_types_recyclerView);
   }

   @Test
   public void listCount(){
        Assert.assertTrue("list count is less than 5", listView.getAdapter().getItemCount() < 5);
   }

   @After
   public void tearDown(){
        roomTypesActivity = null;
        listView = null;
   }
}
