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

    private Button sortBy;
    private ImageButton back;
    private View include;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView icon, gender;
    private TextView username, intro, following, followers;
    private ImageButton follow, message;
    private FrameLayout up_timeline;
    private RecycleViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SwipeBackHelper.onCreate(this);

        back = findViewById(R.id.up_back);
        include = findViewById(R.id.content_user_profile);
        toolbar = findViewById(R.id.up_toolbar);
        collapsingToolbarLayout = findViewById(R.id.up_ctoolbar);
        sortBy = include.findViewById(R.id.up_sortby_button);
        icon = include.findViewById(R.id.up_usericon);
        gender = include.findViewById(R.id.up_gender);
        username = include.findViewById(R.id.up_username);
        intro = include.findViewById(R.id.up_intro);
        following = include.findViewById(R.id.up_following);
        followers = include.findViewById(R.id.up_followers);
        follow = include.findViewById(R.id.up_follow);
        message = include.findViewById(R.id.up_message);
        up_timeline = include.findViewById(R.id.up_timeline);
        fragment = new RecycleViewFragment();
        fragment.setAdapter(new PostsAdapter());
        fragment.show(getSupportFragmentManager(), R.id.up_timeline);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        setUserProfile(getIntent().getStringExtra("userid"));
    }

    private void setUserProfile(final String userid){
        User user = User.findUser(userid);
        collapsingToolbarLayout.setTitle(user.getUsername());
        username.setText(user.getUsername());
        if (user.getGender().equals(getString(R.string.unknown))) gender.setVisibility(View.INVISIBLE);
        else{
            gender.setVisibility(View.VISIBLE);
            gender.setImageDrawable(user.getGender().equals(getString(R.string.male)) ? getDrawable(R.drawable.ic_male) : getDrawable(R.drawable.ic_female));
        }
        following.setText(user.getFollowers().size() + " followers");
        followers.setText(user.getFollowers().size() + " followers");
        intro.setText("Intro: " + user.getIntro());
        if (userid.equals(User.user.getUserid())){
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
            final boolean isFollowing = User.user.isFollowing(userid);
            follow.setImageResource(isFollowing ? R.drawable.ic_unfollow : R.drawable.ic_follow);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFollowing){
                        User.user.follow(userid);
                    } else{
                        User.user.unfollow(userid);
                    }
                    follow.setImageResource(isFollowing ? R.drawable.ic_unfollow : R.drawable.ic_follow);
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
            setUserProfile(User.user.getUserid());
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
