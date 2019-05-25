package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.GuestRoom;

public class BookingFirstStepFragment extends Fragment {
    /**
     * Debugging tag BookingFirstStepFragment used by the Android logger.
     */
    private static final String TAG = "BookingFirstSFragment";
    private View view;
    GuestRoom selectedRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.booking_first_step_fragment, container,false);
        selectedRoom= (GuestRoom) getActivity().getIntent().getSerializableExtra("guestroom_for_booking_first_step_activity");
        Toolbar tb = view.findViewById(R.id.toolbar_booking_first_step);
        tb.setSubtitle("Angaben eintragen");
        Log.d(TAG, " - onCreate(Bundle) called. ");
        TextView name= view.findViewById(R.id.input_name);
        TextView firstname= view.findViewById(R.id.input_firstname);
        TextView street= view.findViewById(R.id.input_street);
        return view;
    }
}
