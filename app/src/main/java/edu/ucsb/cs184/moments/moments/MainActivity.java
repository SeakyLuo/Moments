package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private GroupsFragment groupsFragment;
    private NotificationsFragment notificationsFragment;
    private String lastTag;
    private final static String homeTag = "HOME";
    private final static String groupTag = "GROUPS";
    private final static String notificationTag = "NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_nav);

        drawer = findViewById(R.id.drawer_layout);
        dNavigationView = findViewById(R.id.drawer_navigation);
        dNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(false);
                item.setCheckable(false);
                drawer.closeDrawer(Gravity.START);
                Intent intent;
                switch (item.getItemId()){
                    case R.id.nav_user_profile:
                        intent = new Intent(getApplicationContext(), UserProfileActivity.class);
//                        intent.putExtra("userid",0);
                        startActivity(intent);
                        return true;
                    case R.id.nav_user_collections:
                        intent = new Intent(getApplicationContext(), UserCollectionsActivity.class);
//                        intent.putExtra("userid",0);
                        startActivity(intent);
                        return true;
                    case R.id.nav_user_draftbox:
                        intent = new Intent(getApplicationContext(), UserDraftboxActivity.class);
//                        intent.putExtra("userid",0);
                        startActivity(intent);
                        return true;
                    case R.id.nav_settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
//                        intent.putExtra("userid",0);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        View header = dNavigationView.getHeaderView(0);
        header.findViewById(R.id.nav_usericon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
        bNavigation = findViewById(R.id.navigation);
        bNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showFragment(homeFragment, homeTag, lastTag);
                        lastTag = homeTag;
                        return true;
                    case R.id.navigation_groups:
                        showFragment(groupsFragment, groupTag, lastTag);
                        lastTag = groupTag;
                        return true;
                    case R.id.navigation_notifications:
                        showFragment(notificationsFragment, notificationTag, lastTag);
                        lastTag = notificationTag;
                        return true;
                }
                return false;
            }
        });

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, homeTag);
        }else{
            homeFragment = new HomeFragment(); homeFragment.setDrawer(drawer);
            groupsFragment = new GroupsFragment(); groupsFragment.setDrawer(drawer);
            notificationsFragment = new NotificationsFragment(); notificationsFragment.setDrawer(drawer);
            getSupportFragmentManager().beginTransaction().add(R.id.contentView, homeFragment, homeTag).commit();
            lastTag = homeTag;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, homeTag, homeFragment);
    }

    protected void showFragment(Fragment fragment, String tag, String lastTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (lastTag != null) {
            Fragment lastFragment = fragmentManager.findFragmentByTag(lastTag);
            if (lastFragment != null) {
                transaction.hide(lastFragment);
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        }
        else {
            transaction.add(R.id.contentView, fragment, tag).setBreadCrumbShortTitle(tag);
        }
        transaction.commit();
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
