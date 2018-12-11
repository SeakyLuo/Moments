package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;
import java.util.Collections;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USERID = "userid";
    public static final String SORTBY = "Sort By";
    private Button sortBy;
    private ImageButton back, search;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView icon, gender;
    private TextView username, intro, following, followers, posts_count;
    private ImageButton  edit, follow, message;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerViewFragment fragment;
    private String userid;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SwipeBackHelper.onCreate(this);

        userid = getIntent().getStringExtra(USERID);
        user = User.findUser(getIntent().getStringExtra(USERID));
        back = findViewById(R.id.up_back);
        search = findViewById(R.id.up_search);
        toolbar = findViewById(R.id.up_toolbar);
        collapsingToolbarLayout = findViewById(R.id.up_ctoolbar);
        sortBy = findViewById(R.id.up_sortby_button);
        icon = findViewById(R.id.up_usericon);
        gender = findViewById(R.id.up_gender);
        username = findViewById(R.id.up_username);
        intro = findViewById(R.id.up_intro);
        following = findViewById(R.id.up_following);
        followers = findViewById(R.id.up_followers);
        edit = findViewById(R.id.up_edit);
        follow = findViewById(R.id.up_follow);
        posts_count = findViewById(R.id.up_posts);
        message = findViewById(R.id.up_message);
        recyclerView = findViewById(R.id.up_timeline);
        swipeRefreshLayout = findViewById(R.id.up_swipeFreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        fragment = new RecyclerViewFragment();
        PostAdapter adapter = new PostAdapter();
        adapter.setUsericonClickable(false);
        fragment.setSwipeEnabled(false);
        fragment.setAdapter(adapter);
        fragment.setRecyclerView(recyclerView);

        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SearchListActivity.class);
                intent.putExtra(SearchListActivity.ADAPTER, SearchListActivity.POST);
                intent.putExtra(SearchListActivity.DATA, user.getPosts());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Maximize Image
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sortBy.setText(SORTBY);
        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuHelper helper = new PopupMenuHelper(R.menu.sort_by_menu, UserProfileActivity.this, sortBy);
                helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
                    @Override
                    public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                        ArrayList<Post> data = user.getPosts();
                        switch (menuItem.getItemId()){
                            case R.id.sb_most_popular:
                                sortBy.setText(SORTBY + ": " + getString(R.string.most_popular));
                                Collections.sort(data, new Post.PopularityComparator());
                                break;
                            case R.id.sb_least_popular:
                                sortBy.setText(SORTBY + ": " + getString(R.string.least_popular));
                                Collections.sort(data, new Post.PopularityComparator());
                                Collections.reverse(data);
                                break;
                            case R.id.sb_highest_rating:
                                sortBy.setText(SORTBY + ": " + getString(R.string.highest_rating));
                                Collections.sort(data, new Post.RatingComparator());
                                break;
                            case R.id.sb_lowest_rating:
                                sortBy.setText(SORTBY + ": " + getString(R.string.lowest_rating));
                                Collections.sort(data, new Post.RatingComparator());
                                Collections.reverse(data);
                                break;
                            case R.id.sb_latest:
                                sortBy.setText(SORTBY + ": " + getString(R.string.most_recent));
                                break;
                            case R.id.sb_oldest:
                                sortBy.setText(SORTBY + ": " + getString(R.string.least_recent));
                                Collections.sort(data, new Post.PostComparator());
                                Collections.reverse(data);
                                break;
                            default:
                                return false;
                        }
                        try {
                            fragment.setData(data);
                        } catch (RecyclerViewFragment.UnsupportedDataException e) {
                            e.printStackTrace();
                        }
                        fragment.gotoTop();
                        return true;
                    }
                });
                helper.show();
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.gotoTop();
            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SearchListActivity.class);
                intent.putExtra(SearchListActivity.ADAPTER, SearchListActivity.FOLLOWING);
                intent.putExtra(SearchListActivity.DATA, user.getFollowing());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SearchListActivity.class);
                intent.putExtra(SearchListActivity.ADAPTER, SearchListActivity.FOLLOWER);
                intent.putExtra(SearchListActivity.DATA, user.getFollowers());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                intent.putExtra(UploadIconActivity.ICON, ((BitmapDrawable) icon.getDrawable()).getBitmap());
                startActivityForResult(intent, EditUserProfileActivity.FINISH);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.user.isFollowing(userid)) User.user.unfollow(userid);
                else User.user.follow(userid);
                setFollow(userid);
            }
        });
        setUserProfile(user);
    }

    private void setUserProfile(User user){
        collapsingToolbarLayout.setTitle(user.getName());
        FirebaseHelper.setIcon(user.GetIcon(), this, icon);
        username.setText(user.getName());
        if (user.getGender().equals(getString(R.string.unknown))) gender.setVisibility(View.GONE);
        else{
            gender.setVisibility(View.VISIBLE);
            gender.setImageDrawable(user.getGender().equals(getString(R.string.male)) ? getDrawable(R.drawable.ic_male) : getDrawable(R.drawable.ic_female));
        }
        following.setText("Following: " + user.getFollowing().size());
        followers.setText("Followers: " + user.getFollowers().size());
        intro.setText("Intro: " + user.getIntro());
        posts_count.setText("Posts: " + user.getPosts().size());
        boolean self = userid.equals(User.user.getId());
        edit.setVisibility(self ? View.VISIBLE : View.GONE);
        message.setVisibility(self ? View.GONE : View.VISIBLE);
        follow.setVisibility(self ? View.GONE : View.VISIBLE);
        setFollow(userid);
        try {
            fragment.setData(user.getPosts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh(){
        user = User.findUser(userid);
        setUserProfile(user);
    }

    private static String FollowCount(int count){
        if (count > 1000000) return count / 1000000 + " M";
        else if (count > 1000) return count / 1000 + " K";
        return count + "";
    }

    private void setFollow(String userid){
        int followImage = UploadIconActivity.followImage(userid);
        follow.setImageResource(followImage);
        follow.setBackground(getDrawable(followImage == R.drawable.ic_unfollow ? R.drawable.button_border_green : R.drawable.button_border_orange));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == EditUserProfileActivity.FINISH){
            refresh();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
