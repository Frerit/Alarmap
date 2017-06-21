package info.alarmap.us.alarmap;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
                    createHomeFragment();
                    return true;
                case R.id.navigation_explore:
                    _ExploreFragment exploreFragment = new _ExploreFragment();
                    FragmentManager changeExplore = getSupportFragmentManager();
                    changeExplore.beginTransaction()
                            .replace(R.id.contentLayout,exploreFragment)
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    _DashBoardFragment dashBoardFragment = new _DashBoardFragment();
                    FragmentManager changeDash = getSupportFragmentManager();
                    changeDash.beginTransaction()
                            .replace( R.id.contentLayout, dashBoardFragment )
                            .commit();
                    return true;
                case R.id.navigation_notifications:
                    _NotificationFragment notificationFragment = new _NotificationFragment();
                    FragmentManager changeNotify = getSupportFragmentManager();
                    changeNotify.beginTransaction()
                            .replace( R.id.contentLayout, notificationFragment )
                            .commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createHomeFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setItemTextColor( ColorStateList.valueOf( Color.argb( 255,46,40,96 ) ));
        navigation.setDrawingCacheBackgroundColor( Color.argb( 255,46,40,96 ));
        navigation.setItemIconTintList( ColorStateList.valueOf( Color.argb( 255,82,77,139 ) ) );
    }

    private void createHomeFragment() {
        _HomeFragment homeFragment = new _HomeFragment();
        FragmentManager changeHome = getSupportFragmentManager();
        changeHome.beginTransaction()
                .replace(R.id.contentLayout, homeFragment)
                .commit();
    }
}
