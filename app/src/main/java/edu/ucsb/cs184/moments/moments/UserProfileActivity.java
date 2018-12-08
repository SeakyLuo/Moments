package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USERID = "userid";
    private Button sortBy;
    private ImageButton back, search;
    private View include;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView icon, gender;
    private TextView username, user_number, intro, following, followers, posts_count;
    private ImageButton  edit, follow, message;
    private FrameLayout up_timeline;
    private RecyclerViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SwipeBackHelper.onCreate(this);

        back = findViewById(R.id.up_back);
        search = findViewById(R.id.up_search);
        include = findViewById(R.id.content_user_profile);
        toolbar = findViewById(R.id.up_toolbar);
        collapsingToolbarLayout = findViewById(R.id.up_ctoolbar);
        sortBy = include.findViewById(R.id.up_sortby_button);
        icon = include.findViewById(R.id.up_usericon);
        gender = include.findViewById(R.id.up_gender);
        username = include.findViewById(R.id.up_username);
        user_number = include.findViewById(R.id.up_user_number);
        intro = include.findViewById(R.id.up_intro);
        following = include.findViewById(R.id.up_following);
        followers = include.findViewById(R.id.up_followers);
        edit = include.findViewById(R.id.up_edit);
        follow = include.findViewById(R.id.up_follow);
        posts_count = include.findViewById(R.id.up_posts);
        message = include.findViewById(R.id.up_message);
        up_timeline = include.findViewById(R.id.up_timeline);
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new PostAdapter());

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
                // search my posts
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
        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuHelper helper = new PopupMenuHelper(R.menu.sort_by_menu, UserProfileActivity.this, sortBy);
                helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
                    @Override
                    public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sb_highest_rating:
                                return true;
                            case R.id.sb_lowest_rating:
                                return true;
                            case R.id.sb_lastest:
                                return true;
                            case R.id.sb_oldest:
                                return true;
                        }
                        return false;
                    }
                });
                helper.show();
            }
        });
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        setUserProfile(getIntent().getStringExtra(USERID));
    }

    private void setUserProfile(final String userid){
        final User user = User.findUser(userid);
        collapsingToolbarLayout.setTitle(user.getName());
        username.setText(user.getName());
        user_number.setText("#" + user.getNumber());
        if (user.getGender().equals(getString(R.string.unknown))) gender.setVisibility(View.GONE);
        else{
            gender.setVisibility(View.VISIBLE);
            gender.setImageDrawable(user.getGender().equals(getString(R.string.male)) ? getDrawable(R.drawable.ic_male) : getDrawable(R.drawable.ic_female));
        }
        following.setText("Following: " + user.getFollowing().size());
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, FollowListActivity.class);
                intent.putExtra(FollowListActivity.TITLE, FollowListActivity.FOLLOWING);
                intent.putExtra(FollowListActivity.LIST, user.getFollowing());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        followers.setText("Followers: " + user.getFollowers().size());
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, FollowListActivity.class);
                intent.putExtra(FollowListActivity.TITLE, FollowListActivity.FOLLOWER);
                intent.putExtra(FollowListActivity.LIST, user.getFollowers());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        intro.setText("Intro: " + user.getIntro());
        posts_count.setText("Posts: " + user.getPosts().size());
        boolean self = userid.equals(User.user.getId());
        edit.setVisibility(self ? View.VISIBLE : View.GONE);
        message.setVisibility(self ? View.GONE : View.VISIBLE);
        follow.setVisibility(self ? View.GONE : View.VISIBLE);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                intent.putExtra(UploadIconActivity.ICON, ((BitmapDrawable) icon.getDrawable()).getBitmap());
                startActivityForResult(intent, EditUserProfileActivity.FINISH);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        setFollow(userid);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.user.isFollowing(userid)) User.user.unfollow(userid);
                else User.user.follow(userid);
                setFollow(userid);
            }
        });
        try {
            fragment.addElements(user.getPosts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.show(getSupportFragmentManager(), R.id.up_timeline);
    }

    private static String FollowCount(int count){
        if (count > 1000000) return count / 1000000 + " M";
        else if (count > 1000) return count / 1000 + " K";
        return count + "";
    }

    private void setFollow(String userid){
        int followImage = IconHelper.followImage(userid);
        follow.setImageResource(followImage);
        follow.setBackground(getDrawable(followImage == R.drawable.ic_unfollow ? R.drawable.button_border_green : R.drawable.button_border_orange));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == EditUserProfileActivity.FINISH){
            setUserProfile(User.user.getId());
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
