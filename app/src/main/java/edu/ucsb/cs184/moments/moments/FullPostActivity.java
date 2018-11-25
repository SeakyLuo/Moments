package edu.ucsb.cs184.moments.moments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FullPostActivity extends AppCompatActivity {

    public static final String POST = "post";
    public static final String ADD_COMMENT = "Add Comment";

    private TabViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter pagerAdapter;
    private ImageButton more;
    private ImageButton backButton;
    private View include;
    private ImageView poster_icon;
    private TextView username;
    private TextView time;
    private TextView content;
    private ImageButton comment;
    private ImageButton collect;
    private ImageButton share;
    private ImageView usericon;
    private EditText edit_comment;
    private Intent intent;
    private FullPostCommentsFragment commentsFragment;
    private FullPostRatingsFragment ratingsFragment;
    private AddCommentFragment addCommentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);
        SwipeBackHelper.onCreate(this);

        intent = getIntent();
        final Post post = (Post) intent.getSerializableExtra(POST);

        commentsFragment = new FullPostCommentsFragment();
        ratingsFragment = new FullPostRatingsFragment();
        addCommentFragment = new AddCommentFragment();
        backButton = findViewById(R.id.fullpost_back);
        more = findViewById(R.id.fullpost_more);
        include = findViewById(R.id.up_post);
        comment = include.findViewById(R.id.post_comment);
        mViewPager = findViewById(R.id.fullpost_viewpager);
        mTabLayout = findViewById(R.id.fullpost_tablayout);
        usericon = findViewById(R.id.fpac_user_icon);
        edit_comment = findViewById(R.id.fpac_editcomment);

        addCommentFragment.setCaller(edit_comment, commentsFragment);
        addCommentFragment.setPost(post);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenuHelper helper = new PopupMenuHelper(R.menu.fullpost_more_menu, FullPostActivity.this, more);
                if (post.getUserid().equals(User.user.getId())){
                    helper.hideItem(R.id.fullpostmenu_delete);
                    helper.hideItem(R.id.fullpostmenu_follow);
                }else{
                    boolean isFollowing = User.user.isFollowing(post.getUserid());
                    helper.modifyIcon(R.id.fullpostmenu_follow, isFollowing ? "Unfollow" : "Follow",isFollowing ? R.drawable.ic_unfollow : R.drawable.ic_follow);
                }
                helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
                    @Override
                    public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.fullpostmenu_follow:

                                return true;
                            case R.id.fullpostmenu_copy:
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copy", content.getText());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(FullPostActivity.this,"Copied!",Toast. LENGTH_SHORT).show();
                                return true;
                            case R.id.fullpostmenu_delete:

                        }
                        return false;
                    }
                });
                helper.show();
            }
        });
//        include.setClickable(false);
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
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddComment();
            }
        });
        Bitmap icon = User.user.getIcon();
        usericon.setImageBitmap((icon == null) ? ((BitmapDrawable)getDrawable(R.drawable.user_icon)).getBitmap() : icon);
        edit_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddComment();
            }
        });

        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(commentsFragment, "Comments");
        pagerAdapter.addFragment(ratingsFragment, "Ratings");
        mViewPager.setAdapter(pagerAdapter);
        setPost(post);

        if (intent.getStringExtra(ADD_COMMENT) != null) showAddComment();
    }

    private void setPost(Post post){
        poster_icon = include.findViewById(R.id.post_usericon);
        username = include.findViewById(R.id.post_username);
        time = include.findViewById(R.id.post_time);
        content = include.findViewById(R.id.post_content);
        comment = include.findViewById(R.id.post_comment);
        collect = include.findViewById(R.id.post_collect);
        share = include.findViewById(R.id.post_share);
        poster_icon.setClickable(true);
        poster_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        User user = User.findUser(post.getUserid());
        username.setText(user.getName());
//        poster_icon.setImageBitmap(user.getIcon());
        time.setText(TimeText(post.getTime()));
        content.setText(post.getContent());
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    setCollect(post.isCollected(userid));
                setCollect(true);
            }
        });
    }

    public static String TimeText(Date date){
        Date now = new Date();
        if (now.getYear() != date.getYear()) return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        else if (now.getDay() == date.getDay()) return new SimpleDateFormat("HH:mm").format(date);
        return new SimpleDateFormat("MM-dd HH:mm").format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCollect(boolean collected){
        collect.setImageResource(collected ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
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

    private void showAddComment(){
        addCommentFragment.showNow(getSupportFragmentManager(), ADD_COMMENT);
    }
}
