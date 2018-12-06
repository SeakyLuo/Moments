package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {
    public static final String TAB = "TAB";
    public static final String POSTS = "Posts";
    public static final String USERS = "Users";
    public static final String GROUPS = "Groups";
    public static final String HISTORY = "History";
    private static final String[] tabIndices = {POSTS, USERS, GROUPS, HISTORY};
    private static final String[] textHints = {"Search Post Content", "Search Users", "Search Groups", "Search History"};
    private SearchFragment searchPostsFragment, searchUsersFragment, searchGroupsFragment, searchHistoryFragment;
    private ArrayList<SearchFragment> fragments = new ArrayList<>();
    private EditText searchBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private ImageButton backButton;
    private Intent intent;
    private int position;
    private String keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();

        backButton = findViewById(R.id.search_back);
        mViewPager = findViewById(R.id.search_viewpager);
        mTabLayout = findViewById(R.id.search_tabs);
        searchBar = findViewById(R.id.search_text);
        fragments.add(new SearchFragment().setAdapter(POSTS));
        fragments.add(new SearchFragment().setAdapter(USERS));
        fragments.add(new SearchFragment().setAdapter(GROUPS));
        fragments.add(new SearchFragment().setAdapter(HISTORY));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                mViewPager.setCurrentItem(position, true);
                String new_keyword = searchBar.getText().toString();
                // if keyword is not changed, don't search
                // Refresh is ignored for now
                if (keyword != null && !keyword.isEmpty() && !keyword.equals(new_keyword) && !tabIndices[position].equals(HISTORY)){
                    fragments.get(position).search(keyword);
                }
                searchBar.setHint(textHints[position]);
                tab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        keyword = v.getText().toString();
                        SearchFragment fragment = fragments.get(position);
                        if (position == 3){
                            setCurrentTab(POSTS);
                            getFragmentAt(POSTS).search(keyword);
                        }else
                            fragment.search(keyword);
                        return true; // consume.
                    }
                }
                return false;
            }
        });

        adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(fragments, Arrays.asList(tabIndices));
        mViewPager.setAdapter(adapter);

        String searchTab = intent.getStringExtra(TAB);
        if (searchTab == null) setCurrentTab(POSTS);
        else setCurrentTab(searchTab);
    }

    public SearchFragment getFragmentAt(String tab){
        return fragments.get(Arrays.asList(tabIndices).indexOf(tab));
    }

    public void setCurrentTab(String tab){
        mTabLayout.getTabAt(Arrays.asList(tabIndices).indexOf(tab)).select();
    }
}
