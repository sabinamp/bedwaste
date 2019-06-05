package ch.fhnw.bedwaste;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RoomTypesActivityTest {
    private RecyclerView listView= null;
    @Rule
    public ActivityTestRule<RoomTypesActivity> roomTypesActivityTestRule = new ActivityTestRule<RoomTypesActivity>(RoomTypesActivity.class, true);

    @Before
    public void setUp(){
        listView = roomTypesActivityTestRule.getActivity().findViewById(R.id.room_types_recyclerView);
   }

   @Test
   public void listCount(){
        Assert.assertTrue("list count is less than 5", listView.getAdapter().getItemCount() < 5);
   }
}
