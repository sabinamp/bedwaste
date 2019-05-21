package ch.fhnw.bedwaste;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentManager;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    private BottomNavigationView mBottomNavigationView;
    private NetworkDetector netDetector = new NetworkDetector(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        addBottomNavigation();
        netDetector.networkRunnable.run();
    }

    private void addBottomNavigation() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        addClickListenersToBottomNav(mBottomNavigationView);
    }


    protected abstract void addClickListenersToBottomNav(BottomNavigationView bottomNavigationView);
}
