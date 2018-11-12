package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView dNavigationView;
    private BottomNavigationView bNavigation;
    private ImageButton menuButton;
    private HomeFragment homeFragment;
    private GroupFragment groupFragment;
    private NotificationsFragment notificationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        dNavigationView = findViewById(R.id.drawer_navigation);
        dNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                item.setCheckable(false);
                drawer.closeDrawer(Gravity.START);
                switch (item.getItemId()){
                    case R.id.nav_user_profile:
                        return true;
                    case R.id.nav_user_collections:
                        return true;
                    case R.id.nav_user_draftbox:
                        return true;
                    case R.id.nav_settings:
                        return true;
                }
                return false;
            }
        });
        bNavigation = findViewById(R.id.navigation);
        bNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragment(homeFragment);
                        return true;
                    case R.id.navigation_groups:
                        showFragment(groupFragment);
                        return true;
                    case R.id.navigation_notifications:
                        showFragment(notificationsFragment);
                        return true;
                }
                return false;
            }
        });
        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
        }else{
            homeFragment = new HomeFragment();
            groupFragment = new GroupFragment();
            notificationsFragment = new NotificationsFragment();
            showFragment(homeFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment);
    }

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, fragment)
                .commit();
    }

    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
