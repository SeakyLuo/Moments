package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FullPostActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter viewPager;
    private ImageButton backButton;
    private ImageView usericon;
    private TextView username;
    private TextView time;
    private TextView content;
    private ImageButton comment;
    private ImageButton collect;
    private ImageButton share;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpost);
        SwipeBackHelper.onCreate(this);

        intent = getIntent();
        Post post = Post.fromJson(intent.getStringExtra("Post"));
        setPost(post);

        backButton = findViewById(R.id.fullpost_back);
        mViewPager = findViewById(R.id.fullpost_viewpager);
        mTabLayout = findViewById(R.id.fullpost_tablayout);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });
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

        viewPager = new TabPagerAdapter(getSupportFragmentManager());
        viewPager.addFragment(new FullPostCommetsFragment(), "Comments");
        viewPager.addFragment(new FullPostRatingsFragment(), "Ratings");
        mViewPager.setAdapter(viewPager);
    }

    private void setPost(Post post){
        CardView view = findViewById(R.id.up_post);
        usericon = view.findViewById(R.id.post_usericon);
        username = view.findViewById(R.id.post_username);
        time = view.findViewById(R.id.post_time);
        content = view.findViewById(R.id.post_content);
        comment = view.findViewById(R.id.post_comment);
        collect = view.findViewById(R.id.post_collect);
        share = view.findViewById(R.id.post_share);
        usericon.setClickable(true);
        usericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        User user = User.findUser(post.getUserid());
        username.setText(user.getUsername());
//        usericon.setImageBitmap(user.getIcon());
        time.setText(TimeText(post.getDate()));
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
        collect.setImageResource(collected ? R.drawable.ic_collected : R.drawable.ic_collection);
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
