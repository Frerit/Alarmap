package info.alarmap.us.alarmap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import info.alarmap.us.alarmap._fragments.*;

public class Main extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_explore:
                    _ExploreFragment exploreFragment = new _ExploreFragment();
                    FragmentManager changeExplore = getSupportFragmentManager();
                    changeExplore.beginTransaction()
                            .replace(R.id.contentLayout,exploreFragment)
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void createHomeFragment() {
        _HomeFragment homeFragment = new _HomeFragment();
        FragmentManager changeHome = getSupportFragmentManager();
        changeHome.beginTransaction()
                .replace(R.id.contentLayout, homeFragment)
                .commit();
    }
}
