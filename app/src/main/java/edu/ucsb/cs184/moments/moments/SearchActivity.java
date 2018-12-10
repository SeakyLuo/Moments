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

import static edu.ucsb.cs184.moments.moments.SearchPair.GROUPS;
import static edu.ucsb.cs184.moments.moments.SearchPair.HISTORY;
import static edu.ucsb.cs184.moments.moments.SearchPair.POSTS;
import static edu.ucsb.cs184.moments.moments.SearchPair.USERS;
import static edu.ucsb.cs184.moments.moments.SearchPair.types;

public class SearchActivity extends AppCompatActivity {
    public static final String TAB = "tab", KEYWORD = "keyword";
    private static final String[] textHints = {"Search Post Content", "Search Users", "Search Groups", "Search History"};
    private ArrayList<RecyclerViewFragment> fragments = new ArrayList<>();
    private RecyclerViewFragment postsFragment, usersFragment, groupsFragment, historyFragment;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private EditText searchBar;
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
        searchBar = findViewById(R.id.search_text);
        mViewPager = findViewById(R.id.search_viewpager);
        mTabLayout = findViewById(R.id.search_tabs);

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
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearButton.setVisibility((s.toString().isEmpty()) ? View.GONE : View.VISIBLE);
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = v.getText().toString().trim();
                    search(keyword, types[position]);
                    return true;
                }
                return false;
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
                if (keyword != null && !keyword.isEmpty() && !keyword.equals(new_keyword) && !types[position].equals(HISTORY)){
                    search(keyword, types[position]);
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
        adapter.addFragments(fragments, Arrays.asList(types));
        mViewPager.setAdapter(adapter);

        String searchTab = intent.getStringExtra(TAB);
        searchTab = searchTab == null ? POSTS : searchTab;
        setCurrentTab(searchTab);
        String searchKeyword = intent.getStringExtra(KEYWORD);
        if (searchKeyword != null){
            keyword = searchKeyword;
            searchBar.setText(keyword);
            search(keyword, searchTab);
        }
    }

    private void search(final String keyword, final String type){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Searching...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (type){
                        case POSTS:
                            postsFragment.setData(searchPosts(keyword));
                            break;
                        case USERS:
                            usersFragment.setData(searchUsers(keyword));
                            break;
                        case GROUPS:
                            groupsFragment.setData(searchGroups(keyword));
                            break;
                        case HISTORY:
                            postsFragment.setData(searchPosts(keyword));
                            setCurrentTab(POSTS);
                            break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(User.user.addHistory(new SearchPair(keyword, type.equals(HISTORY) ? POSTS : type))){
                        try {
                            historyFragment.setData(User.user.getSearchHistory());
                        } catch (RecyclerViewFragment.UnsupportedDataException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    public void searchHistory(SearchPair pair){
        searchBar.setText(pair.keyword);
        search(pair.keyword, pair.type);
        setCurrentTab(pair.type);
    }

    public static ArrayList<Post> searchPosts(String keyword){
        ArrayList<Post> data = FirebaseHelper.searchPosts(keyword);
        String lowKey = keyword.toLowerCase();
        if (!lowKey.equals(keyword))
            for (Post obj: FirebaseHelper.searchPosts(lowKey))
                if (!data.contains(obj))
                    data.add(obj);
        return data;
    }

    public static ArrayList<User> searchUsers(String keyword){
        ArrayList<User> data = FirebaseHelper.searchUsers(keyword);
        String lowKey = keyword.toLowerCase();
        if (!lowKey.equals(keyword))
            for (User obj: FirebaseHelper.searchUsers(lowKey))
                if (!data.contains(obj))
                    data.add(obj);
        return data;
    }

    public static ArrayList<Group> searchGroups(String keyword){
        ArrayList<Group> data = FirebaseHelper.searchGroups(keyword);
        String lowKey = keyword.toLowerCase();
        if (!lowKey.equals(keyword))
            for (Group obj: FirebaseHelper.searchGroups(lowKey))
                if (!data.contains(obj))
                    data.add(obj);
        return data;
    }

    private RecyclerViewFragment getFragmentAt(String tab){
        return fragments.get(Arrays.asList(types).indexOf(tab));
    }

    private void setCurrentTab(String tab){
        mTabLayout.getTabAt(Arrays.asList(types).indexOf(tab)).select();
    }

    private void setupFragments(){
        postsFragment = new RecyclerViewFragment();
        usersFragment = new RecyclerViewFragment();
        groupsFragment = new RecyclerViewFragment();
        historyFragment = new RecyclerViewFragment();
        postsFragment.setAdapter(new PostAdapter());
        usersFragment.setAdapter(new UserAdapter());
        groupsFragment.setAdapter(new SearchGroupsAdapter());
        SearchHistoryAdapter historyAdapter = new SearchHistoryAdapter();
        historyAdapter.setSearchActivity(this);
        historyFragment.setAdapter(historyAdapter);
        postsFragment.setSwipeEnabled(false);
        usersFragment.setSwipeEnabled(false);
        groupsFragment.setSwipeEnabled(false);
        historyFragment.setSwipeEnabled(false);
        usersFragment.setShowDivider(true);
        groupsFragment.setShowDivider(true);
        historyFragment.setShowDivider(true);
        try {
            historyFragment.setData(User.user.getSearchHistory());
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
        historyFragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    historyFragment.setData(User.user.getSearchHistory());
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
