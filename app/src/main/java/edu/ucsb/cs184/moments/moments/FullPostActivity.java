package edu.ucsb.cs184.moments.moments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FullPostActivity extends AppCompatActivity {

    public static final String POST = "post";
    public static final String ADD_COMMENT = "Add Comment";
    public static final int DELETE_POST = 1;

    private TabViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter pagerAdapter;
    private ImageButton more;
    private ImageButton backButton;
    private View include;
    private ImageView poster_icon;
    private TextView username, time, content, comments_counter;
    private RatingBar ratingBar;
    private ImageButton comment, collect, share, dropdown;
    private ImageView usericon;
    private EditText edit_comment;
    private Post post;
    private FullPostCommentsFragment commentsFragment;
    private FullPostRatingsFragment ratingsFragment;
    private AddCommentDialog addCommentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);
        SwipeBackHelper.onCreate(this);

        Intent intent = getIntent();
        post = intent.getParcelableExtra(POST);

        commentsFragment = new FullPostCommentsFragment();
        ratingsFragment = new FullPostRatingsFragment();
        addCommentDialog = new AddCommentDialog();
        backButton = findViewById(R.id.fullpost_back);
        more = findViewById(R.id.fullpost_more);
        include = findViewById(R.id.fullpost_post);
        poster_icon = include.findViewById(R.id.post_usericon);
        username = include.findViewById(R.id.post_username);
        time = include.findViewById(R.id.post_time);
        comments_counter = include.findViewById(R.id.post_comments_counter);
        content = include.findViewById(R.id.post_content);
        ratingBar = include.findViewById(R.id.post_ratingBar);
        comment = include.findViewById(R.id.post_comment);
        collect = include.findViewById(R.id.post_collect);
        share = include.findViewById(R.id.post_share);
        dropdown = include.findViewById(R.id.post_dropdown);
        mViewPager = findViewById(R.id.fullpost_viewpager);
        mTabLayout = findViewById(R.id.fullpost_tablayout);
        usericon = findViewById(R.id.fpac_user_icon);
        edit_comment = findViewById(R.id.fpac_editcomment);

        addCommentDialog.setCaller(edit_comment, commentsFragment);
        addCommentDialog.setPost(post);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                setResult(RESULT_OK, backIntent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenuHelper helper = new PopupMenuHelper(R.menu.fullpost_more_menu, FullPostActivity.this, more);
                if (post.getUserid().equals(User.user.getId())){
                    helper.hideItem(R.id.fullpost_more_follow);
                }else{
                    helper.hideItem(R.id.fullpost_more_delete);
                    boolean isFollowing = User.user.isFollowing(post.getUserid());
                    helper.modifyIcon(R.id.fullpost_more_follow, isFollowing ? "Unfollow" : "Follow",isFollowing ? R.drawable.ic_unfollow : R.drawable.ic_follow);
                }
                helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
                    @Override
                    public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.fullpost_more_follow:
                                User.user.follow(post.getUserid());
                                return true;
                            case R.id.fullpost_more_copy:
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Copy", content.getText());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(FullPostActivity.this,"Copied!",Toast. LENGTH_SHORT).show();
                                return true;
                            case R.id.fullpost_more_delete:
                                User.user.removePost(post);
                                Intent intent = new Intent();
                                intent.putExtra(POST, post);
                                setResult(FullPostActivity.DELETE_POST, intent);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                                return true;
                            case R.id.fullpost_more_report:
                                return true;
                        }
                        return false;
                    }
                });
                helper.show();
            }
        });
        include.setClickable(false);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position, true);
                tab.select();
                if (position == 1) ratingsFragment.setRating(post);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        int comments_count = post.getComments().size();
        comments_counter.setText(comments_count + "");
        comments_counter.setVisibility(comments_count == 0 ? View.GONE : View.VISIBLE);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddComment();
            }
        });
        dropdown.setVisibility(View.GONE);
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

    private void setPost(final Post post){
        User user = User.findUser(post.getUserid());
        poster_icon.setClickable(true);
        poster_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPostActivity.this, UserProfileActivity.class);
                intent.putExtra(UserProfileActivity.USERID, post.getUserid());
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        username.setText(user.getName());
        if (user.getIcon() == null) poster_icon.setImageResource(R.drawable.user_icon);
        else poster_icon.setImageBitmap(user.getIcon());
        time.setText(TimeText(post.getTime()));
        content.setText(post.getContent());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (post.getUserid().equals(User.user.getId())){
                    Toast.makeText(FullPostActivity.this, "You can't rate your own post!", Toast.LENGTH_SHORT).show();
                    ratingBar.setRating(0);
                }else{
                    if (rating == 0)
                        ratingBar.setRating(post.ratings_avg());
                    else
                        User.user.rate(new Rating(User.user.getId(), (int) rating, Calendar.getInstance().getTimeInMillis(), post.GetKey()));
                }
            }
        });
        setCollect(User.user.hasCollected(post));
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect(post);
            }
        });
    }

    public static String TimeText(Long time){
        Calendar date = Calendar.getInstance(), now = Calendar.getInstance();
        date.setTimeInMillis(time);
        if (now.get(Calendar.YEAR) != date.get(Calendar.YEAR)) return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
        else if (now.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)) return new SimpleDateFormat("HH:mm").format(time);
        return new SimpleDateFormat("MM-dd HH:mm").format(time);
    }

    public void collect(Post post){
        boolean collected = User.user.hasCollected(post);
        if (collected) User.user.uncollect(post);
        else User.user.collect(post);
        setCollect(!collected);
    }
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
        addCommentDialog.showNow(getSupportFragmentManager(), ADD_COMMENT);
    }
}
