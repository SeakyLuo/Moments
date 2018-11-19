package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {
    public static final String TAB = "TAB";
    public static final String POSTS = "Posts";
    public static final String USERS = "Users";
    public static final String GROUPS = "Groups";
    public static final String HISTORY = "History";
    private static final String[] tabIndices = {POSTS, USERS, GROUPS, HISTORY};
    private static final String[] textHints = {"Search Post Content", "Search Users", "Search Groups", "Search History"};
    private EditText searchBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private ImageButton backButton;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();

        backButton = findViewById(R.id.search_back);
        mViewPager = findViewById(R.id.search_viewpager);
        mTabLayout = findViewById(R.id.search_tabs);
        searchBar = findViewById(R.id.search_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                mViewPager.setCurrentItem(index, true);
                searchBar.setHint(textHints[index]);
                tab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchPostFragment(), POSTS);
        adapter.addFragment(new SearchUserFragment(), USERS);
        adapter.addFragment(new SearchGroupsFragment(), GROUPS);
        adapter.addFragment(new SearchHistoryFragment(), HISTORY);
        mViewPager.setAdapter(adapter);

        String searchTab = intent.getStringExtra(TAB);
        if (searchTab == null) setCurrentTab(POSTS);
        else setCurrentTab(searchTab);
    }

    public void setCurrentTab(String tab){
        mTabLayout.getTabAt(Arrays.asList(tabIndices).indexOf(tab)).select();
    }
}
