package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class SearchActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backButton = findViewById(R.id.search_back);
        mViewPager = findViewById(R.id.search_viewpager);
        mTabLayout = findViewById(R.id.search_tabs);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchPostFragment(), "Posts");
        adapter.addFragment(new SearchUserFragment(), "Users");
        adapter.addFragment(new SearchHistoryFragment(), "History");
        mViewPager.setAdapter(adapter);
    }
}
