package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private TextView titleText;
    private ImageButton menuButton;
    private HomeFragment homeFragment;
    private GroupFragment groupFragment;
    private MessageFragment messageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        titleText = findViewById(R.id.titleText);
        menuButton = findViewById(R.id.menuButton);
        drawer = findViewById(R.id.drawer_layout);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });

        homeFragment = new HomeFragment();
        groupFragment = new GroupFragment();
        messageFragment = new MessageFragment();
        showFragment(homeFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    titleText.setText(R.string.home);
                    showFragment(homeFragment);
                    return true;
                case R.id.navigation_group:
                    titleText.setText(R.string.group);
                    showFragment(groupFragment);
                    return true;
                case R.id.navigation_messages:
                    titleText.setText(R.string.messages);
                    showFragment(messageFragment);
                    return true;
            }
            return false;
        }
    };

    private void showFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
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
