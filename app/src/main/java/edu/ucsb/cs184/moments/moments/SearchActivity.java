package edu.ucsb.cs184.moments.moments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ArrayList<RecyclerViewFragment> fragments = new ArrayList<>();
    private RecyclerViewFragment postsFragment, usersFragment, groupsFragment, historyFragment;
    private EditText searchBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private ImageButton backButton, clearButton;
    private Intent intent;
    private int position;
    private String keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        intent = getIntent();

        backButton = findViewById(R.id.search_back);
        clearButton = findViewById(R.id.search_clear);
        mViewPager = findViewById(R.id.search_viewpager);
        mTabLayout = findViewById(R.id.search_tabs);
        searchBar = findViewById(R.id.search_text);

        setupFragments();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        clearButton.setVisibility(View.GONE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                clearButton.setVisibility(View.GONE);
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
                    search(keyword);
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
        adapter = new TabPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(fragments, Arrays.asList(tabIndices));
        mViewPager.setAdapter(adapter);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBar.setVisibility((s.length() == 0) ? View.GONE : View.VISIBLE);
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = v.getText().toString();
                    if (position == 3){
                        setCurrentTab(POSTS);
                    }
                    search(keyword);
                    return true;
                }
                return false;
            }
        });

        String searchTab = intent.getStringExtra(TAB);
        if (searchTab == null) setCurrentTab(POSTS);
        else setCurrentTab(searchTab);
    }

    public void search(final String keyword){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Searching...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (position){
                        case 0:
                            postsFragment.setData(FirebaseHelper.searchPosts(keyword));
                            break;
                        case 1:
                            usersFragment.setData(FirebaseHelper.searchUsers(keyword));
                            break;
                        case 2:
                            groupsFragment.setData(FirebaseHelper.searchGroups(keyword));
                            break;
                        case 3:
                            postsFragment.setData(FirebaseHelper.searchPosts(keyword));
                            historyFragment.addElement(keyword);
                            break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    User.user.addHistory(keyword);
                    dialog.dismiss();
                }
            }
        });
    }

    public RecyclerViewFragment getFragmentAt(String tab){
        return fragments.get(Arrays.asList(tabIndices).indexOf(tab));
    }

    public void setCurrentTab(String tab){
        mTabLayout.getTabAt(Arrays.asList(tabIndices).indexOf(tab)).select();
    }

    public void setupFragments(){
        postsFragment = new RecyclerViewFragment();
        usersFragment = new RecyclerViewFragment();
        groupsFragment = new RecyclerViewFragment();
        historyFragment = new RecyclerViewFragment();
        postsFragment.setAdapter(new PostsAdapter());
        usersFragment.setAdapter(new SearchUsersAdapter());
        groupsFragment.setAdapter(new SearchGroupsAdapter());
        historyFragment.setAdapter(new SearchHistoryAdapter());
        usersFragment.setShowDivider(true);
        groupsFragment.setShowDivider(true);
        historyFragment.setShowDivider(true);
        try {
            historyFragment.setData(User.user.getSearch_history());
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
        historyFragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    historyFragment.setData(User.user.getSearch_history());
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
            }
        });
        fragments.add(postsFragment);
        fragments.add(usersFragment);
        fragments.add(groupsFragment);
        fragments.add(historyFragment);
    }
}
