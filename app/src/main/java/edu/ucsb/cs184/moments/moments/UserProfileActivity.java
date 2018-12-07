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
    private ImageButton follow, message;
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
        follow = include.findViewById(R.id.up_follow);
        posts_count = include.findViewById(R.id.up_posts);
        message = include.findViewById(R.id.up_message);
        up_timeline = include.findViewById(R.id.up_timeline);
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new PostsAdapter());
        try {
            fragment.addElements(User.user.getPosts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.show(getSupportFragmentManager(), R.id.up_timeline);

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
        User user = User.findUser(userid);
        collapsingToolbarLayout.setTitle(user.getName());
        username.setText(user.getName());
        user_number.setText("#" + user.getNumber());
        if (user.getGender().equals(getString(R.string.unknown))) gender.setVisibility(View.INVISIBLE);
        else{
            gender.setVisibility(View.VISIBLE);
            gender.setImageDrawable(user.getGender().equals(getString(R.string.male)) ? getDrawable(R.drawable.ic_male) : getDrawable(R.drawable.ic_female));
        }
        following.setText(user.getFollowers().size() + " following");
        followers.setText(user.getFollowers().size() + " followers");
        intro.setText("Intro: " + user.getIntro());
        posts_count.setText("Posts: " + user.getPosts().size());
        if (userid.equals(User.user.getId())){
            follow.setImageResource(R.drawable.ic_edit);
            message.setVisibility(View.INVISIBLE);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eup = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                    eup.putExtra(UploadIconActivity.ICON, ((BitmapDrawable) icon.getDrawable()).getBitmap());
                    startActivityForResult(eup, EditUserProfileActivity.FINISH);
                }
            });
        }else{
            message.setVisibility(View.VISIBLE);
            follow.setImageResource(User.user.isFollowing(userid) ? R.drawable.ic_unfollow : R.drawable.ic_follow);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (User.user.isFollowing(userid)){
                        User.user.unfollow(userid);
                        follow.setImageResource(R.drawable.ic_follow);
                    }
                    else{
                        User.user.follow(userid);
                        follow.setImageResource(R.drawable.ic_unfollow);
                    }
                }
            });
        }
        try {
            fragment.addElements(user.getPosts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String FollowCount(int count){
        if (count > 1000000) return count / 1000000 + " M";
        else if (count > 1000) return count / 1000 + " K";
        return count + "";
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
